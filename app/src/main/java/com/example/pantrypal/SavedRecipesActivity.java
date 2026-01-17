package com.example.pantrypal;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SavedRecipesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_recipes_activity);

        RecyclerView rv = findViewById(R.id.rvSavedRecipes);
        View emptyView = findViewById(R.id.emptyView);

        rv.setLayoutManager(new LinearLayoutManager(this));

        List<SavedRecipe> savedRecipes =
                PantryDatabase.getInstance(this)
                        .savedRecipeDao()
                        .getAllSavedRecipes();

        if (savedRecipes == null || savedRecipes.isEmpty()) {
            // ✅ show empty state
            rv.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            // ✅ show list
            rv.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            rv.setAdapter(new SavedRecipeAdapter(this, savedRecipes));
        }
    }
}
