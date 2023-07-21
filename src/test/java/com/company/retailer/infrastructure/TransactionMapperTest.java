package com.company.retailer.infrastructure;

import com.company.retailer.domain.TransactionData;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class TransactionMapperTest {

    @Test
    void shouldMapToTransactionData() {
        Transaction transaction = TransactionBuilder.create(BigDecimal.valueOf(80), 30, null);
        TransactionData transactionData = TransactionMapper.toData(transaction);

        assertNotNull(transactionData);
        assertEquals(transaction.getId(), transactionData.getId());
        assertEquals(transaction.getAmount(), transactionData.getAmount());
        assertEquals(transaction.getCreateDate(), transactionData.getCreateDate());
        assertEquals(transaction.getLastUpdateDate(), transactionData.getLastUpdateDate());
        assertEquals(transaction.getRewardPoints(), transactionData.getRewardPoints());
    }

    @Test
    void shouldMapToTransactionEntity() {
        TransactionData transactionData = TransactionData.create(BigDecimal.valueOf(80));

        Transaction transaction = TransactionMapper.toEntity(transactionData);

        assertNotNull(transaction);
        assertEquals(transactionData.getId(), transaction.getId());
        assertEquals(transactionData.getAmount(), transaction.getAmount());
        assertEquals(transactionData.getCreateDate(), transaction.getCreateDate());
        assertEquals(transactionData.getLastUpdateDate(), transaction.getLastUpdateDate());
        assertEquals(transactionData.getRewardPoints(), transaction.getRewardPoints());
        assertNull(transaction.getCustomer());
    }
}