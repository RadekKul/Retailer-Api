package com.company.retailer.application;

import com.company.retailer.domain.CustomerDao;
import com.company.retailer.domain.CustomerData;
import com.company.retailer.domain.CustomerDataMapper;
import com.company.retailer.domain.TransactionData;
import com.company.retailer.domain.exception.CustomerAlreadyExistsException;
import com.company.retailer.webadapter.CreateCustomerRequest;
import com.company.retailer.webadapter.CreateTransactionRequest;
import com.company.retailer.webadapter.UpdateTransactionRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

    private final CustomerDao customerDao;

    public UUID createCustomer(CreateCustomerRequest createCustomerRequest) {
        log.debug("Trying to create customer with request {}", createCustomerRequest);
        final CustomerData customerData = CustomerDataMapper.toData(createCustomerRequest);
        /*
        There is also pattern (better) to check first on database if users with parameters from requests
        that should be uniqe, are already on DB, but due to requirements and time I did it this simple way
         */
        try {
            customerDao.save(customerData);
        } catch (DataIntegrityViolationException ex) {
            throw new CustomerAlreadyExistsException(createCustomerRequest.login(), createCustomerRequest.email());
        }
        log.debug("Created customer with request {}\nCustomer id: {}", createCustomerRequest, customerData.getId());
        return customerData.getId();
    }

    @Transactional
    public UUID createTransaction(CreateTransactionRequest createTransactionRequest, UUID customerId) {
        log.debug("Trying to create transaction for customer {} with request {}", customerId, createTransactionRequest);

        CustomerData customerData = customerDao.getById(customerId);

        TransactionData transactionData = TransactionData.create(createTransactionRequest.amount());
        log.debug("Created transaction for customer with id {} with request {}.\nTransaction data: {}", customerId, createTransactionRequest, transactionData);
        customerData.addTransaction(transactionData);

        customerDao.save(customerData);
        log.debug("Saved transaction for customer {} with request {}.\nTransaction id: {}", customerId, createTransactionRequest, transactionData.getId());
        return transactionData.getId();
    }

    @Transactional
    public void updateTransaction(UUID customerId, UUID transactionId, UpdateTransactionRequest updateTransactionRequest) {
        log.debug("Trying to update transaction {} for customer {} with request {}", customerId, transactionId, updateTransactionRequest);
        CustomerData customerData = customerDao.getById(customerId);

        customerData.updateTransaction(transactionId, updateTransactionRequest);

        customerDao.save(customerData);
        log.debug("Updated transaction {} for customer {} with request {}", customerId, transactionId, updateTransactionRequest);
    }
}
