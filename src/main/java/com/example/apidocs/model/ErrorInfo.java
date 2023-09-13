package com.example.apidocs.model;

import com.example.apidocs.config.MsaProperties.Domain;
import lombok.Getter;

@Getter
public class ErrorInfo {
    private final Domain domain;
    private String message;

    private ErrorInfo(Domain domain) {
        this.domain = domain;
        this.message = "";
    }

    public static ErrorInfo createErrorInfo(Domain domain) {
        return new ErrorInfo(domain);
    }

    public void message(String message) {
        this.message = message;
    }
}
