package com.example.pantrypal;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "shopping_items")
public class ShoppingItem {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String itemName;
    public boolean isPurchased;

    public ShoppingItem(String itemName) {
        this.itemName = itemName;
        this.isPurchased = false;
    }
}
