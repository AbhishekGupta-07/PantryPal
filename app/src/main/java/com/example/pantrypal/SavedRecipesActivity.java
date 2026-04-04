package com.example.pantrypal;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SavedRecipesActivity extends AppCompatActivity {

    private RecyclerView rv;
    private View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_recipes_activity);

        rv = findViewById(R.id.rvSavedRecipes);
        emptyView = findViewById(R.id.emptyView);

        rv.setLayoutManager(new LinearLayoutManager(this));

        loadData();
    }

    private void loadData() {

        SharedPreferences prefs = getSharedPreferences("recipes", MODE_PRIVATE);

        String type = getIntent().getStringExtra("type");

        Set<String> data;

        if ("fav".equals(type)) {
            data = prefs.getStringSet("fav", null);
        } else {
            data = prefs.getStringSet("saved", null);
        }

        List<String> list = new ArrayList<>();

        if (data != null) {
            list.addAll(data);
        }

        if (list.isEmpty()) {
            rv.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            rv.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);

            // 🔥 FINAL FIX (IMPORTANT)
            rv.setAdapter(new SimpleRecipeAdapter(this, list, type));
        }
    }
}