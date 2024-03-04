package ru.antonsibgatulin.bankingservice.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(description = "Profile Change Phone DTO")
public class ProfileChangePhoneDto {
    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^\\+\\d{1,3}\\s?\\(\\d{1,3}\\)\\s?\\d{1,9}$", message = "Phone number is not valid")
    @Schema(description = "New phone number")
    private String phone;
}