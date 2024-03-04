package ru.antonsibgatulin.bankingservice.except;

import lombok.Data;

@Data
public class AuthenticationException extends RuntimeException {
    private String message;
    private Integer code = 404;

    public AuthenticationException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public AuthenticationException(String message) {
        this.message = message;
    }
}
