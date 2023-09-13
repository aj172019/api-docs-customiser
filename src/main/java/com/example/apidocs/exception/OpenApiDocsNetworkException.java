package com.example.apidocs.exception;

public class OpenApiDocsNetworkException extends RuntimeException{
    public OpenApiDocsNetworkException(String message, Throwable cause) {
        super(message, cause);
    }
}
