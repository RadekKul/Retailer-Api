package com.company.retailer.domain.exception;

import com.company.retailer.common.exception.AbstractBadRequestException;

public class CustomerAlreadyExistsException extends AbstractBadRequestException {
    public CustomerAlreadyExistsException(String login, String email) {
        super(String.format("Customer with given login %s or email %s already exists", login, email));
    }
}
