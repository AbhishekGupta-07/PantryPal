package com.example.pantrypal.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ExpiryUtils {

    private static final int EXPIRING_SOON_DAYS = 3;

    // 🔥 Support BOTH formats
    private static final DateTimeFormatter FORMAT_1 =
            DateTimeFormatter.ofPattern("yyyy-MM-dd"); // DB format

    private static final DateTimeFormatter FORMAT_2 =
            DateTimeFormatter.ofPattern("dd/MM/yyyy"); // User format

    // 🔥 MAIN METHOD
    public static String getExpiryStatus(String expiryDate) {

        if (expiryDate == null || expiryDate.trim().isEmpty()) {
            return "Safe";
        }

        try {
            LocalDate expiry = parseDate(expiryDate);
            LocalDate today = LocalDate.now();

            long days = java.time.temporal.ChronoUnit.DAYS.between(today, expiry);

            if (days < 0) {
                return "Expired";
            } else if (days <= EXPIRING_SOON_DAYS) {
                return "Expiring Soon";
            } else {
                return "Safe";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Safe";
        }
    }

    // 🔥 DATE PARSER (handles both formats)
    private static LocalDate parseDate(String date) {

        try {
            return LocalDate.parse(date, FORMAT_1);
        } catch (DateTimeParseException e) {
            return LocalDate.parse(date, FORMAT_2);
        }
    }

    // ✅ Helpers
    public static boolean isExpiringSoon(String expiryDate) {
        return "Expiring Soon".equals(getExpiryStatus(expiryDate));
    }

    public static boolean isExpired(String expiryDate) {
        return "Expired".equals(getExpiryStatus(expiryDate));
    }

    // 🔥 Days left (useful for UI / AI)
    public static long daysLeft(String expiryDate) {
        try {
            LocalDate expiry = parseDate(expiryDate);
            LocalDate today = LocalDate.now();
            return java.time.temporal.ChronoUnit.DAYS.between(today, expiry);
        } catch (Exception e) {
            return 0;
        }
    }
}