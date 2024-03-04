package ru.antonsibgatulin.bankingservice.dto.user.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Email Address DTO")
public class EmailAddressDto {
    @Schema(description = "ID of the email address")
    private Long id;

    @Schema(description = "Email address")
    private String email;
}
