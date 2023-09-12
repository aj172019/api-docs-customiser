package com.example.apidocs.model;

import com.example.apidocs.config.MsaProperties.Domain;
import com.example.apidocs.constant.ErrorType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
@Getter
public class ErrorInfo {
    private final Domain domain;
    private final List<ErrorType> errorList;

    private ErrorInfo(Domain domain) {
        this.domain = domain;
        this.errorList = new ArrayList<>();
    }

    public String getStatus() {
        if (errorList.contains(ErrorType.NETWORK_ERROR)) {
            return "⛈️";
        }
        return "☀️";
    }

    public static ErrorInfo createErrorInfo(Domain domain) {
        return new ErrorInfo(domain);
    }

    public void add(ErrorType errorType) {
        errorList.add(errorType);
    }
}
