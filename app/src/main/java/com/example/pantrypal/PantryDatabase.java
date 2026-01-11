package com.example.pantrypal;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {PantryItem.class}, version = 1)
public abstract class PantryDatabase extends RoomDatabase {

    private static PantryDatabase INSTANCE;

    public abstract PantryDao pantryDao();

    public static synchronized PantryDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    PantryDatabase.class,
                    "pantry_db"
            ).allowMainThreadQueries().build();
        }
        return INSTANCE;
    }
}
