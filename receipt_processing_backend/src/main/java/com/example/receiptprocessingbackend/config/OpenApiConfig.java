package com.example.receiptprocessingbackend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI specification configuration.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
            .info(new Info()
                .title("Receipt Processing Backend API")
                .description("APIs for upload, OCR extraction, categorization, versioning, search, and admin monitoring")
                .version("0.1.0"))
            .addTagsItem(new Tag().name("Documents").description("Document operations"))
            .addTagsItem(new Tag().name("Admin").description("Admin monitoring and stats"))
            .addTagsItem(new Tag().name("Meta").description("Meta and documentation"));
    }
}
