package com.example.pantrypal;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PantryDao {

    // 🔹 Insert item (replace if conflict)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertItem(PantryItem item);

    // 🔹 Update item
    @Update
    void updateItem(PantryItem item);

    // 🔹 Delete single item
    @Delete
    void deleteItem(PantryItem item);

    // 🔹 Get all items (latest first)
    @Query("SELECT * FROM pantry_items ORDER BY id DESC")
    List<PantryItem> getAllItems();

    // 🔹 Total items count
    @Query("SELECT COUNT(*) FROM pantry_items")
    int countTotal();

    // 🔹 Items having expiry date
    @Query("SELECT COUNT(*) FROM pantry_items WHERE expiryDate IS NOT NULL AND expiryDate != ''")
    int countHasExpiry();

    // 🔴 Clear all pantry data (Profile → Clear Pantry Data)
    @Query("DELETE FROM pantry_items")
    void deleteAll();
}
