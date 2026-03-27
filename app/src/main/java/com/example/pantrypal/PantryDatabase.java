package com.example.pantrypal;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(
        entities = {
                PantryItem.class,
                SavedRecipe.class,
                ShoppingItem.class
        },
        version = 7, // 🔥 IMPORTANT: updated
        exportSchema = false
)
public abstract class PantryDatabase extends RoomDatabase {

    private static volatile PantryDatabase INSTANCE;

    // 🔹 DAO methods
    public abstract PantryDao pantryDao();
    public abstract SavedRecipeDao savedRecipeDao();
    public abstract ShoppingDao shoppingDao();

    // 🔹 Singleton instance
    public static PantryDatabase getInstance(Context context) {

        if (INSTANCE == null) {
            synchronized (PantryDatabase.class) {

                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    PantryDatabase.class,
                                    "pantry_db"
                            )
                            .fallbackToDestructiveMigration() // 🔥 auto reset DB
                            .build(); // ❌ removed allowMainThreadQueries (better)
                }
            }
        }
        return INSTANCE;
    }
}