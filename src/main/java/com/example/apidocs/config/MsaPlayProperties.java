package com.example.apidocs.config;

import com.example.apidocs.openapi.service.validator.OpenApiDocsDefaultValidator;
import com.example.apidocs.openapi.service.validator.OpenApiDocsValidator;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "msa-play")
public class MsaPlayProperties {

    private List<Domain> domains;
    private boolean isValidate = false;

    @Getter
    @Setter
    public static class Domain {
        private String name;
        private String host;
        private boolean isHttps = false;
        private String apiDocsPath;
        private Class<? extends OpenApiDocsValidator> validator = OpenApiDocsDefaultValidator.class;
    }
}
