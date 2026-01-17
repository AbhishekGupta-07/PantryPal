package com.example.pantrypal.utils;

public class MoodUtils {

    public static String normalizeMood(String moodInput) {

        if (moodInput == null) return "normal";

        String mood = moodInput.toLowerCase();

        if (mood.contains("happy") || mood.contains("excited")) {
            return "happy";
        } else if (mood.contains("tired") || mood.contains("lazy")) {
            return "tired";
        } else if (mood.contains("sad") || mood.contains("low")) {
            return "sad";
        } else if (mood.contains("angry") || mood.contains("stress")) {
            return "stressed";
        } else {
            return "normal";
        }
    }
}
