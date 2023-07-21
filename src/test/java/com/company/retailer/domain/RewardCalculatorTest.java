package com.company.retailer.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RewardCalculatorTest {

    @ParameterizedTest
    @MethodSource("testCasesParametersForRewardPointsCalculation")
    void shouldReturnExpectedRewardPointsForGivenTransactionAmount(BigDecimal transactionAmount, long expectedPoints) {
        long actualPoints = RewardCalculator.calculateRewardPoints(transactionAmount);

        assertEquals(expectedPoints, actualPoints);
    }

    private static Stream<Arguments> testCasesParametersForRewardPointsCalculation() {
        return Stream.of(
                Arguments.of(BigDecimal.ZERO, 0),
                Arguments.of(BigDecimal.valueOf(45.0), 0),
                Arguments.of(BigDecimal.valueOf(50.0), 0),
                Arguments.of(BigDecimal.valueOf(75.0), 25),
                Arguments.of(BigDecimal.valueOf(100.0), 50),
                Arguments.of(BigDecimal.valueOf(120.0), 90),
                Arguments.of(BigDecimal.valueOf(30.23), 0),
                Arguments.of(BigDecimal.valueOf(55.999999), 5),
                Arguments.of(BigDecimal.valueOf(123.99), 96),
                Arguments.of(BigDecimal.valueOf(123.50), 96)
        );
    }

    @Test
    void shouldReturnCorrectPointsSumForLastMonth() {
        TransactionData transactionDataWithLastUpdateDateOlderThanOneMonth = TransactionData.create(BigDecimal.valueOf(100));
        setFieldValue(transactionDataWithLastUpdateDateOlderThanOneMonth, "lastUpdateDate", LocalDateTime.now().minusMonths(1).withDayOfMonth(28));
        setFieldValue(transactionDataWithLastUpdateDateOlderThanOneMonth, "createDate", LocalDateTime.now().minusMonths(1).withDayOfMonth(28));
        TransactionData transactionDataWithLastUpdateDateExactlyAtLastMonthRange = TransactionData.create(BigDecimal.valueOf(80));
        setFieldValue(transactionDataWithLastUpdateDateExactlyAtLastMonthRange, "lastUpdateDate", LocalDateTime.now().withDayOfMonth(1).withMinute(0).withSecond(0).withNano(0));
        setFieldValue(transactionDataWithLastUpdateDateExactlyAtLastMonthRange, "createDate", LocalDateTime.now().withDayOfMonth(1).withMinute(0).withSecond(0).withNano(0));

        List<TransactionData> transactions = Arrays.asList(
                TransactionData.create(BigDecimal.valueOf(70)),
                transactionDataWithLastUpdateDateExactlyAtLastMonthRange,
                transactionDataWithLastUpdateDateOlderThanOneMonth
        );

        long sum = RewardCalculator.calculatePointsForLastMonth(transactions);
        assertEquals(20 + 30, sum);
    }

    @Test
    void shouldReturnCorrectPointsSumForLastThreeMonth() {
        TransactionData transactionDataWithLastUpdateDateOlderThanFreeMonths = TransactionData.create(BigDecimal.valueOf(100));
        setFieldValue(transactionDataWithLastUpdateDateOlderThanFreeMonths, "lastUpdateDate", LocalDateTime.now().minusMonths(3).withDayOfMonth(28));
        setFieldValue(transactionDataWithLastUpdateDateOlderThanFreeMonths, "createDate", LocalDateTime.now().minusMonths(3).withDayOfMonth(28));
        TransactionData transactionDataWithLastUpdateDateExactlyAtLastThreeMonthsRange = TransactionData.create(BigDecimal.valueOf(80));
        setFieldValue(transactionDataWithLastUpdateDateExactlyAtLastThreeMonthsRange, "lastUpdateDate", LocalDateTime.now().minusMonths(2).withDayOfMonth(1).withMinute(0).withSecond(0).withNano(0));
        setFieldValue(transactionDataWithLastUpdateDateExactlyAtLastThreeMonthsRange, "createDate", LocalDateTime.now().minusMonths(2).withDayOfMonth(1).withMinute(0).withSecond(0).withNano(0));


        List<TransactionData> transactions = Arrays.asList(
                TransactionData.create(BigDecimal.valueOf(70)),
                transactionDataWithLastUpdateDateOlderThanFreeMonths,
                transactionDataWithLastUpdateDateExactlyAtLastThreeMonthsRange
        );

        long sum = RewardCalculator.calculatePointsForLastThreeMonths(transactions);
        assertEquals(20 + 30, sum);
    }

    /*
    To set values in TransactionData Object that has private AllArgsConstructor, I need to use reflection mechanism
     */
    private static void setFieldValue(TransactionData object, String fieldName, Object value) {
        try {
            Field field = TransactionData.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
