package com.company.retailer.infrastructure;

import com.company.retailer.domain.CustomerData;
import com.company.retailer.domain.TransactionData;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class CustomerMapper {

    private CustomerMapper() {
    }

    static Customer toEntity(CustomerData customerData) {
        Customer customer = new Customer(
                customerData.getId(),
                customerData.getLogin(),
                customerData.getEmail(),
                customerData.getFirstName(),
                customerData.getSurname(),
                Collections.emptyList()
        );

        List<Transaction> transactions = mapTransactions(customerData);
        transactions.forEach(t -> t.setCustomer(customer));
        customer.setTransactions(transactions);

        return customer;
    }

    static CustomerData toData(Customer customer) {
        return new CustomerData(
                customer.getId(),
                customer.getLogin(),
                customer.getEmail(),
                customer.getFirstName(),
                customer.getSurname(),
                mapTransactions(customer.getTransactions())
        );
    }

    private static List<TransactionData> mapTransactions(List<Transaction> transactions) {
        return transactions.stream()
                .map(TransactionMapper::toData)
                .collect(Collectors.toList());
    }

    private static List<Transaction> mapTransactions(CustomerData customerData) {
        return customerData.getTransactions().stream()
                .map(TransactionMapper::toEntity)
                .toList();
    }
}
