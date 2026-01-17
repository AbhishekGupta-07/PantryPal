package com.example.pantrypal.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExpiryUtils {

    private static final int EXPIRING_SOON_DAYS = 7;

    // Existing method (KEEP THIS)
    public static String getExpiryStatus(String expiryDate) {

        if (expiryDate == null || expiryDate.isEmpty()) {
            return "Safe";
        }

        try {
            SimpleDateFormat sdf =
                    new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            Date expiry = sdf.parse(expiryDate);
            Date today = new Date();

            Calendar cal = Calendar.getInstance();
            cal.setTime(today);
            cal.add(Calendar.DAY_OF_YEAR, EXPIRING_SOON_DAYS);
            Date soonDate = cal.getTime();

            if (expiry.before(today)) {
                return "Expired";
            } else if (!expiry.after(soonDate)) {
                return "Expiring Soon";
            } else {
                return "Safe";
            }

        } catch (Exception e) {
            return "Safe";
        }
    }

    // ✅ NEW METHOD (THIS FIXES YOUR ERROR)
    public static boolean isExpiringSoon(String expiryDate) {
        return "Expiring Soon".equals(getExpiryStatus(expiryDate));
    }

    // ✅ OPTIONAL BUT GOOD
    public static boolean isExpired(String expiryDate) {
        return "Expired".equals(getExpiryStatus(expiryDate));
    }
}
