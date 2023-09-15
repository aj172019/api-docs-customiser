package com.example.apidocs.openapi.service;

import com.example.apidocs.config.MsaPlayProperties;
import com.example.apidocs.config.MsaPlayProperties.Domain;
import com.example.apidocs.openapi.exception.OpenApiDocsRuntimeException;
import com.example.apidocs.openapi.model.ErrorInfo;
import com.example.apidocs.openapi.model.InfoBuilder;
import com.example.apidocs.openapi.utils.OpenApiDocsJsonConverter;
import com.example.apidocs.openapi.service.validator.OpenApiDocsValidator;
import com.example.apidocs.openapi.service.validator.OpenApiDocsValidatorFactory;
import com.example.apidocs.openapi.utils.OpenApiDocsMerger;
import com.example.apidocs.openapi.utils.OpenApiDocsParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class OpenApiDocsService {

    private final MsaPlayProperties msaPlayProperties;

    private final OpenApiDocsFetcher openApiDocsFetcher;
    
    private final OpenApiDocsValidatorFactory openApiDocsValidatorFactory;

    public OpenApiDocsService(MsaPlayProperties msaPlayProperties, OpenApiDocsFetcher openApiDocsFetcher, OpenApiDocsValidatorFactory openApiDocsValidatorFactory) {
        this.msaPlayProperties = msaPlayProperties;
        this.openApiDocsFetcher = openApiDocsFetcher;
        this.openApiDocsValidatorFactory = openApiDocsValidatorFactory;
    }

    public String getServicesOpenApiDocs() throws JsonProcessingException {
        OpenAPI target = new OpenAPI();
        List<ErrorInfo> errorInfos = new ArrayList<>();
        for (Domain domain : msaPlayProperties.getDomains()) {
            ErrorInfo errorInfo = ErrorInfo.createErrorInfo(domain);
            try {
                OpenAPI source = fetchAndParseOpenApiSpec(domain);
                if (msaPlayProperties.isValidate()) {
                    validateOpenApiSpec(domain, source);
                }
                target = OpenApiDocsMerger.merge(target, source);
            } catch (OpenApiDocsRuntimeException e) {
                errorInfo.setException(e);
                log.error(e.getMessage(), e);
            }
            errorInfos.add(errorInfo);
        }

        target.info(buildInfo(errorInfos));

        return OpenApiDocsJsonConverter.convertOpenApiToJson(target);
    }

    private OpenAPI fetchAndParseOpenApiSpec(Domain domain) {
        String openApiSpecYaml = openApiDocsFetcher.fetchOpenApiYaml(domain);
        return OpenApiDocsParser.parse(openApiSpecYaml);
    }

    private void validateOpenApiSpec(Domain domain, OpenAPI source) {
        OpenApiDocsValidator validator = openApiDocsValidatorFactory.getValidator(domain.getValidator());
        validator.validate(source);
    }

    private static Info buildInfo(List<ErrorInfo> errorInfos) {
        // errorInfos to List<String[]>
        List<String[]> rows = errorInfos.stream().map(errorInfo -> new String[]{errorInfo.getDomain().getName(), errorInfo.getException() == null ? "☀️" : "⛈️", errorInfo.toFormattedString()}).toList();

        // End table creation and other configurations
        return InfoBuilder.builder()
                .title("Megazone Play API Documentation")
                .append("### Megazone Play Server & Docs Status")
                .table()
                .header("Server", "Status", "Remark")
                .rows(rows)
                .endTable()
                .build();
    }
}
