package com.company.retailer.infrastructure;

import com.company.retailer.domain.TransactionData;

class TransactionMapper {

    private TransactionMapper() {
    }

    static TransactionData toData(Transaction transaction) {
        return TransactionData.rehydrate(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getCreateDate(),
                transaction.getLastUpdateDate(),
                transaction.getRewardPoints()
        );
    }

    static Transaction toEntity(TransactionData transaction) {
        return new Transaction(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getCreateDate(),
                transaction.getLastUpdateDate(),
                transaction.getRewardPoints(),
                null
        );
    }
}
