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

    @Query("SELECT * FROM pantry_items")
    List<PantryItem> getAllItems();
}
