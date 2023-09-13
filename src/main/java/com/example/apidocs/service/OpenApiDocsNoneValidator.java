package com.example.apidocs.service;

import com.example.apidocs.exception.OpenApiValidationException;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OpenApiDocsNoneValidator implements OpenApiDocsValidator {
    @Override
    public void validate(OpenAPI openApi) {
        // do nothing
    }
}
