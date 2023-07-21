package com.company.retailer.infrastructure;

import java.util.List;
import java.util.UUID;

import static com.company.retailer.utils.RandomStringGenerator.generateRandomEmail;
import static com.company.retailer.utils.RandomStringGenerator.generateRandomString;

class CustomerBuilder {
    public static Customer create(List<Transaction> transactions) {
        return new Customer(UUID.randomUUID(), generateRandomString(), generateRandomEmail(), generateRandomString(), generateRandomString(), transactions);
    }
}
