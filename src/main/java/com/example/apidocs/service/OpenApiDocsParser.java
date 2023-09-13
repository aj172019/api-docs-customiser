package com.example.apidocs.service;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;

public class OpenApiDocsParser {

    private OpenApiDocsParser() {
        throw new IllegalStateException("Utility class");
    }

    public static OpenAPI parse(String yaml) {
        OpenAPIV3Parser parser = new OpenAPIV3Parser();
        return parser.readContents(yaml, null, null).getOpenAPI();
    }
}
