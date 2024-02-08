package com.projects.chatterboxapi;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Chat App Rest API",
				description = "Chat App Rest API Documentation",
				version = "v1.0",
				contact = @Contact(
						name = "John Christopher Ilacad",
						email = "johnchristopherilacad27@gmail.com"
				),
				license = @License(
						name = "Apache 2.0"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "Chat App Rest Api Documentation",
				url = "https://github.com/jcilacad/chatterbox-rest-api"
		)
)
public class ChatterboxApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatterboxApiApplication.class, args);
	}

}
