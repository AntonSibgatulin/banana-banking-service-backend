package ru.antonsibgatulin.bankingservice.config.swagger;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@io.swagger.v3.oas.annotations.security.SecurityScheme(
        name = "JWT",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Banking service")
                        .version("1.0.0")
                        .description("We need to develop a service for \"banking\" operations. In our system, we have users (clients), each with strictly one \"bank account\" that initially contains a certain amount of money. It is possible to transfer money between clients. Additionally, interest is accrued on the funds.\n"))
                ;
    }

/*
    @Bean
    public SecurityScheme securityScheme() {
        return new SecurityScheme()
                .name("bearerAuth")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");
    }


 */
}