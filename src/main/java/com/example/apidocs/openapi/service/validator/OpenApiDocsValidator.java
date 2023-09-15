package com.example.apidocs.openapi.service.validator;

import com.example.apidocs.openapi.exception.OpenApiDocsValidationException;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.servers.Server;

import java.util.Map;


public interface OpenApiDocsValidator {

    default void validate(OpenAPI openApi) {
        if (openApi == null) {
            throw new OpenApiDocsValidationException("OpenAPI cannot be null");
        }
        if (openApi.getPaths().isEmpty()) {
            throw new OpenApiDocsValidationException("OpenAPI.paths cannot be null");
        }
        for (Map.Entry<String, PathItem> entry: openApi.getPaths().entrySet()) {
            if (entry.getValue().getServers().isEmpty()) {
                throw new OpenApiDocsValidationException("OpenAPI.paths." + entry.getKey() + ".servers cannot be null");
            }
            for (Server server : entry.getValue().getServers()) {
                if (server.getUrl() == null || server.getUrl().contains("localhost")) {
                    throw new OpenApiDocsValidationException("OpenAPI.paths." + entry.getKey() + ".servers.url cannot be null or localhost");
                }
            }
        }
    }
}
