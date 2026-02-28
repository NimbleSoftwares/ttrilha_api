package com.nimblesoftwares.ttrilha_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("TTrilha API")
            .description("TTrilha App API Documentation")
            .version("1.0.0")
            .contact(new Contact()
                .name("Team: Luan Marcene, Odirlei Kempfer")
                .email("nimble.ask@gmail.com")
                .url("https://nimblesoftwares.com.br")
            )
        )
        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
        .components(new io.swagger.v3.oas.models.Components()
            .addSecuritySchemes("bearerAuth",
                new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                    .description("JWT token from Auth0")));
  }
}
