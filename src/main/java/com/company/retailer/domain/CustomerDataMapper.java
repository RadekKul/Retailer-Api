package com.company.retailer.domain;

import com.company.retailer.webadapter.CreateCustomerRequest;

import java.util.Collections;
import java.util.UUID;

public class CustomerDataMapper {

    private CustomerDataMapper() {
    }

    public static CustomerData toData(final CreateCustomerRequest createCustomerRequest) {
        return new CustomerData(
                UUID.randomUUID(),
                createCustomerRequest.login(),
                createCustomerRequest.email(),
                createCustomerRequest.firstName(),
                createCustomerRequest.surname(),
                Collections.emptyList()
        );
    }
}
