package com.example.apidocs.service.validator;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class OpenApiDocsValidatorFactory {

    private final ApplicationContext applicationContext;

    public OpenApiDocsValidatorFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public OpenApiDocsValidator getValidator(Class<? extends OpenApiDocsValidator> validatorClass) {
        return applicationContext.getBean(validatorClass);
    }
}
