package com.company.retailer.domain;

import java.util.UUID;

public interface CustomerDao {
    void save(CustomerData customerData);

    CustomerData getById(UUID customerId);
}
