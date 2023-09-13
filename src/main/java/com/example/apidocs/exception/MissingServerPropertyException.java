package com.example.apidocs.exception;

public class MissingServerPropertyException extends RuntimeException {
    public MissingServerPropertyException(String message, Throwable cause) {
        super(message, cause);
    }
}
