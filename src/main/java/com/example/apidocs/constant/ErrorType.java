package com.example.apidocs.constant;

public enum ErrorType {
    NORMAL(null),
    MISSING_SERVER_PROPERTY("**Cannot test. missing `servers` property. Please ask Backend team**"),
    NETWORK_ERROR("**Server is not available. Please ask Backend team**");

    private final String message;

    ErrorType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
