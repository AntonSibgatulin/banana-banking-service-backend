package ru.antonsibgatulin.bankingservice.except;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccessDeniedForUnAutorizationException extends RuntimeException {
    private String message;

}
