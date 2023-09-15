package com.example.apidocs.openapi.model;

import com.example.apidocs.config.MsaPlayProperties.Domain;
import com.example.apidocs.openapi.exception.OpenApiDocsRuntimeException;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class ErrorInfo {
    private static final String FINE_MESSAGE = "";
    private static final String ERROR_MESSAGE = "**Error!!!**<br/><br/>**Domain**:`%s`<br/><br/>**Host**:`%s`<br/><br/>**Message**: %s";

    private final Domain domain;
    private OpenApiDocsRuntimeException exception;

    private ErrorInfo(Domain domain) {
        this.domain = domain;
    }

    public static ErrorInfo createErrorInfo(Domain domain) {
        return new ErrorInfo(domain);
    }

    public void setException(OpenApiDocsRuntimeException exception) {
        this.exception = exception;
    }

    public String toFormattedString() {
        if (domain == null || StringUtils.isEmpty(domain.getName()) || StringUtils.isEmpty(domain.getHost())) {
            return FINE_MESSAGE;
        }
        if (exception == null) {
            return FINE_MESSAGE;
        }
        return ERROR_MESSAGE.formatted(domain.getName(), domain.getHost(), exception.getMessage());
    }
}
