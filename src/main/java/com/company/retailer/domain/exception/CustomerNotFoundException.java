package com.company.retailer.domain.exception;

import com.company.retailer.common.exception.AbstractNotFoundException;

import java.util.UUID;

public class CustomerNotFoundException extends AbstractNotFoundException {
    public CustomerNotFoundException(UUID id) {
        super("Could not find customer with id: " + id);
    }
}
