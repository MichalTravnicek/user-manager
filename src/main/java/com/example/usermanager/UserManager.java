package com.example.usermanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@SpringBootApplication
@ComponentScan
public class UserManager {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("User Management API")
						.version("1.0")
						.description("API for managing users")
						.contact(new Contact()
								.name("API Support")
								.email("support@company.com"))
						.license(new License()
								.name("MIT")
								.url("https://opensource.org/licenses/MIT")));
	}

	public static void main(String[] args) {
		SpringApplication.run(UserManager.class, args);
	}
}
