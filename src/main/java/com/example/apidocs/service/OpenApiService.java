package com.example.apidocs.service;

import com.example.apidocs.config.MsaProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenApiService {

    private final MsaProperties msaProperties;

    private final RestTemplate restTemplate;

    public OpenApiService(MsaProperties msaProperties, RestTemplate restTemplate) {
        this.msaProperties = msaProperties;
        this.restTemplate = restTemplate;
    }

    public String getMergedOpenApi() throws JsonProcessingException {
        OpenAPI openApi = initializeBaseOpenApi();
        customizeOpenApi(openApi);
        return convertOpenApiToJson(openApi);
    }

    private OpenAPI initializeBaseOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Megazone Play API Documentation")
                        .description("""
                                ### Megazone Play Servers Status
                                |Server|Status|remark|
                                |---|---|---|
                                """));
    }

    private void customizeOpenApi(OpenAPI openApi) {
        Info info = openApi.getInfo();
        for (MsaProperties.Domain domain : msaProperties.getDomains()) {
                String completeUrl = domain.isHttps() ? "https://" : "http://" + domain.getHost() + domain.getApiDocsPath();
                SwaggerParseResultWithStatus parseResultWithStatus = fetchAndParseOpenApi(completeUrl);
                if (parseResultWithStatus.parseResult.getOpenAPI() != null) {
                    mergeOpenApi(openApi, parseResultWithStatus.parseResult);
                }
                updateInfoDescriptionWithServerStatus(info, domain, parseResultWithStatus);
        }
    }

    private String fetchOpenApiYaml(String serviceUrl) {
        return restTemplate.getForObject(serviceUrl, String.class);
    }

    private void mergeOpenApi(OpenAPI openApi, SwaggerParseResult parseResult) {
        mergeTags(openApi, parseResult);
        mergePaths(openApi, parseResult);
        mergeSchemas(openApi, parseResult);
    }

    private void mergeTags(OpenAPI target, SwaggerParseResult source) {
        if (source.getOpenAPI().getTags() == null) {
            return;
        }
        if (target.getTags() == null) {
            target.setTags(source.getOpenAPI().getTags());
            return;
        }
        target.getTags().addAll(source.getOpenAPI().getTags());
    }

    private void mergePaths(OpenAPI target, SwaggerParseResult source) {
        if (source.getOpenAPI().getPaths() == null) {
            return;
        }
        if (target.getPaths() == null) {
            target.setPaths(source.getOpenAPI().getPaths());
            return;
        }
        target.getPaths().putAll(source.getOpenAPI().getPaths());
    }

    private void mergeSchemas(OpenAPI target, SwaggerParseResult source) {
        if (source.getOpenAPI().getComponents() == null) {
            return;
        }
        initializeComponentsIfAbsent(target);
        if (target.getComponents().getSchemas() == null) {
            target.getComponents().setSchemas(source.getOpenAPI().getComponents().getSchemas());
            return;
        }
        target.getComponents().getSchemas().putAll(source.getOpenAPI().getComponents().getSchemas());
    }

    private void initializeComponentsIfAbsent(OpenAPI openApi) {
        if (openApi.getComponents() == null) {
            openApi.setComponents(new Components());
        }
    }

    private String convertOpenApiToJson(OpenAPI openApi) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper.writeValueAsString(openApi);
    }

    private void updateInfoDescriptionWithServerStatus(Info info, MsaProperties.Domain domain, SwaggerParseResultWithStatus parseResultWithStatus) {
        String description = info.getDescription() + "|%s|☀️||%n".formatted(domain.getName());
        if (parseResultWithStatus.serverStatus == ServerStatus.NETWORK_ERROR) {
            description = info.getDescription() + "|%s|⛈️|**[%s] server is not available. Please ask [%s] Backend team**|%n".formatted(domain.getName(), domain.getName(), domain.getName());
        }
        if (parseResultWithStatus.serverStatus == ServerStatus.MISSING_PATH_SERVER_PROPERTY) {
            description = info.getDescription() + "|%s|🌧️|**[%s] server`s openapi spec is missing `servers` property. Please ask [%s] Backend team**|%n".formatted(domain.getName(), domain.getName(), domain.getName());
        }
        info.setDescription(description);
    }

    private SwaggerParseResultWithStatus fetchAndParseOpenApi(String serviceUrl) {
        SwaggerParseResultWithStatus resultWithStatus = new SwaggerParseResultWithStatus();
        try {
            String yamlContent = fetchOpenApiYaml(serviceUrl);
            OpenAPIV3Parser parser = new OpenAPIV3Parser();
            resultWithStatus.parseResult = parser.readContents(yamlContent, null, null);
            resultWithStatus.serverStatus = ServerStatus.NORMAL;
        } catch (Exception e) {
            resultWithStatus.parseResult = new SwaggerParseResult();
            resultWithStatus.serverStatus = ServerStatus.NETWORK_ERROR;
        }
        return resultWithStatus;
    }


    private static class SwaggerParseResultWithStatus {
        private SwaggerParseResult parseResult;
        private ServerStatus serverStatus;
    }

    private enum ServerStatus {
        NORMAL("☀️"),
        MISSING_PATH_SERVER_PROPERTY("🌧️"),
        NETWORK_ERROR("⛈️");

        private final String emoji;

        ServerStatus(String emoji) {
            this.emoji = emoji;
        }

        public String getEmoji() {
            return emoji;
        }
    }
}
