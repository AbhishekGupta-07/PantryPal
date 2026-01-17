package com.example.pantrypal.ai;

import java.util.ArrayList;
import java.util.List;

public class RecipeAIBrain {

    /**
     * Main AI method
     * @param mood user mood (happy, sad, tired, etc.)
     * @param season current season (summer, winter, monsoon)
     * @param expiringItems pantry items that are expiring soon
     */
    public static String generateRecipe(
            String mood,
            String season,
            List<String> expiringItems
    ) {

        StringBuilder recipe = new StringBuilder();

        // Title
        recipe.append("🍽 Recipe Suggestion\n\n");

        // Mood based logic
        if (mood.equalsIgnoreCase("happy")) {
            recipe.append("Mood: Happy 😊\n");
            recipe.append("Try something special and flavorful.\n\n");
        } else if (mood.equalsIgnoreCase("tired")) {
            recipe.append("Mood: Tired 😴\n");
            recipe.append("Quick and light food is best.\n\n");
        } else if (mood.equalsIgnoreCase("sad")) {
            recipe.append("Mood: Sad 😔\n");
            recipe.append("Comfort food will help.\n\n");
        } else {
            recipe.append("Mood: ").append(mood).append("\n");
            recipe.append("Simple homemade food suggested.\n\n");
        }

        // Season based logic
        recipe.append("Season: ").append(season).append("\n");
        if (season.equalsIgnoreCase("summer")) {
            recipe.append("Prefer light & cooling dishes.\n\n");
        } else if (season.equalsIgnoreCase("winter")) {
            recipe.append("Warm & healthy food recommended.\n\n");
        } else {
            recipe.append("Balanced food recommended.\n\n");
        }

        // Expiry priority
        if (expiringItems != null && !expiringItems.isEmpty()) {
            recipe.append("⚠ Use these expiring items first:\n");
            for (String item : expiringItems) {
                recipe.append("- ").append(item).append("\n");
            }
            recipe.append("\n");
        }

        // Final suggestion
        recipe.append("🍳 Suggested Recipe:\n");
        recipe.append("Vegetable Stir Fry / Simple Dal-Chawal / Omelette\n\n");
        recipe.append("👨‍🍳 Tip: You can adjust spices as per taste.");

        return recipe.toString();
    }

    /**
     * Helper: Dummy data if pantry empty
     */
    public static List<String> getSampleExpiringItems() {
        List<String> items = new ArrayList<>();
        items.add("Tomato");
        items.add("Onion");
        items.add("Milk");
        return items;
    }
}
