package com.example.demo.infrastructure.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI templateOpenApi() {
        return new OpenAPI().info(
                new Info()
                        .title("Resource API")
                        .description("Resource management API")
                        .version("v1")
                        .contact(new Contact().name("Template Team").email("team@example.com"))
        );
    }
}
