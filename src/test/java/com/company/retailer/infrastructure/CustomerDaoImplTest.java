package com.company.retailer.infrastructure;

import com.company.retailer.domain.CustomerData;
import com.company.retailer.domain.exception.CustomerNotFoundException;
import com.company.retailer.utils.CustomerDataBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerDaoImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerDaoImpl customerDao;

    @Test
    void shouldSaveCustomer() {
        CustomerData customerData = CustomerDataBuilder.create(new ArrayList<>());
        Customer customer = CustomerBuilder.create(new ArrayList<>());

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        customerDao.save(customerData);

        verify(customerRepository).save(any(Customer.class));
        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    void shouldGetCustomer() {
        Customer customer = CustomerBuilder.create(new ArrayList<>());

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));

        CustomerData customerDataFromDb = customerDao.getById(customer.getId());

        assertNotNull(customerDataFromDb);
        assertEquals(customer.getId(), customerDataFromDb.getId());
        assertEquals(customer.getLogin(), customerDataFromDb.getLogin());
        assertEquals(customer.getEmail(), customerDataFromDb.getEmail());
        assertEquals(customer.getFirstName(), customerDataFromDb.getFirstName());
        assertEquals(customer.getSurname(), customerDataFromDb.getSurname());
        assertEquals(customer.getTransactions().size(), customerDataFromDb.getTransactions().size());

        verify(customerRepository).findById(customer.getId());
        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    void shouldThrowErrorWhenCustomerDoesNotExist() {
        UUID customerId = UUID.randomUUID();

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerDao.getById(customerId));

        verify(customerRepository).findById(customerId);
        verifyNoMoreInteractions(customerRepository);
    }
}