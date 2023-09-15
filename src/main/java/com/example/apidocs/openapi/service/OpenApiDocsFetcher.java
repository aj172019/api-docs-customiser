package com.example.apidocs.openapi.service;

import com.example.apidocs.config.MsaPlayProperties.Domain;
import com.example.apidocs.openapi.exception.OpenApiDocsNetworkException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenApiDocsFetcher {

    private final RestTemplate restTemplate;

    public OpenApiDocsFetcher(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String fetchOpenApiYaml(Domain domain) throws OpenApiDocsNetworkException {
        String endpoint = domain.isHttps() ? "https://" : "http://" + domain.getHost() + domain.getApiDocsPath();
        try {
            return restTemplate.getForObject(endpoint, String.class);
        } catch (Exception e) {
            throw new OpenApiDocsNetworkException("Failed to fetch OpenAPI YAML");
        }
    }
}
