package com.myshop.products.product_service.model;

public record Product(
        String productId,
        String name,
        String category,
        int price,
        String status) {
}
