package ru.antonsibgatulin.bankingservice.except;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AlreadyExistException extends RuntimeException {
    private String message;
}
