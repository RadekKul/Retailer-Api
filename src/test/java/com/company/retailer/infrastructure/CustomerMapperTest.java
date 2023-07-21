package com.company.retailer.infrastructure;

import com.company.retailer.domain.CustomerData;
import com.company.retailer.domain.TransactionData;
import com.company.retailer.utils.CustomerDataBuilder;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CustomerMapperTest {

    @Test
    void shouldMapToCustomerEntity() {
        TransactionData transaction = TransactionData.create(BigDecimal.valueOf(20));
        List<TransactionData> transactions = List.of(transaction);
        CustomerData customerData = CustomerDataBuilder.create(transactions);

        Customer customer = CustomerMapper.toEntity(customerData);

        assertNotNull(customer);
        assertEquals(customerData.getId(), customer.getId());
        assertEquals(customerData.getLogin(), customer.getLogin());
        assertEquals(customerData.getEmail(), customer.getEmail());
        assertEquals(customerData.getFirstName(), customer.getFirstName());
        assertEquals(customerData.getSurname(), customer.getSurname());
        assertEquals(transactions.size(), customer.getTransactions().size());
        assertEquals(transactions.get(0).getId(), customer.getTransactions().get(0).getId());
    }

    @Test
    void shouldMapToCustomerData() {

        Transaction transaction = TransactionBuilder.create(BigDecimal.valueOf(70), 20, null);
        List<Transaction> transactions = List.of(transaction);
        Customer customer = CustomerBuilder.create(transactions);

        CustomerData customerData = CustomerMapper.toData(customer);

        assertNotNull(customerData);
        assertEquals(customer.getId(), customerData.getId());
        assertEquals(customer.getLogin(), customerData.getLogin());
        assertEquals(customer.getEmail(), customerData.getEmail());
        assertEquals(customer.getFirstName(), customerData.getFirstName());
        assertEquals(customer.getSurname(), customerData.getSurname());
        assertEquals(transactions.size(), customerData.getTransactions().size());
        assertEquals(transactions.get(0).getId(), customerData.getTransactions().get(0).getId());
    }
}