package ru.antonsibgatulin.bankingservice.advise;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerRestController {
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity handleValidationException(MethodArgumentNotValidException ex)
    {
        System.out.println("++++++++++++++++++++++++++++++++++++++++++");
        return new ResponseEntity(ex.getMessage() , HttpStatus.BAD_REQUEST);
    }
}
