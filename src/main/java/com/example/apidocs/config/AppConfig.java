package com.example.apidocs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
    // restTemplate bean
     @Bean
     public RestTemplate restTemplate() {
         return new RestTemplate();
     }
}
