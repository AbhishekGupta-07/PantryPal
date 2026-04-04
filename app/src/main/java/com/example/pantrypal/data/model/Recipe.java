package com.example.pantrypal.data.model;

import java.util.List;

public class Recipe {

    private String name;
    private String description;
    private String cookTime;
    private String difficulty;
    private String emoji;
    private boolean usesExpiringItems;

    private List<String> ingredients;
    private List<String> steps;
    private List<String> expiringIngredients;

    // ❤️ NEW FEATURES
    private boolean isFavorite = false;
    private boolean isSaved = false;

    // 🔹 GETTERS & SETTERS

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCookTime() {
        return cookTime;
    }

    public void setCookTime(String cookTime) {
        this.cookTime = cookTime;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public boolean isUsesExpiringItems() {
        return usesExpiringItems;
    }

    public void setUsesExpiringItems(boolean usesExpiringItems) {
        this.usesExpiringItems = usesExpiringItems;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public List<String> getExpiringIngredients() {
        return expiringIngredients;
    }

    public void setExpiringIngredients(List<String> expiringIngredients) {
        this.expiringIngredients = expiringIngredients;
    }

    // ❤️ FAVORITE
    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    // 💾 SAVE
    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }
}