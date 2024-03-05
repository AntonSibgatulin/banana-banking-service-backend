package ru.antonsibgatulin.bankingservice.advise;

import io.micrometer.common.util.StringUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.antonsibgatulin.bankingservice.dto.except.ApiError;
import ru.antonsibgatulin.bankingservice.except.*;


import org.springframework.context.support.DefaultMessageSourceResolvable;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.MethodArgumentNotValidException;

import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.util.stream.Collectors;

import org.springframework.validation.ObjectError;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
@ResponseBody
@Order(Ordered.HIGHEST_PRECEDENCE) // Set the highest precedence
public class ExceptionHandlerController {

    @Autowired
    private ApplicationContext applicationContext;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<ApiError> handleBadRequestException(BadRequestException badRequestException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(badRequestException.getMessage(), badRequestException.getCode()));
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<ApiError> handleAuthenticationFail(AuthenticationException authenticationException) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiError(authenticationException.getMessage(), authenticationException.getCode()));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ApiError> handleNotFoundException(NotFoundException notFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiError(notFoundException.getMessage(), notFoundException.getCode()));
    }

    /*
        @ExceptionHandler(value = MethodArgumentNotValidException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ResponseEntity handleValidationException(MethodArgumentNotValidException ex)
        {
            List<String> errorMessages = ((MethodArgumentNotValidException)ex)
                    .getBindingResult()
                    .getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return new ResponseEntity(errorMessages.toString(), HttpStatus.BAD_REQUEST);
        }

     */

    /*
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolationException(ConstraintViolationException ex) {
        String errors = ex.getConstraintViolations().stream()
                .map(violation -> violation.getMessage())
                .collect(Collectors.joining(", "));
        return new ResponseEntity<>(new ApiError(errors, 400), HttpStatus.BAD_REQUEST);
    }

     */


    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<ApiError> handleRegistrationException(RegistrationException ex) {
        return new ResponseEntity<>(new ApiError(ex.getMessage(), 409), HttpStatus.BAD_REQUEST);
    }


    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<ApiError> handleAlreadyException(AlreadyExistException ex) {

        return new ResponseEntity<>(new ApiError(ex.getMessage(), 409), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(WebExchangeBindException.class)
    public HttpStatus handleWebExchangeBindException(WebExchangeBindException e) {

        FieldError fieldError = e.getBindingResult().getFieldError();
        if (fieldError != null) {
            throw new WebExchangeBindException(e.getMethodParameter(), e.getBindingResult());
        }
        throw e;
    }


    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ApiError> handleConstraintViolationException(ConstraintViolationException e) {
        String fieldName = e.getMessage();
        String message = getResourceMessage(fieldName + " already exist", "Already Exists");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(message, 400));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ApiError> onMethodArgumentNotValidException(
            MethodArgumentNotValidException e
    ) {
        final List<String> violations = e.getBindingResult().getFieldErrors().stream()

                .map(error -> error.getDefaultMessage())

                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(violations.stream().collect(Collectors.joining(", ")), 400));


    }


    private String getResourceMessage(String key, String defaultMessage) {
        String message = applicationContext.getMessage(key, null, Locale.getDefault());
        if (StringUtils.isNotEmpty(message)) {
            return message;
        }
        return defaultMessage;
    }
}
