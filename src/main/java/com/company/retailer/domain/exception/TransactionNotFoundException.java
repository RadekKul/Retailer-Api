package com.company.retailer.domain.exception;

import com.company.retailer.common.exception.AbstractNotFoundException;

import java.util.UUID;

public class TransactionNotFoundException extends AbstractNotFoundException {
    public TransactionNotFoundException(UUID id) {
        super("Could not find transaction with id: " + id);
    }
}
