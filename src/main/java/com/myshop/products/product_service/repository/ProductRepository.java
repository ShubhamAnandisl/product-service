package com.myshop.products.product_service.repository;

import com.myshop.products.product_service.exception.ProductNotFoundException;
import com.myshop.products.product_service.exception.ProductPersistenceException;
import com.myshop.products.product_service.model.CreateProductRequest;
import com.myshop.products.product_service.model.Product;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ProductRepository {

    private final DynamoDbClient dynamoDbClient;
    private static final String TABLE_NAME = "products";

    public ProductRepository(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    public Product getByProductId(String productId) {
        try {
            Map<String, AttributeValue> key = Map.of(
                    "pk", AttributeValue.fromS("PRODUCT#" + productId),
                    "sk", AttributeValue.fromS("METADATA")
            );

            GetItemRequest request = GetItemRequest.builder()
                    .tableName(TABLE_NAME)
                    .key(key)
                    .build();

            GetItemResponse response = dynamoDbClient.getItem(request);

            if (!response.hasItem()) {
                throw new ProductNotFoundException(productId);
            }

            Map<String, AttributeValue> item = response.item();

            return new Product(
                    item.get("productId").s(),
                    item.get("name").s(),
                    item.get("category").s(),
                    Integer.parseInt(item.get("price").n()),
                    item.get("status").s()
            );

        } catch (ResourceNotFoundException e) {
            throw new ProductPersistenceException("Products table not found", e);

        } catch (DynamoDbException e) {
            throw new ProductPersistenceException("Failed to fetch product", e);
        }
    }

    public void saveProduct(CreateProductRequest request) {
        try {
            Map<String, AttributeValue> item = new HashMap<>();

            item.put("pk", AttributeValue.fromS("PRODUCT#" + request.productId()));
            item.put("sk", AttributeValue.fromS("METADATA"));
            item.put("productId", AttributeValue.fromS(request.productId()));
            item.put("name", AttributeValue.fromS(request.name()));
            item.put("category", AttributeValue.fromS(request.category()));
            item.put("price", AttributeValue.fromN(String.valueOf(request.price())));
            item.put("status", AttributeValue.fromS(request.status()));
            item.put("createdAt", AttributeValue.fromS(Instant.now().toString()));

            PutItemRequest putRequest = PutItemRequest.builder()
                    .tableName(TABLE_NAME)
                    .item(item)
                    .build();

            dynamoDbClient.putItem(putRequest);

        } catch (DynamoDbException e) {
            throw new ProductPersistenceException("Failed to save product", e);
        }
    }
}
