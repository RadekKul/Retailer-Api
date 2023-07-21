package com.company.retailer.application;

import com.company.retailer.domain.CustomerDao;
import com.company.retailer.domain.CustomerData;
import com.company.retailer.domain.CustomerRewardsData;
import com.company.retailer.domain.TransactionData;
import com.company.retailer.webadapter.CustomerResponse;
import com.company.retailer.webadapter.CustomerRewardsResponse;
import com.company.retailer.webadapter.CustomerTransactionResponse;
import com.company.retailer.webadapter.CustomerTransactionsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerQueryService {

    private final CustomerDao customerDao;

    public CustomerResponse getCustomerById(UUID customerId) {
        log.debug("Trying to get customer by id {}", customerId);
        CustomerData customerData = customerDao.getById(customerId);
        log.debug("Got with id {} : ", customerId);
        return mapToCustomerResponse(customerData);
    }

    public CustomerRewardsResponse calculateRewards(UUID customerId) {
        log.debug("Trying to calculate rewards for customer with id {}", customerId);
        CustomerData customerData = customerDao.getById(customerId);
        CustomerRewardsData customerRewardsData =
                new CustomerRewardsData(customerData.calculateLastMonthRewardPoints(), customerData.calculateLastThreeMonthsRewardPoints());
        log.debug("Calculated rewards for customer with id {}", customerId);
        return mapToCustomerRewardsResponse(customerRewardsData);
    }

    private static CustomerResponse mapToCustomerResponse(CustomerData customerData) {
        return new CustomerResponse(customerData.getLogin(),
                customerData.getEmail(),
                customerData.getFirstName(),
                customerData.getSurname()
        );
    }

    public CustomerTransactionsResponse getCustomerTransactions(UUID customerId) {
        log.debug("Trying to get customer transaction by customer id {}", customerId);
        CustomerData customerData = customerDao.getById(customerId);
        log.debug("Got transactions for customer with id {}", customerId);
        return mapToCustomerTransactionsResponse(customerData.getTransactions());
    }

    private static CustomerTransactionsResponse mapToCustomerTransactionsResponse(List<TransactionData> transactionsData) {
        return new CustomerTransactionsResponse(
                transactionsData.stream()
                        .map(CustomerQueryService::mapToCustomerTransactionResponse)
                        .toList()
        );
    }

    private static CustomerTransactionResponse mapToCustomerTransactionResponse(TransactionData transactionData) {
        return new CustomerTransactionResponse(
                transactionData.getId(),
                transactionData.getAmount(),
                transactionData.getCreateDate(),
                transactionData.getLastUpdateDate(),
                transactionData.getRewardPoints()
        );
    }

    private static CustomerRewardsResponse mapToCustomerRewardsResponse(CustomerRewardsData customerRewardsData) {
        return new CustomerRewardsResponse(
                customerRewardsData.oneMonthRewardPoints(),
                customerRewardsData.threeMonthsRewardPoints()
        );
    }
}
