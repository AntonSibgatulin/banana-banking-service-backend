package ru.antonsibgatulin.bankingservice.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Schema(description = "User Authentication DTO")
public class UserAuthenticationDto {
    @Schema(description = "User login")
    private String login;

    @Schema(description = "User password")
    private String password;
}