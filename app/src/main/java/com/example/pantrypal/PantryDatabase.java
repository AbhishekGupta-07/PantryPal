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
        version = 8, // 🔥 increment version (important after model change)
        exportSchema = false
)
public abstract class PantryDatabase extends RoomDatabase {

    private static volatile PantryDatabase INSTANCE;

    public abstract PantryDao pantryDao();
    public abstract SavedRecipeDao savedRecipeDao();
    public abstract ShoppingDao shoppingDao();

    public static PantryDatabase getInstance(Context context) {

        if (INSTANCE == null) {
            synchronized (PantryDatabase.class) {

                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    PantryDatabase.class,
                                    "pantry_db"
                            )
                            .fallbackToDestructiveMigration() // 🔥 avoids crash
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // Optional cleanup
    public static void destroyInstance() {
        INSTANCE = null;
    }
}