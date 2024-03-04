package ru.antonsibgatulin.bankingservice.dto.user.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailAddressDto {
    private Long id;
    private String email;
}
