package com.company.retailer.utils;

import com.company.retailer.webadapter.CreateCustomerRequest;

import static com.company.retailer.utils.RandomStringGenerator.generateRandomEmail;
import static com.company.retailer.utils.RandomStringGenerator.generateRandomString;

public class CreateCustomerRequestBuilder {

    public static CreateCustomerRequest create() {
        return new CreateCustomerRequest(generateRandomString(), generateRandomEmail(), generateRandomString(), generateRandomString());
    }
}
