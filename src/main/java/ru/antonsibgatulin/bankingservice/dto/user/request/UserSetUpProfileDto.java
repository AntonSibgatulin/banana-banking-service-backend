package ru.antonsibgatulin.bankingservice.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "User Set Up Profile DTO")
public class UserSetUpProfileDto {

    @NotNull
    @NotBlank(message = "Firstname is required")
    @Size(min = 2, max = 20, message = "Firstname length must be between 2 and 20 characters")
    @Schema(description = "First name of the user")
    private String firstname;

    @NotNull
    @NotBlank(message = "Lastname is required")
    @Size(min = 2, max = 20, message = "Lastname length must be between 2 and 20 characters")
    @Schema(description = "Last name of the user")
    private String lastname;

    @NotNull
    @Size(max = 20, message = "Secondname length must be up to 20 characters")
    @Schema(description = "Second name of the user")
    private String secondname;


    @NotNull(message = "Birthday is required")
    @Past(message = "Birthday must be in the past")
    @Schema(description = "Birth day of the user")
    private LocalDate birthDay;


    @NotNull(message = "Birthday is required")
    @AssertTrue(message = "User must be at least 16 years old")
    @Schema(description = "Flag indicating if the user is an adult (at least 16 years old)")
    private Boolean isAdult() {
        if (birthDay == null) return null;
        return birthDay.plusYears(16).isBefore(LocalDate.now());
    }

}