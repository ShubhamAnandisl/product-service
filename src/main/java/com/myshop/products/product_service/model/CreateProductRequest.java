package com.myshop.products.product_service.model;

public record CreateProductRequest(
        String productId,
        String name,
        String category,
        int price,
        String status
) {
}
