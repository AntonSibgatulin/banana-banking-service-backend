package ru.antonsibgatulin.bankingservice.except;

import lombok.Data;

@Data
public class BadRequestException extends RuntimeException{
    private String message;
    private Integer code = 404;

    public BadRequestException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public BadRequestException(String message) {
        this.message = message;
    }
}
