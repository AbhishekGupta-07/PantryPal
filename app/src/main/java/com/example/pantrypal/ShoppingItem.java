package com.example.pantrypal;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "shopping_items")
public class ShoppingItem {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String itemName;
    private boolean isPurchased;

    // ✅ Required empty constructor for Room
    public ShoppingItem() {
    }

    // ✅ Custom constructor
    public ShoppingItem(String itemName) {
        this.itemName = itemName;
        this.isPurchased = false;
    }

    // ----------------------
    // Getters & Setters
    // ----------------------

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public boolean isPurchased() {
        return isPurchased;
    }

    public void setPurchased(boolean purchased) {
        isPurchased = purchased;
    }
}
