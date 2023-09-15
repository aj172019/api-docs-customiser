package com.example.apidocs.openapi.exception;

public abstract class OpenApiDocsRuntimeException extends RuntimeException {
    OpenApiDocsRuntimeException(String message) {
        super(message);
    }
}
