package com.company.retailer.webadapter;

public record CustomerResponse(
        String login,
        String email,
        String firstName,
        String surname
) {
}
