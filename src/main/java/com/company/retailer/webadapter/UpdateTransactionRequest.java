package com.company.retailer.webadapter;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpdateTransactionRequest(
        @Parameter(description = "Transaction amount. Must be positive")
        @NotNull(message = "Transaction amount must not be null")
        @Positive(message = "Transaction amount must be positive")
        BigDecimal amount
) {
}
