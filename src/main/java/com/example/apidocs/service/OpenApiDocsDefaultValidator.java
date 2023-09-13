package com.example.apidocs.service;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.stereotype.Service;

@Service
public class OpenApiDocsDefaultValidator implements OpenApiDocsValidator {
    @Override
    public void validate(OpenAPI openApi) {
        OpenApiDocsValidator.super.validate(openApi);
    }
}
