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
        version = 7,
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
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // 🔥 OPTIONAL (GOOD PRACTICE)
    public static void destroyInstance() {
        INSTANCE = null;
    }
}