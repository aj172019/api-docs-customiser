package com.example.apidocs.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "msa")
public class MsaProperties {

    private List<Domain> domains;

    @Getter
    @Setter
    public static class Domain {
        private String name;
        private String host;
        private boolean isHttps = false;
        private String apiDocsPath;
    }
}
