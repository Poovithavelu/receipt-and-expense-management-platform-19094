package com.example.receiptprocessingbackend;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Receipt Processing Backend API",
				version = "0.1.0",
				description = "RESTful APIs for uploading and processing receipts, OCR extraction, "
						+ "expense categorization, versioning, and search.",
				contact = @Contact(name = "Support", email = "support@example.com"),
				license = @License(name = "Apache 2.0")
		),
		servers = {
				@Server(url = "/", description = "Default Server")
		},
		tags = {
				@Tag(name = "Documents", description = "Upload, versioning, and retrieval of documents"),
				@Tag(name = "Processing", description = "OCR and categorization operations"),
				@Tag(name = "Search", description = "Search and filter documents")
		}
)
@SecurityScheme(name = "none", type = SecuritySchemeType.HTTP, scheme = "none")
public class receiptprocessingbackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(receiptprocessingbackendApplication.class, args);
	}
}
