package com.example.pantrypal.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ExpiryUtils {

    private static final int EXPIRING_SOON_DAYS = 3;

    // ISO formatter (yyyy-MM-dd)
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // 🔥 MAIN METHOD (Updated)
    public static String getExpiryStatus(String expiryDate) {

        if (expiryDate == null || expiryDate.isEmpty()) {
            return "Safe";
        }

        try {
            LocalDate expiry = LocalDate.parse(expiryDate, formatter);
            LocalDate today = LocalDate.now();

            if (expiry.isBefore(today)) {
                return "Expired";
            } else if (!expiry.isAfter(today.plusDays(EXPIRING_SOON_DAYS))) {
                return "Expiring Soon";
            } else {
                return "Safe";
            }

        } catch (Exception e) {
            return "Safe";
        }
    }

    // ✅ Boolean helpers (used everywhere)
    public static boolean isExpiringSoon(String expiryDate) {
        return "Expiring Soon".equals(getExpiryStatus(expiryDate));
    }

    public static boolean isExpired(String expiryDate) {
        return "Expired".equals(getExpiryStatus(expiryDate));
    }

    // 🔥 NEW (IMPORTANT for next steps)
    public static long daysLeft(String expiryDate) {
        try {
            LocalDate expiry = LocalDate.parse(expiryDate, formatter);
            LocalDate today = LocalDate.now();
            return java.time.temporal.ChronoUnit.DAYS.between(today, expiry);
        } catch (Exception e) {
            return 0;
        }
    }
}