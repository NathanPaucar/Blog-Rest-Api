package com.springboot.blog;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Spring Boot Blog App Rest APIs",
				description = "Spring Boot Blog APp APIs Documentation",
				version = "v1.0",
				contact = @Contact(
						name = "Andres",
						email = "example@mail.com",
						url = "https://examplepage.com"),
				license = @License(
						name = "Apache 2.4",
						url = "https://examplepage.com"
				)
		),
		externalDocs = @ExternalDocumentation(
			description = "Spring Boot Blog App Documentation",
				url = "https://github.com/NathanPaucar/Blog-Rest-Api"
		)
)
public class SpringbootBlogRestApiApplication {

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

	public static void main(String[] args){
		SpringApplication.run(SpringbootBlogRestApiApplication.class, args);
	}
}
