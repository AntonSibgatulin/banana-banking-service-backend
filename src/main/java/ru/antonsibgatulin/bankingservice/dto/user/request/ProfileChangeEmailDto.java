package ru.antonsibgatulin.bankingservice.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Profile Change Email DTO")
public class ProfileChangeEmailDto {
    @NotBlank(message = "Email is required")
    @Email(message = "Email is not valid")
    @Schema(description = "New email address")
    private String email;
}