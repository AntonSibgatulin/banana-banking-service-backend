package ru.antonsibgatulin.bankingservice.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Schema(description = "User Registration DTO")
public class UserRegistrationDto {
    @NotBlank(message = "Login is required")
    @Size(min = 3, max = 20, message = "Login length must be between 3 and 20 characters")
    @Schema(description = "User login")
    private String login;


    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 20, message = "Password length must be between 6 and 20 characters")
    @Schema(description = "User password")
    private String password;


    @NotBlank(message = "Email is required")
    @Email(message = "Email is not valid")
    @Schema(description = "User email address")
    private String email;


    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^7\\d{10}$", message = "Phone number is not valid")
    @Schema(description = "User phone number")
    private String phone;

    @DecimalMin(value = "0.0", inclusive = false, message = "Start deposit must be greater than zero")
    @Schema(description = "Start deposit amount. Must be greater than zero")
    private BigDecimal startDeposit;


}