package com.example.pantrypal;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SavedRecipeDao {

    // Save recipe
    @Insert
    void insert(SavedRecipe recipe);

    // Get all saved recipes
    @Query("SELECT * FROM saved_recipes ORDER BY id DESC")
    List<SavedRecipe> getAllSavedRecipes();

    // Delete recipe
    @Delete
    void delete(SavedRecipe recipe);
}