package com.project.api.exception;

import org.springframework.http.HttpStatus;

public class InvalidException extends ApiException {
    public InvalidException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}