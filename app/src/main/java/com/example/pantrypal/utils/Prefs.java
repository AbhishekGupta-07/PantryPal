package com.example.pantrypal.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {

    private static final String PREFS_NAME = "pantrypal_prefs";

    private static final String KEY_DARK_MODE = "dark_mode";
    private static final String KEY_NOTIFICATIONS = "notifications";
    private static final String KEY_EXPIRY_DAYS = "expiry_days";

    // Profile
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_AVATAR_URI = "avatar_uri";

    // Login
    private static final String KEY_LOGGED_IN = "logged_in";

    private static SharedPreferences sp(Context c) {
        return c.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    // ----------------- Dark Mode -----------------
    public static boolean isDarkMode(Context c) {
        return sp(c).getBoolean(KEY_DARK_MODE, false);
    }

    public static void setDarkMode(Context c, boolean value) {
        sp(c).edit().putBoolean(KEY_DARK_MODE, value).apply();
    }

    // ----------------- Notifications -----------------
    public static boolean isNotificationsEnabled(Context c) {
        return sp(c).getBoolean(KEY_NOTIFICATIONS, true);
    }

    public static void setNotificationsEnabled(Context c, boolean value) {
        sp(c).edit().putBoolean(KEY_NOTIFICATIONS, value).apply();
    }

    // ----------------- Expiry Reminder Days -----------------
    public static int getExpiryDays(Context c) {
        return sp(c).getInt(KEY_EXPIRY_DAYS, 3);
    }

    public static void setExpiryDays(Context c, int days) {
        sp(c).edit().putInt(KEY_EXPIRY_DAYS, days).apply();
    }

    // ----------------- Profile Name -----------------
    public static String getUserName(Context c, String defaultName) {
        return sp(c).getString(KEY_USER_NAME, defaultName);
    }

    public static void setUserName(Context c, String name) {
        sp(c).edit().putString(KEY_USER_NAME, name).apply();
    }

    // ----------------- Avatar Uri -----------------
    public static String getAvatarUri(Context c) {
        return sp(c).getString(KEY_AVATAR_URI, null);
    }

    public static void setAvatarUri(Context c, String uri) {
        sp(c).edit().putString(KEY_AVATAR_URI, uri).apply();
    }

    // ----------------- Login -----------------
    public static boolean isLoggedIn(Context c) {
        return sp(c).getBoolean(KEY_LOGGED_IN, false);
    }

    public static void setLoggedIn(Context c, boolean value) {
        sp(c).edit().putBoolean(KEY_LOGGED_IN, value).apply();
    }

    // ----------------- Logout Clear -----------------
    public static void clearUser(Context c) {
        sp(c).edit()
                .remove(KEY_USER_NAME)
                .remove(KEY_AVATAR_URI)
                .putBoolean(KEY_LOGGED_IN, false)
                .apply();
    }
}
