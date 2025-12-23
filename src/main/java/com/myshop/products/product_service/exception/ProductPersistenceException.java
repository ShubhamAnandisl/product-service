package com.myshop.products.product_service.exception;

public class ProductPersistenceException extends RuntimeException {

    public ProductPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
