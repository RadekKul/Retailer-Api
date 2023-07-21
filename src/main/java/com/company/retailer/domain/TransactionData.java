package com.company.retailer.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@ToString
public class TransactionData {

    private final UUID id;
    private BigDecimal amount;
    private final LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private long rewardPoints;

    public static TransactionData create(BigDecimal amount) {
        LocalDateTime now = LocalDateTime.now();
        return new TransactionData(
                UUID.randomUUID(),
                amount,
                now,
                now,
                RewardCalculator.calculateRewardPoints(amount));
    }

    public static TransactionData rehydrate(UUID id,
                                            BigDecimal amount,
                                            LocalDateTime createDate,
                                            LocalDateTime lastUpdateDate,
                                            long rewardPoints) {
        return new TransactionData(id, amount, createDate, lastUpdateDate, rewardPoints);
    }

    void update(BigDecimal amount) {
        this.amount = amount;
        this.lastUpdateDate = LocalDateTime.now();
        this.rewardPoints = RewardCalculator.calculateRewardPoints(amount);
    }
}
