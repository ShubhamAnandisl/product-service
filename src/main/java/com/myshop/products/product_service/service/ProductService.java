package com.myshop.products.product_service.service;

import com.myshop.products.product_service.model.CreateProductRequest;
import com.myshop.products.product_service.model.Product;
import com.myshop.products.product_service.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public Product getProduct(String productId) {
        return repository.getByProductId(productId);
    }

    public void createOrUpdateProduct(CreateProductRequest request) {
        repository.saveProduct(request);
    }
}
