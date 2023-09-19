package com.pockettracker.user.openapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@OpenAPIDefinition(info = @Info(title = "User service rest API", version = "1.0",
        description = "API specification for user service",
        contact = @Contact(name = "Dmytro Horban")))
@SecurityScheme(name = "bearerToken", type = SecuritySchemeType.HTTP,
        scheme = "bearer", bearerFormat = "JWT")
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .tags(List.of(
                        new Tag()
                                .name("Authentication")
                                .description("Auth controller")
                ))
                .path("/api/v1/auth/logout", new PathItem()
                        .delete(new Operation()
                                .tags(List.of(
                                        "Authentication"
                                ))
                                .summary("Logout current user and clear cookies")
                                .operationId("logout")
                                .responses(new ApiResponses()
                                        .addApiResponse("200", new ApiResponse().description("OK"))
                                )
                        )
                );
    }
}
