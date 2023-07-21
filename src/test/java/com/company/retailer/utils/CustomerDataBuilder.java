package com.company.retailer.utils;

import com.company.retailer.domain.CustomerData;
import com.company.retailer.domain.TransactionData;

import java.util.List;
import java.util.UUID;

import static com.company.retailer.utils.RandomStringGenerator.generateRandomEmail;
import static com.company.retailer.utils.RandomStringGenerator.generateRandomString;

public class CustomerDataBuilder {

    public static CustomerData create(List<TransactionData> transactions) {
        return new CustomerData(UUID.randomUUID(), generateRandomString(), generateRandomEmail(), generateRandomString(), generateRandomString(), transactions);
    }
}
