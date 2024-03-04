package ru.antonsibgatulin.bankingservice.dto.user.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Phone Number DTO")
public class PhoneNumberDto {
    @Schema(description = "ID of the phone number")
    private Long id;

    @Schema(description = "Phone number")
    private String phone;
}