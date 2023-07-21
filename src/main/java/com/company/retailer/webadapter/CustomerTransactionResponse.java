package com.company.retailer.webadapter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CustomerTransactionResponse(
        UUID id,
        BigDecimal amount,
        LocalDateTime createDate,
        LocalDateTime lastUpdateDate,
        long rewardPoints
) {
}
