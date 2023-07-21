package com.company.retailer.webadapter;

public record CustomerRewardsResponse(
        long lastMonthRewardPoints,
        long lastThreeMonthsRewardPoints
) {
}
