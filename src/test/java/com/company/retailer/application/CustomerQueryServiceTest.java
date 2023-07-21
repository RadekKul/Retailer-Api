package com.company.retailer.application;

import com.company.retailer.domain.CustomerDao;
import com.company.retailer.domain.CustomerData;
import com.company.retailer.domain.TransactionData;
import com.company.retailer.utils.CustomerDataBuilder;
import com.company.retailer.webadapter.CustomerResponse;
import com.company.retailer.webadapter.CustomerRewardsResponse;
import com.company.retailer.webadapter.CustomerTransactionResponse;
import com.company.retailer.webadapter.CustomerTransactionsResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerQueryServiceTest {
    @Mock
    private CustomerDao customerDao;

    @InjectMocks
    private CustomerQueryService customerQueryService;

    @Test
    void shouldReturnCustomerData() {
        CustomerData customerData = CustomerDataBuilder.create(Collections.emptyList());

        when(customerDao.getById(customerData.getId())).thenReturn(customerData);
        CustomerResponse customerResponse = customerQueryService.getCustomerById(customerData.getId());

        assertNotNull(customerResponse);
        assertEquals(customerData.getLogin(), customerResponse.login());
        assertEquals(customerData.getEmail(), customerResponse.email());
        assertEquals(customerData.getFirstName(), customerResponse.firstName());
        assertEquals(customerData.getSurname(), customerResponse.surname());

        verify(customerDao).getById(customerData.getId());
        verifyNoMoreInteractions(customerDao);
    }

    @Test
    void shouldCalculateRewardPoints() {
        UUID customerId = UUID.randomUUID();
        TransactionData transaction = TransactionData.create(BigDecimal.valueOf(70));
        CustomerData customerData = CustomerDataBuilder.create(List.of(transaction));

        when(customerDao.getById(customerId)).thenReturn(customerData);

        CustomerRewardsResponse rewardsResponse = customerQueryService.calculateRewards(customerId);

        assertNotNull(rewardsResponse);
        assertEquals(20, rewardsResponse.lastMonthRewardPoints());
        assertEquals(20, rewardsResponse.lastThreeMonthsRewardPoints());

        verify(customerDao).getById(customerId);
        verifyNoMoreInteractions(customerDao);
    }

    @Test
    void shouldReturnCustomerTransactions() {
        UUID customerId = UUID.randomUUID();
        TransactionData transaction = TransactionData.create(BigDecimal.valueOf(70));
        CustomerData customerData = CustomerDataBuilder.create(List.of(transaction));

        when(customerDao.getById(customerId)).thenReturn(customerData);

        CustomerTransactionsResponse transactionsResponse = customerQueryService.getCustomerTransactions(customerId);

        assertNotNull(transactionsResponse);
        assertEquals(1, transactionsResponse.customerTransactions().size());
        CustomerTransactionResponse customerTransactionResponse = transactionsResponse.customerTransactions().get(0);

        assertEquals(transaction.getId(), customerTransactionResponse.id());
        assertEquals(transaction.getAmount(), customerTransactionResponse.amount());
        assertEquals(transaction.getRewardPoints(), customerTransactionResponse.rewardPoints());
        assertEquals(transaction.getCreateDate(), customerTransactionResponse.createDate());
        assertEquals(transaction.getLastUpdateDate(), customerTransactionResponse.lastUpdateDate());

        verify(customerDao).getById(customerId);
        verifyNoMoreInteractions(customerDao);
    }
}