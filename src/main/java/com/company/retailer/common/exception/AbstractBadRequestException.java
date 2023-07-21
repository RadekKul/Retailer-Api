package com.company.retailer.common.exception;

public abstract class AbstractBadRequestException extends RuntimeException {
    protected AbstractBadRequestException(String message) {
        super(message);
    }
}
