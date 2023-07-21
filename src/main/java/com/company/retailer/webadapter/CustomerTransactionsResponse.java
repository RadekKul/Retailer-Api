package com.company.retailer.webadapter;

import java.util.List;

public record CustomerTransactionsResponse(
        List<CustomerTransactionResponse> customerTransactions
) {
}
