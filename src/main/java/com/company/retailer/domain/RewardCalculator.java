package com.company.retailer.domain;

import com.google.common.collect.Range;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
class RewardCalculator {

    private RewardCalculator() {
    }

    private static final BigDecimal TRANSACTION_AMOUNT_50_VALUE = BigDecimal.valueOf(50.0);
    private static final BigDecimal TRANSACTION_AMOUNT_100_VALUE = BigDecimal.valueOf(100.0);
    private static final long POINTS_RATE_ABOVE_50_AND_BELOW_100_AMOUNT_SPENT = 1;
    private static final long POINTS_RATE_ABOVE_100_AMOUNT_SPENT = 2;

    static long calculateRewardPoints(@NonNull final BigDecimal transactionAmount) {
        if (isTransactionAboveGivenAmount(transactionAmount, TRANSACTION_AMOUNT_100_VALUE)) {
            return calculatePointsForAmountAbove100(transactionAmount);
        } else if (isTransactionAboveGivenAmount(transactionAmount, TRANSACTION_AMOUNT_50_VALUE)) {
            return calculatePointsForAmountAbove50(transactionAmount);
        }
        return 0;
    }

    private static long calculatePointsForAmountAbove100(BigDecimal transactionAmount) {
        long pointsForTransactionAmountBetween50And100 = TRANSACTION_AMOUNT_100_VALUE.subtract(TRANSACTION_AMOUNT_50_VALUE).longValue();
        long excessAmountAbove100 = countExcessAmountAboveGivenTransactionAmount(transactionAmount, TRANSACTION_AMOUNT_100_VALUE);
        long pointsForTransactionAmountAbove100 = excessAmountAbove100 * POINTS_RATE_ABOVE_100_AMOUNT_SPENT;
        return pointsForTransactionAmountAbove100 + pointsForTransactionAmountBetween50And100;
    }

    private static long calculatePointsForAmountAbove50(BigDecimal transactionAmount) {
        long excessAmountAbove50 = countExcessAmountAboveGivenTransactionAmount(transactionAmount, TRANSACTION_AMOUNT_50_VALUE);
        return excessAmountAbove50 * POINTS_RATE_ABOVE_50_AND_BELOW_100_AMOUNT_SPENT;
    }

    private static boolean isTransactionAboveGivenAmount(BigDecimal transactionAmount, BigDecimal amount) {
        return transactionAmount.compareTo(amount) > 0;
    }

    private static long countExcessAmountAboveGivenTransactionAmount(final BigDecimal transactionAmount, final BigDecimal amountToSubtract) {
        return transactionAmount.subtract(amountToSubtract)
                .setScale(0, RoundingMode.DOWN)
                .longValue();
    }

    /*
    I assume that in this simple example Customer has no a lot of transaction.
    In case that I know it will have a lot of them, this logic should be moved to database
     */
    static long calculatePointsForLastMonth(List<TransactionData> transactions) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastMonthStartDate = now.withDayOfMonth(1).withHour(8).withMinute(0).withSecond(0).withNano(0);
        Range<LocalDateTime> range = Range.closed(lastMonthStartDate, now);
        return sumPointsForGivenDateRange(transactions, range);

    }

    static long calculatePointsForLastThreeMonths(List<TransactionData> transactions) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastThreeMonthsStartDate = now.minusMonths(2).withDayOfMonth(1).withHour(8).withMinute(0).withSecond(0).withNano(0);
        Range<LocalDateTime> range = Range.closed(lastThreeMonthsStartDate, now);
        return sumPointsForGivenDateRange(transactions, range);
    }

    private static long sumPointsForGivenDateRange(List<TransactionData> transactions, Range<LocalDateTime> range) {
        return transactions.stream()
                .filter(transaction -> range.contains(transaction.getLastUpdateDate()))
                .map(TransactionData::getRewardPoints)
                .mapToLong(Long::longValue)
                .sum();
    }

}
