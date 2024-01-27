package com.example.springboot.util;

import com.example.springboot.exceptions.ProductNotFoundException;
import com.example.springboot.exceptions.ProductsListEmptyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ProductNotFoundException.class)
    private ResponseEntity<String> productNotFoundHandler(ProductNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found with id: " + exception.getMessage());
    }

    @ExceptionHandler(ProductsListEmptyException.class)
    private ResponseEntity<String> productListEmptyHandler(ProductsListEmptyException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

}
