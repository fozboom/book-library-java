package com.daniel.projects.booklibrary.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition (
		info = @Info(
                title = "Book Library API",
				description = "Book library",
                version = "1.0",
				contact = @Contact (
						name = "Daniel",
						email = "fozboom@gmail.com"
				)
        )
)
public class BookLibraryApiConfig {
}
