package com.company.retailer.application;

import com.company.retailer.domain.CustomerDao;
import com.company.retailer.domain.CustomerData;
import com.company.retailer.domain.TransactionData;
import com.company.retailer.domain.exception.CustomerNotFoundException;
import com.company.retailer.domain.exception.TransactionNotFoundException;
import com.company.retailer.utils.CreateCustomerRequestBuilder;
import com.company.retailer.utils.CustomerDataBuilder;
import com.company.retailer.webadapter.CreateCustomerRequest;
import com.company.retailer.webadapter.CreateTransactionRequest;
import com.company.retailer.webadapter.UpdateTransactionRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerDao customerDao;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void shouldCreateCustomer() {
        CreateCustomerRequest request = CreateCustomerRequestBuilder.create();

        doNothing().when(customerDao).save(any(CustomerData.class));

        UUID customerId = customerService.createCustomer(request);

        assertNotNull(customerId);

        verify(customerDao).save(any(CustomerData.class));
        verifyNoMoreInteractions(customerDao);
    }

    @Test
    void shouldThrowErrorInCreateCustomerWhenCustomerWithGivenLoginOrEmailAlreadyExists() {
        CreateCustomerRequest request = CreateCustomerRequestBuilder.create();

        doThrow(CustomerNotFoundException.class).when(customerDao).save(any(CustomerData.class));

        assertThrows(CustomerNotFoundException.class, () -> customerService.createCustomer(request));

        verify(customerDao).save(any(CustomerData.class));
        verifyNoMoreInteractions(customerDao);
    }

    @Test
    void shouldCreateTransaction() {
        CreateTransactionRequest request = new CreateTransactionRequest(BigDecimal.valueOf(70));
        CustomerData customerData = CustomerDataBuilder.create(new ArrayList<>());

        when(customerDao.getById(customerData.getId())).thenReturn(customerData);
        doNothing().when(customerDao).save(any(CustomerData.class));

        UUID transactionId = customerService.createTransaction(request, customerData.getId());

        assertNotNull(transactionId);

        verify(customerDao).getById(customerData.getId());
        verify(customerDao).save(customerData);
        verifyNoMoreInteractions(customerDao);
    }

    @Test
    void shouldUpdateTransaction() {
        UUID customerId = UUID.randomUUID();
        TransactionData currentTransaction = TransactionData.create(BigDecimal.valueOf(50));
        CustomerData customerData = CustomerDataBuilder.create(List.of(currentTransaction));

        UpdateTransactionRequest request = new UpdateTransactionRequest(BigDecimal.valueOf(80));

        when(customerDao.getById(customerId)).thenReturn(customerData);

        customerService.updateTransaction(customerId, currentTransaction.getId(), request);

        verify(customerDao).getById(customerId);
        verify(customerDao).save(customerData);
        verifyNoMoreInteractions(customerDao);
    }

    @Test
    void shouldThrowErrorWhenUpdatingTransactionThatDoesNotExist() {
        UUID customerId = UUID.randomUUID();
        TransactionData currentTransaction = TransactionData.create(BigDecimal.valueOf(50));
        CustomerData customerData = CustomerDataBuilder.create(List.of(currentTransaction));

        UpdateTransactionRequest request = new UpdateTransactionRequest(BigDecimal.valueOf(80));
        UUID notExistingTransactionId = UUID.randomUUID();

        when(customerDao.getById(customerId)).thenReturn(customerData);

        assertThrows(TransactionNotFoundException.class,
                () -> customerService.updateTransaction(customerId, notExistingTransactionId, request));

        verify(customerDao).getById(customerId);
        verifyNoMoreInteractions(customerDao);
    }
}