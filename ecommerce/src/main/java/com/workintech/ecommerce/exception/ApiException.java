package com.workintech.ecommerce.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException{
    private HttpStatus  httpStatus;

    public ApiException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
