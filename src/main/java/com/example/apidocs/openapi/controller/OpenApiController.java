package com.example.apidocs.openapi.controller;

import com.example.apidocs.openapi.service.OpenApiDocsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OpenApiController {

    private final OpenApiDocsService openApiDocsService;

    public OpenApiController(OpenApiDocsService openApiDocsService) {
        this.openApiDocsService = openApiDocsService;
    }

    @GetMapping("/openapi.json")
    public ResponseEntity<String> getMergedOpenApi() throws JsonProcessingException {
        return ResponseEntity.ok(openApiDocsService.getServicesOpenApiDocs());
    }
}
