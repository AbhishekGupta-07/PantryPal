package com.example.pantrypal;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PantryDao {

    @Insert
    void insertItem(PantryItem item);

    @Update
    void updateItem(PantryItem item);

    @Delete
    void deleteItem(PantryItem item);

    // ✅ Latest items first
    @Query("SELECT * FROM pantry_items ORDER BY id DESC")
    List<PantryItem> getAllItems();

    // ✅ Dashboard counts (fast)
    @Query("SELECT COUNT(*) FROM pantry_items")
    int countTotal();

    // ✅ Optional: expiry column name must match your PantryItem field name
    // If your column is "expiryDate" then keep it same, warna change it.
    @Query("SELECT COUNT(*) FROM pantry_items WHERE expiryDate IS NOT NULL AND expiryDate != ''")
    int countHasExpiry();
}
