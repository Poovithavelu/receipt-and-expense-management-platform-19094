package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;

/**
 * OpenAPI configuration that sets application metadata and tag definitions
 * for improved Swagger UI documentation.
 */
@Configuration
public class OpenApiConfig {

    // PUBLIC_INTERFACE
    @Bean
    public OpenAPI receiptProcessingOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Receipt Processing Backend API")
                .description("APIs for uploading, processing, categorizing, and searching receipts and documents.")
                .version("0.1.0")
                .contact(new Contact().name("Receipt Platform").email("support@example.com")))
            .addTagsItem(new Tag().name("Hello Controller").description("Basic endpoints for service availability and info"))
            .externalDocs(new ExternalDocumentation()
                .description("Swagger UI")
                .url("/swagger-ui.html"));
    }
}
