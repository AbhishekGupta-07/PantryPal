package com.example.pantrypal;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;
import androidx.room.ColumnInfo;

@Entity(tableName = "pantry_items")
public class PantryItem {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    // ✅ UPDATED: quantity now String (supports "4 kg", "500 ml")
    private String quantity;

    // ✅ Future-ready: multiple expiry support (comma separated)
    private String expiryDate;

    @ColumnInfo(name = "price")
    private double price;

    // ✅ Required empty constructor (Room)
    public PantryItem() {
    }

    // ✅ Main constructor (FINAL)
    @Ignore
    public PantryItem(String name, String quantity, String expiryDate, double price) {
        this.name = name;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.price = price;
    }

    // ===== GETTERS =====

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public double getPrice() {
        return price;
    }

    // ===== SETTERS =====

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}