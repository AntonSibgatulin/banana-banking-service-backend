package ru.antonsibgatulin.bankingservice.dto.user.request;

import lombok.Data;

@Data
public class AuthenticationUserDto {
    private String login;
    private String password;
}
