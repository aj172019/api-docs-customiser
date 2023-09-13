package com.example.apidocs.service;

import com.example.apidocs.exception.OpenApiValidationException;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Configuration;

import java.util.Map;


public interface OpenApiDocsValidator {

    default void validate(OpenAPI openApi) {
        if (openApi == null) {
            throw new OpenApiValidationException("OpenAPI cannot be null");
        }
        if (openApi.getPaths().isEmpty()) {
            throw new OpenApiValidationException("OpenAPI.paths cannot be null");
        }
        for (Map.Entry<String, PathItem> entry: openApi.getPaths().entrySet()) {
            if (entry.getValue().getServers().isEmpty()) {
                throw new OpenApiValidationException("OpenAPI.paths." + entry.getKey() + ".servers cannot be null");
            }
            for (Server server : entry.getValue().getServers()) {
                if (server.getUrl() == null || server.getUrl().contains("localhost")) {
                    throw new OpenApiValidationException("OpenAPI.paths." + entry.getKey() + ".servers.url cannot be null or localhost");
                }
            }
        }
    }
}
