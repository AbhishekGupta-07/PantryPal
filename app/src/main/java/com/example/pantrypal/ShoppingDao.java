package com.example.pantrypal;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ShoppingDao {

    @Insert
    void insert(ShoppingItem item);

    @Update
    void update(ShoppingItem item);

    @Delete
    void delete(ShoppingItem item);

    // ✅ Normal order (latest first)
    @Query("SELECT * FROM shopping_items ORDER BY id DESC")
    List<ShoppingItem> getAllItems();

    // ✅ AUTO SORT: Unchecked first, Purchased bottom
    @Query("SELECT * FROM shopping_items ORDER BY isPurchased ASC, id DESC")
    List<ShoppingItem> getAllItemsSorted();

    // 🔥 NEW: Delete all items
    @Query("DELETE FROM shopping_items")
    void deleteAll();

    // 🔥 NEW: Only pending (unchecked)
    @Query("SELECT * FROM shopping_items WHERE isPurchased = 0 ORDER BY id DESC")
    List<ShoppingItem> getPendingItems();

    // 🔥 NEW: Only completed (checked)
    @Query("SELECT * FROM shopping_items WHERE isPurchased = 1 ORDER BY id DESC")
    List<ShoppingItem> getCompletedItems();
}