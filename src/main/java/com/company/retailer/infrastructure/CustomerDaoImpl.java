package com.company.retailer.infrastructure;

import com.company.retailer.domain.CustomerDao;
import com.company.retailer.domain.CustomerData;
import com.company.retailer.domain.exception.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
class CustomerDaoImpl implements CustomerDao {

    private final CustomerRepository customerRepository;

    @Override
    public void save(CustomerData customerData) {
        Customer customer = CustomerMapper.toEntity(customerData);
        customerRepository.save(customer);
    }

    @Override
    public CustomerData getById(UUID customerId) {
        return customerRepository.findById(customerId)
                .map(CustomerMapper::toData)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
    }
}
