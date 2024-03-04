package ru.antonsibgatulin.bankingservice.except;

import lombok.Data;

@Data
public class NotFoundException extends RuntimeException {
    private String message;
    private Integer code = 404;

    public NotFoundException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public NotFoundException(String message) {
        this.message = message;
    }
}
