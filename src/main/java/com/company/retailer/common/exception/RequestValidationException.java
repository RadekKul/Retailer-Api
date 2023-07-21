package com.company.retailer.common.exception;

import lombok.Getter;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
public class RequestValidationException extends RuntimeException {
    private final List<FieldError> errors;

    public RequestValidationException(List<FieldError> errors) {
        this.errors = errors;
    }
}
