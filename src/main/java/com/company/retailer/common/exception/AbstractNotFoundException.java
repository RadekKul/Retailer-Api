package com.company.retailer.common.exception;

public abstract class AbstractNotFoundException extends RuntimeException {
    protected AbstractNotFoundException(String message) {
        super(message);
    }
}
