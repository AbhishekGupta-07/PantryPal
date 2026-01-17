package com.example.pantrypal;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(
        entities = {
                PantryItem.class,
                SavedRecipe.class,
                ShoppingItem.class   // ✅ ADD THIS
        },
        version = 3,
        exportSchema = false
)
public abstract class PantryDatabase extends RoomDatabase {

    private static PantryDatabase INSTANCE;

    public abstract PantryDao pantryDao();
    public abstract SavedRecipeDao savedRecipeDao();
    public abstract ShoppingDao shoppingDao(); // ✅ ADD THIS

    public static synchronized PantryDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            PantryDatabase.class,
                            "pantry_db"
                    )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
}
