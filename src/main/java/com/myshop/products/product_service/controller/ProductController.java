package com.myshop.products.product_service.controller;


import com.myshop.products.product_service.model.CreateProductRequest;
import com.myshop.products.product_service.model.Product;
import com.myshop.products.product_service.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable String productId) {
        Product product = service.getProduct(productId);

        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<Void> createProduct(@RequestBody CreateProductRequest request) {
        service.createOrUpdateProduct(request);
        return ResponseEntity.status(201).build();
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Void> updateProduct(
            @PathVariable String productId,
            @RequestBody CreateProductRequest request
    ) {
        CreateProductRequest updated =
                new CreateProductRequest(
                        productId,
                        request.name(),
                        request.category(),
                        request.price(),
                        request.status()
                );

        service.createOrUpdateProduct(updated);
        return ResponseEntity.ok().build();
    }
}
