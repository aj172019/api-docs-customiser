package com.example.apidocs.controller;

import com.example.apidocs.service.OpenApiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OpenApiController {

    private final OpenApiService openApiService;

    public OpenApiController(OpenApiService openApiService) {
        this.openApiService = openApiService;
    }

    @GetMapping("/openapi.json")
    public ResponseEntity<String> getMergedOpenApi() throws JsonProcessingException {
        return ResponseEntity.ok(openApiService.getMergedOpenApi());
    }
}
