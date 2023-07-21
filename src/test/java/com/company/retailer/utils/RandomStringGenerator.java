package com.company.retailer.utils;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomStringGenerator {

    public static String generateRandomString() {
        return RandomStringUtils.random(10, "abcdefghijklmnopqrstuvwxyz");
    }

    public static String generateRandomEmail() {
        return RandomStringUtils.random(10, "abcdefghijklmnopqrstuvwxyz") + "@testdomain.com";
    }
}
