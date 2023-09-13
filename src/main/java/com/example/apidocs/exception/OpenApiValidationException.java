package com.example.apidocs.exception;

public class OpenApiValidationException extends RuntimeException {
    public OpenApiValidationException(String message) {
        super(message);
    }
}
