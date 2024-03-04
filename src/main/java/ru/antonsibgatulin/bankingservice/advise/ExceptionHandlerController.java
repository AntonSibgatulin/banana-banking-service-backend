package ru.antonsibgatulin.bankingservice.advise;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.antonsibgatulin.bankingservice.dto.except.ApiError;
import ru.antonsibgatulin.bankingservice.except.AuthenticationException;
import ru.antonsibgatulin.bankingservice.except.BadRequestException;
import ru.antonsibgatulin.bankingservice.except.NotFoundException;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<ApiError> badRequest(BadRequestException badRequestException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(badRequestException.getMessage(), badRequestException.getCode()));
    }


    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<ApiError> authenticationFail(AuthenticationException authenticationException) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiError(authenticationException.getMessage(), authenticationException.getCode()));
    }



    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ApiError> notFound(NotFoundException notFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiError(notFoundException.getMessage(), notFoundException.getCode()));
    }


}
