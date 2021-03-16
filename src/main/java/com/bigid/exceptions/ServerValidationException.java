package com.bigid.exceptions;

import org.springframework.validation.Errors;

public class ServerValidationException extends RuntimeException {
    private Errors errors;

    public ServerValidationException(String message, Errors errors) {
        super(message);
        this.errors = errors;
    }

    public Errors getErrors() { return errors; }
}