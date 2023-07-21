package com.company.retailer.domain;

import com.company.retailer.domain.exception.TransactionNotFoundException;
import com.company.retailer.webadapter.UpdateTransactionRequest;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
public class CustomerData {

    UUID id;
    String login;
    String email;
    String firstName;
    String surname;
    List<TransactionData> transactions;

    public void addTransaction(TransactionData transactionData) {
        transactions.add(transactionData);
    }

    public void updateTransaction(UUID transactionId, UpdateTransactionRequest transactionRequest) {
        TransactionData transactionToUpdate = findTransaction(transactionId);
        transactionToUpdate.update(transactionRequest.amount());
    }

    private TransactionData findTransaction(UUID transactionId) {
        return transactions.stream()
                .filter(t -> t.getId().equals(transactionId))
                .findAny()
                .orElseThrow(() -> new TransactionNotFoundException(transactionId));
    }

    public long calculateLastMonthRewardPoints() {
        return RewardCalculator.calculatePointsForLastMonth(this.transactions);
    }

    public long calculateLastThreeMonthsRewardPoints() {
        return RewardCalculator.calculatePointsForLastThreeMonths(this.transactions);
    }
}
