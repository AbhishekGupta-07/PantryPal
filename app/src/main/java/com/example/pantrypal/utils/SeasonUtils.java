package com.example.pantrypal.utils;

import java.util.Calendar;

public class SeasonUtils {

    public static String getCurrentSeason() {

        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        // Jan = 1, Dec = 12

        if (month == 12 || month == 1 || month == 2) {
            return "Winter";
        } else if (month >= 3 && month <= 5) {
            return "Summer";
        } else if (month >= 6 && month <= 9) {
            return "Monsoon";
        } else {
            return "Autumn";
        }
    }
}
