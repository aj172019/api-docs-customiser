package com.example.apidocs.service;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;

public class OpenApiDocsCustomizer {

    private OpenApiDocsCustomizer() {
        throw new IllegalStateException("Utility class");
    }

    public static OpenAPI merge(OpenAPI target, OpenAPI source) {
        mergeTags(target, source);
        mergePaths(target, source);
        mergeSchemas(target, source);
        return target;
    }

    private static void mergeTags(OpenAPI target, OpenAPI source) {
        if (source.getTags() == null) {
            return;
        }
        if (target.getTags() == null) {
            target.setTags(source.getTags());
            return;
        }
        target.getTags().addAll(source.getTags());
    }

    private static void mergePaths(OpenAPI target, OpenAPI source) {
        if (source.getPaths() == null) {
            return;
        }
        if (target.getPaths() == null) {
            target.setPaths(source.getPaths());
            return;
        }
        target.getPaths().putAll(source.getPaths());
    }

    private static void mergeSchemas(OpenAPI target, OpenAPI source) {
        if (source.getComponents() == null) {
            return;
        }
        if (target.getComponents() == null) {
            target.setComponents(new Components());
        }
        if (target.getComponents().getSchemas() == null) {
            target.getComponents().setSchemas(source.getComponents().getSchemas());
            return;
        }
        target.getComponents().getSchemas().putAll(source.getComponents().getSchemas());
    }



}
