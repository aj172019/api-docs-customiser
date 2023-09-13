package com.example.apidocs.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.OpenAPI;

public class OpenApiDocsJsonConverter {

    private OpenApiDocsJsonConverter() {
        throw new IllegalStateException("Utility class");
    }

    public static String convertOpenApiToJson(OpenAPI openApi) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper.writeValueAsString(openApi);
    }
}
