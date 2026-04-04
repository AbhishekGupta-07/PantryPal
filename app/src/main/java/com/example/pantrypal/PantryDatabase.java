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
        version = 8, // 🔥 update whenever schema changes
        exportSchema = false
)
public abstract class PantryDatabase extends RoomDatabase {

    private static volatile PantryDatabase INSTANCE;

    // 🔹 DAO access
    public abstract PantryDao pantryDao();
    public abstract SavedRecipeDao savedRecipeDao();
    public abstract ShoppingDao shoppingDao();

    // 🔥 Singleton instance
    public static PantryDatabase getInstance(Context context) {

        if (INSTANCE == null) {
            synchronized (PantryDatabase.class) {

                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    PantryDatabase.class,
                                    "pantry_db"
                            )
                            .fallbackToDestructiveMigration() // 🔥 avoids crash on schema change
                            .allowMainThreadQueries() // ⚠️ TEMP (safe for now, remove later)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // 🔹 Optional cleanup
    public static void destroyInstance() {
        if (INSTANCE != null) {
            INSTANCE.close();
            INSTANCE = null;
        }
    }
}