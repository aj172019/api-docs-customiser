package com.example.apidocs.service;

import com.example.apidocs.config.MsaProperties;
import com.example.apidocs.config.MsaProperties.Domain;
import com.example.apidocs.constant.ErrorType;
import com.example.apidocs.exception.MissingServerPropertyException;
import com.example.apidocs.exception.OpenApiDocsNetworkException;
import com.example.apidocs.model.ErrorInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OpenApiService {

    private final MsaProperties msaProperties;

    private final OpenApiDocsFetcher openApiDocsFetcher;

    public OpenApiService(MsaProperties msaProperties, OpenApiDocsFetcher openApiDocsFetcher) {
        this.msaProperties = msaProperties;
        this.openApiDocsFetcher = openApiDocsFetcher;
    }

    public String getServicesOpenApiDocs() throws JsonProcessingException {
        OpenAPI target = new OpenAPI();
        List<ErrorInfo> errorInfos = new ArrayList<>();
        for (Domain domain : msaProperties.getDomains()) {
            ErrorInfo errorInfo = ErrorInfo.createErrorInfo(domain);
            try {
                String openApiSpecYaml = openApiDocsFetcher.fetchOpenApiYaml(domain);
                OpenAPI source = OpenApiDocsParser.parse(openApiSpecYaml);
                target = OpenApiDocsCustomizer.merge(target, source);
            } catch (Exception e) {
                if (e instanceof OpenApiDocsNetworkException) {
                    errorInfo.add(ErrorType.NETWORK_ERROR);
                }
                if (e instanceof MissingServerPropertyException) {
                    errorInfo.add(ErrorType.MISSING_SERVER_PROPERTY);
                }
                e.printStackTrace();
            }
            errorInfos.add(errorInfo);
        }

        target.info(renderInformation(errorInfos));

        return OpenApiDocsJsonConverter.convertOpenApiToJson(target);
    }

    private static Info renderInformation(List<ErrorInfo> errorInfos) {
        OpenApiDocsInfoRenderer.OpenApiDocsInfoTableHeader renderer = OpenApiDocsInfoRenderer.create()
                .title("Megazone Play API Documentation")
                .append("### Megazone Play Servers Status")
                .table()
                .header("Server", "Status", "Remark");

        OpenApiDocsInfoRenderer.OpenApiDocsInfoTableRow row = renderer.row();
        // Iterate over errorInfos to add rows to the table
        for (ErrorInfo errorInfo : errorInfos) {
            if (errorInfo != null) { // Ensure errorInfo is not null
                String server = errorInfo.getDomain().getName(); // Assuming there's a getter for domain in ErrorInfo
                String status = errorInfo.getErrorList().isEmpty() ? "☀️" : "⛈️";
                String remark = String.join("<br/>",errorInfo.getErrorList().stream().map(ErrorType::getMessage).toList());
                row.row(server, status, remark);
            }
        }

        // End table creation and other configurations
        return row.endTable().build();
    }
}
