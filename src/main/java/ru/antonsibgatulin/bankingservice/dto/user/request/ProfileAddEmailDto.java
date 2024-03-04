package ru.antonsibgatulin.bankingservice.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Profile Add Email DTO")
public class ProfileAddEmailDto {
    @NotBlank(message = "Email is required")
    @Email(message = "Email is not valid")
    @Schema(description = "Email address to be added")
    private String email;
}