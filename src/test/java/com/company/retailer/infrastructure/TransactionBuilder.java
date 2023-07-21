package com.company.retailer.infrastructure;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

class TransactionBuilder {

    static Transaction create(BigDecimal amount, long rewardPoints, Customer customer) {
        return new Transaction(UUID.randomUUID(), amount, LocalDateTime.now(), LocalDateTime.now(), rewardPoints, customer);
    }
}
