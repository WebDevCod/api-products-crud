package com.example.springboot.exceptions;

public class ProductsListEmptyException extends RuntimeException {
    public ProductsListEmptyException(String message) {
        super(message);
    }
}
