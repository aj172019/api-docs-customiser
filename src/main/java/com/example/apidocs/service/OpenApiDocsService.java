package com.example.apidocs.service;

import com.example.apidocs.config.MsaProperties;
import com.example.apidocs.config.MsaProperties.Domain;
import com.example.apidocs.exception.OpenApiDocsNetworkException;
import com.example.apidocs.exception.OpenApiValidationException;
import com.example.apidocs.model.ErrorInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OpenApiDocsService {

    private final MsaProperties msaProperties;

    private final OpenApiDocsFetcher openApiDocsFetcher;
    private final ApplicationContext applicationContext;

    public OpenApiDocsService(MsaProperties msaProperties, OpenApiDocsFetcher openApiDocsFetcher, ApplicationContext applicationContext) {
        this.msaProperties = msaProperties;
        this.openApiDocsFetcher = openApiDocsFetcher;
        this.applicationContext = applicationContext;
    }

    public String getServicesOpenApiDocs() throws JsonProcessingException {
        OpenAPI target = new OpenAPI();
        List<ErrorInfo> errorInfos = new ArrayList<>();
        for (Domain domain : msaProperties.getDomains()) {
            ErrorInfo errorInfo = ErrorInfo.createErrorInfo(domain);
            try {
                String openApiSpecYaml = openApiDocsFetcher.fetchOpenApiYaml(domain);
                OpenAPI source = OpenApiDocsParser.parse(openApiSpecYaml);
                if (msaProperties.isValidate()) {
                    OpenApiDocsValidator validator = applicationContext.getBean(domain.getValidator());
                    validator.validate(source);
                }
                target = OpenApiDocsMerger.merge(target, source);
            } catch (Exception e) {
                if (e instanceof OpenApiDocsNetworkException) {
                    errorInfo.message(getFormatted(domain, e));
                }
                if (e instanceof OpenApiValidationException) {
                    errorInfo.message(getFormatted(domain, e));
                }
                e.printStackTrace();
            }
            errorInfos.add(errorInfo);
        }

        target.info(renderInformation(errorInfos));

        return OpenApiDocsJsonConverter.convertOpenApiToJson(target);
    }

    private static String getFormatted(Domain domain, Exception e) {
        return "**Error!!!**<br/><br/>**Domain**:`%s`<br/><br/>**Host**:`%s`<br/><br/>**Message**: %s".formatted(domain.getName(), domain.getHost(), e.getMessage());
    }

    private static Info renderInformation(List<ErrorInfo> errorInfos) {
        // errorInfos to List<String[]>
        List<String[]> rows = errorInfos.stream().map(errorInfo -> new String[]{errorInfo.getDomain().getName(), StringUtils.isEmpty(errorInfo.getMessage()) ? "☀️" : "⛈️", errorInfo.getMessage()}).toList();

        // End table creation and other configurations
        return OpenApiDocsInfoRenderer.create()
                .title("Megazone Play API Documentation")
                .append("### Megazone Play Servers Status")
                .table()
                .header("Server", "Status", "Remark")
                .rows(rows)
                .endTable()
                .build();
    }
}