package com.example.apidocs.constant;

public enum ErrorType {
    NORMAL("☀️", null),
    MISSING_PATH_SERVER_PROPERTY("🌧️", "Cannot test. missing `servers` property. Please ask Backend team**"),
    NETWORK_ERROR("⛈️", "Server is not available. Please ask Backend team**");

    private final String emoji;

    private final String message;

    ErrorType(String emoji, String message) {
        this.emoji = emoji;
        this.message = message;
    }

    public String getEmoji() {
        return emoji;
    }



    public String getMessage() {
        return message;
    }
}
