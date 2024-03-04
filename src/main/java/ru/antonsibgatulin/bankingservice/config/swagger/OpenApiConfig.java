package ru.antonsibgatulin.bankingservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Банковский сервис")
                        .version("1.0.0")
                        .description("Необходимо написать сервис для “банковских” операций. В нашей системе есть пользователи (клиенты), у каждого клиента есть строго один “банковский аккаунт”, в котором изначально лежит какая-то сумма. Деньги можно переводить между клиентами. На средства также начисляются проценты."));
    }

    @Bean

    public SecurityScheme securityScheme() {

        return new SecurityScheme()
                .name("bearerAuth")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");
    }
}