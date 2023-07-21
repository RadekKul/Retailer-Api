package com.company.retailer.webadapter;

import com.company.retailer.common.exception.RequestValidationException;
import org.springframework.validation.BindingResult;

class RequestErrorValidator {

    private RequestErrorValidator() {
    }

    static void validateRequestErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new RequestValidationException(bindingResult.getFieldErrors());
        }
    }
}
