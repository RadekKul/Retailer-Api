package com.company.retailer.common.exception.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
class RequestValidationResponse {

    List<RequestValidationError> errors;

    RequestValidationResponse(List<FieldError> fieldErrors) {
        this.errors = fieldErrors.stream()
                .map(e -> RequestValidationError.of(e.getField(), e.getDefaultMessage()))
                .toList();
    }

    @Getter
    @AllArgsConstructor(staticName = "of")
    private static class RequestValidationError {
        private String fieldName;
        private String errorMessage;
    }
}
