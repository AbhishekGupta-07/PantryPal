package com.example.pantrypal.ui.recipes;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pantrypal.R;
import com.example.pantrypal.data.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipesFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private EditText etInput;
    private Button btnGenerate;

    private RecipeAdapter adapter;
    private final List<Recipe> recipeList = new ArrayList<>();

    public RecipesFragment() {
        super(R.layout.fragment_recipes);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        recyclerView = view.findViewById(R.id.recyclerRecipes);
        progressBar = view.findViewById(R.id.progressBar);
        etInput = view.findViewById(R.id.etUserInput);
        btnGenerate = view.findViewById(R.id.btnGetRecipe);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecipeAdapter(recipeList);
        recyclerView.setAdapter(adapter);

        btnGenerate.setOnClickListener(v -> fetchRecipes());
    }

    private void fetchRecipes() {

        String input = etInput.getText().toString().toLowerCase().trim();

        if (input.isEmpty()) {
            etInput.setError("Enter something");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        new Handler().postDelayed(() -> {

            recipeList.clear();

            // 🔥 SMART DEMO LOGIC
            if (input.contains("easy") || input.contains("quick")) {

                addRecipe("Bread Sandwich 🥪", "Quick veg sandwich");
                addRecipe("Maggi 🍜", "2 min noodles");
                addRecipe("Omelette 🍳", "Protein breakfast");
                addRecipe("Poha 🥣", "Light healthy meal");
                addRecipe("Upma 🍛", "South Indian dish");
                addRecipe("Fruit Salad 🍎", "Healthy mix");
                addRecipe("Milkshake 🥤", "Sweet drink");
                addRecipe("Toast Butter 🍞", "Simple snack");

            } else if (input.contains("tomato")) {

                addRecipe("Tomato Soup 🍅", "Hot & tasty soup");
                addRecipe("Tomato Pasta 🍝", "Italian style");
                addRecipe("Tomato Sandwich 🥪", "Easy snack");
                addRecipe("Tomato Salad 🥗", "Healthy dish");
                addRecipe("Tomato Rice 🍛", "Spicy rice");
                addRecipe("Tomato Curry 🍲", "Indian gravy");
                addRecipe("Tomato Chutney 🌶", "Side dish");
                addRecipe("Stuffed Tomato 🍅", "Special recipe");

            } else {

                // DEFAULT
                addRecipe("Fried Rice 🍚", "Simple rice dish");
                addRecipe("Paneer Curry 🧀", "Delicious curry");
                addRecipe("Veg Noodles 🍜", "Street style");
                addRecipe("Dal Rice 🍛", "Comfort food");
                addRecipe("Paratha 🫓", "Indian bread");
                addRecipe("Burger 🍔", "Fast food");
                addRecipe("Pizza 🍕", "Cheesy delight");
                addRecipe("Pasta 🍝", "Creamy pasta");
            }

            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);

        }, 1000);
    }

    private void addRecipe(String name, String desc) {
        Recipe r = new Recipe();
        r.setName(name);
        r.setDescription(desc);
        recipeList.add(r);
    }
}