package ru.antonsibgatulin.bankingservice.dto.user.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User DTO")
public class UserDto {
    @Schema(description = "ID of the user")
    private Long id;

    @Schema(description = "First name of the user")
    private String firstname;

    @Schema(description = "Last name of the user")
    private String lastname;

    @Schema(description = "Second name of the user")
    private String secondname;

    @Schema(description = "Birth day of the user")
    private LocalDate birthDay;

    @Schema(description = "Login of the user")
    private String login;

    @Schema(description = "List of phone numbers associated with the user")
    private List<PhoneNumberDto> phones;

    @Schema(description = "List of email addresses associated with the user")
    private List<EmailAddressDto> emails;
}