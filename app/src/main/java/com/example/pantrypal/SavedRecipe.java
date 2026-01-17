package com.example.pantrypal;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "saved_recipes")
public class SavedRecipe {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;
    public String content;

    public SavedRecipe(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
