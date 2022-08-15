package com.migrosone.couriermanagement.controller.advice;

import com.migrosone.couriermanagement.controller.response.ErrorResponse;
import com.migrosone.couriermanagement.controller.response.MultipleErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler
    public ResponseEntity<MultipleErrorResponse> handleValidationException(
            MethodArgumentNotValidException exception) {
        MultipleErrorResponse response = new MultipleErrorResponse();

        exception
                .getFieldErrors()
                .forEach(fieldError -> response.getMessages().add(fieldError.getDefaultMessage()));

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception exception) {
        ErrorResponse response = new ErrorResponse();
        response.setMessage(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
