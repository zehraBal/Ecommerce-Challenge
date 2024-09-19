package com.workintech.ecommerce.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@ControllerAdvice
public class ApiGlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleApiException(ApiException exception) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(exception.getMessage(), exception.getHttpStatus().value(), Timestamp.valueOf(LocalDateTime.now()));
        return new ResponseEntity<>(exceptionResponse, exception.getHttpStatus());

    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleApiException(Exception exception) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), Timestamp.valueOf(LocalDateTime.now()));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleIllegalArgument(IllegalArgumentException exception) {
        ExceptionResponse response = new ExceptionResponse(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                Timestamp.valueOf(LocalDateTime.now())
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleDatabaseException(DataAccessException exception) {
        ExceptionResponse response = new ExceptionResponse(
                "Database error occurred: " + exception.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                Timestamp.valueOf(LocalDateTime.now())
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}