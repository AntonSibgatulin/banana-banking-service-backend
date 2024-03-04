package ru.antonsibgatulin.bankingservice.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "User Authentication DTO")
public class UserAuthenticationDto {
    @Schema(description = "User login")
    private String login;

    @Schema(description = "User password")
    private String password;
}