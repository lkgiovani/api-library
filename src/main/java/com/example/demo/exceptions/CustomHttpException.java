package com.example.demo.exceptions;

import org.springframework.http.ResponseEntity;

public class CustomHttpException extends RuntimeException{
    private final int statusCode;

    public CustomHttpException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public ResponseEntity<?> toResponseEntity() {
        return ResponseEntity.status(this.statusCode).body(this.getMessage());
    }
}
