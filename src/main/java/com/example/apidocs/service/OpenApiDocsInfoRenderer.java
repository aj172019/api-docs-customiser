package com.example.apidocs.service;

import io.swagger.v3.oas.models.info.Info;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OpenApiDocsInfoRenderer {

    private final Info info;
    private final StringBuilder descriptionBuilder;

    private OpenApiDocsInfoRenderer() {
        this.info = new Info();
        this.descriptionBuilder = new StringBuilder();
    }

    public static OpenApiDocsInfoRenderer create() {
        return new OpenApiDocsInfoRenderer();
    }

    public OpenApiDocsInfoRenderer title(String title) {
        info.setTitle(title);
        return this;
    }

    public OpenApiDocsInfoTable table() {
        return new OpenApiDocsInfoTable(this);
    }

    public Info build() {
        info.setDescription(descriptionBuilder.toString());
        return info;
    }

    public OpenApiDocsInfoRenderer append(String content) {
        descriptionBuilder.append(content).append("\n");
        return this;
    }

    protected static class OpenApiDocsInfoTable {
        private final OpenApiDocsInfoRenderer renderer;
        private final List<String> headers = new ArrayList<>();
        private final List<List<String>> rows = new ArrayList<>();

        public OpenApiDocsInfoTable(OpenApiDocsInfoRenderer renderer) {
            this.renderer = renderer;
        }

        public OpenApiDocsInfoTableHeader header(String... headers) {
            this.headers.addAll(Arrays.asList(headers));
            return new OpenApiDocsInfoTableHeader(this);
        }

        private void addRow(String... columns) {
            rows.add(Arrays.asList(columns));
        }

        OpenApiDocsInfoRenderer endTable() {
            StringBuilder tableBuilder = new StringBuilder();

            // Add headers
            tableBuilder.append("|").append(String.join("|", headers)).append("|\n");

            // Add header delimiters
            String[] delimiters = new String[headers.size()];
            Arrays.fill(delimiters, "---");
            tableBuilder.append("|").append(String.join("|", delimiters)).append("|\n");

            // Add rows
            for (List<String> row : rows) {
                tableBuilder.append("|").append(String.join("|", row)).append("|\n");
            }

            return renderer.append(tableBuilder.toString());
        }
    }

    protected static class OpenApiDocsInfoTableHeader {
        private final OpenApiDocsInfoTable table;

        public OpenApiDocsInfoTableHeader(OpenApiDocsInfoTable table) {
            this.table = table;
        }

        public OpenApiDocsInfoTableRow row() {
            return new OpenApiDocsInfoTableRow(table);
        }

        public OpenApiDocsInfoTableRow row(String... columns) {
            table.addRow(columns);
            return row();
        }
    }

    protected static class OpenApiDocsInfoTableRow {
        private final OpenApiDocsInfoTable table;

        public OpenApiDocsInfoTableRow(OpenApiDocsInfoTable table) {
            this.table = table;
        }

        public OpenApiDocsInfoTableRow row(String... columns) {
            table.addRow(columns);
            return this;
        }

        public OpenApiDocsInfoRenderer endTable() {
            return table.endTable();
        }
    }
}
