package com.example.pantrypal.ui.recipes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.pantrypal.PantryDatabase;
import com.example.pantrypal.R;
import com.example.pantrypal.SavedRecipe;

public class RecipesFragment extends Fragment {

    public RecipesFragment() {
        super(R.layout.activity_recipe_suggestion); // ⚠️ ensure layout correct
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText etMood = view.findViewById(R.id.etMood);
        Button btnGetRecipe = view.findViewById(R.id.btnGetRecipe);
        TextView tvDish = view.findViewById(R.id.tvDishName);
        TextView tvResult = view.findViewById(R.id.tvRecipeResult);
        Button btnShare = view.findViewById(R.id.btnShareRecipe);
        Button btnSave = view.findViewById(R.id.btnSaveRecipe);
        Button btnVideo = view.findViewById(R.id.btnWatchVideo);
        Button btnSaved = view.findViewById(R.id.btnViewSavedRecipes);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);

        btnGetRecipe.setOnClickListener(v -> {

            String mood = etMood.getText().toString().trim();

            if (mood.isEmpty()) {
                etMood.setError("Enter mood");
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            new Handler().postDelayed(() -> {

                if (!isAdded()) return;

                progressBar.setVisibility(View.GONE);

                String dish = "Masala Maggi 🍜";
                String recipe =
                        "1. Boil water\n" +
                                "2. Add maggi\n" +
                                "3. Add masala\n" +
                                "4. Cook for 2 mins\n" +
                                "5. Serve hot 😋";

                tvDish.setText(dish);
                tvResult.setText(recipe);

                tvDish.setVisibility(View.VISIBLE);
                tvResult.setVisibility(View.VISIBLE);
                btnShare.setVisibility(View.VISIBLE);
                btnSave.setVisibility(View.VISIBLE);
                btnVideo.setVisibility(View.VISIBLE);
                btnSaved.setVisibility(View.VISIBLE);

            }, 1200);
        });

        // 📤 SHARE
        btnShare.setOnClickListener(v -> {

            String dish = tvDish.getText().toString();
            String recipe = tvResult.getText().toString();

            if (dish.isEmpty()) {
                Toast.makeText(getContext(), "Generate recipe first", Toast.LENGTH_SHORT).show();
                return;
            }

            String text = dish + "\n\n" + recipe;

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, text);

            startActivity(Intent.createChooser(intent, "Share Recipe"));
        });

        // ❤️ SAVE
        btnSave.setOnClickListener(v -> {

            String dish = tvDish.getText().toString();
            String recipe = tvResult.getText().toString();

            if (dish.isEmpty()) {
                Toast.makeText(getContext(), "Generate recipe first", Toast.LENGTH_SHORT).show();
                return;
            }

            SavedRecipe savedRecipe = new SavedRecipe(dish, recipe);

            new Thread(() -> {

                if (getContext() == null) return;

                PantryDatabase.getInstance(getContext())
                        .savedRecipeDao()
                        .insert(savedRecipe);

                if (!isAdded()) return;

                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Saved Successfully ❤️", Toast.LENGTH_SHORT).show()
                );

            }).start();
        });

        // ▶ YOUTUBE
        btnVideo.setOnClickListener(v -> {

            String dish = tvDish.getText().toString();

            if (dish.isEmpty()) {
                Toast.makeText(getContext(), "Generate recipe first", Toast.LENGTH_SHORT).show();
                return;
            }

            String query = dish + " recipe";

            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/results?search_query=" + query));

            startActivity(intent);
        });

        // 📂 VIEW SAVED
        btnSaved.setOnClickListener(v -> {

            try {
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_recipes_to_saved);
            } catch (Exception e) {
                Toast.makeText(getContext(), "Navigation error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}