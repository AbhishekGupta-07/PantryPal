package com.example.pantrypal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Set;

public class RecipeDetailActivity extends AppCompatActivity {

    TextView tvTitle, tvContent;
    Button btnShare, btnYoutube, btnFav, btnSave;

    public static Set<String> savedRecipes = new HashSet<>();
    public static Set<String> favoriteRecipes = new HashSet<>();

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        tvTitle = findViewById(R.id.tvRecipeTitle);
        tvContent = findViewById(R.id.tvRecipeContent);

        btnShare = findViewById(R.id.btnShareRecipe);
        btnYoutube = findViewById(R.id.btnYoutube);
        btnFav = findViewById(R.id.btnFav);
        btnSave = findViewById(R.id.btnSave);

        prefs = getSharedPreferences("recipes", MODE_PRIVATE);

        // 🔥 SAFE LOAD (IMPORTANT FIX)
        savedRecipes = new HashSet<>(prefs.getStringSet("saved", new HashSet<>()));
        favoriteRecipes = new HashSet<>(prefs.getStringSet("fav", new HashSet<>()));

        // 🔥 SAFE INTENT DATA
        String name = getIntent().getStringExtra("name");
        if (name == null || name.trim().isEmpty()) {
            name = "Recipe 🍽";
        }
        final String finalName = name;

        tvTitle.setText(finalName);

        String content = getRecipeDetails(finalName);
        if (content == null) content = "No details available";

        final String finalContent = content;
        tvContent.setText(finalContent);

        // 🔥 BUTTON STATE
        updateButtons(finalName);

        // ❤️ FAVORITE
        btnFav.setOnClickListener(v -> {
            if (favoriteRecipes.contains(finalName)) {
                favoriteRecipes.remove(finalName);
                Toast.makeText(this, "Removed from Favorites ❌", Toast.LENGTH_SHORT).show();
            } else {
                favoriteRecipes.add(finalName);
                Toast.makeText(this, "Added to Favorites ❤️", Toast.LENGTH_SHORT).show();
            }
            saveData();
            updateButtons(finalName);
        });

        // 💾 SAVE
        btnSave.setOnClickListener(v -> {
            if (savedRecipes.contains(finalName)) {
                savedRecipes.remove(finalName);
                Toast.makeText(this, "Removed ❌", Toast.LENGTH_SHORT).show();
            } else {
                savedRecipes.add(finalName);
                Toast.makeText(this, "Saved Successfully 💾", Toast.LENGTH_SHORT).show();
            }
            saveData();
            updateButtons(finalName);
        });

        // 📤 SHARE
        btnShare.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, finalName + "\n\n" + finalContent);
                startActivity(Intent.createChooser(intent, "Share Recipe"));
            } catch (Exception e) {
                Toast.makeText(this, "Share failed", Toast.LENGTH_SHORT).show();
            }
        });

        // 🎥 YOUTUBE
        btnYoutube.setOnClickListener(v -> {
            try {
                String query = finalName.replace(" ", "+");
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.youtube.com/results?search_query=" + query)));
            } catch (Exception e) {
                Toast.makeText(this, "Cannot open YouTube", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveData() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet("saved", new HashSet<>(savedRecipes)); // 🔥 FIX
        editor.putStringSet("fav", new HashSet<>(favoriteRecipes)); // 🔥 FIX
        editor.apply();
    }

    private void updateButtons(String name) {
        btnFav.setText(favoriteRecipes.contains(name) ? "❤️ Favorited" : "❤️ Favorite");
        btnSave.setText(savedRecipes.contains(name) ? "✅ Saved" : "💾 Save");
    }

    private String getRecipeDetails(String name) {

        String cleanName = name.toLowerCase().replaceAll("[^a-z ]", "").trim();

        switch (cleanName) {

            case "bread sandwich":
                return "🧾 Ingredients:\n" +
                        "- Bread slices\n- Butter\n- Tomato\n- Cucumber\n- Potato\n- Chutney\n\n" +
                        "👨‍🍳 Steps:\n" +
                        "1. Slice veggies\n2. Apply butter\n3. Add chutney\n4. Add fillings\n5. Close\n6. Cut\n7. Serve\n8. Enjoy\n9. Fresh\n10. Done";

            case "maggi":
                return "🧾 Ingredients:\n" +
                        "- Maggi\n- Water\n- Tastemaker\n- Onion\n\n" +
                        "👨‍🍳 Steps:\n" +
                        "1. Heat oil\n2. Add onion\n3. Add water\n4. Add tastemaker\n5. Add noodles\n6. Cook\n7. Mix\n8. Serve\n9. Enjoy\n10. Done";

            case "omelette":
                return "🧾 Ingredients:\n" +
                        "- Eggs\n- Onion\n- Salt\n- Oil\n\n" +
                        "👨‍🍳 Steps:\n" +
                        "1. Crack eggs\n2. Add onion\n3. Add salt\n4. Beat\n5. Heat pan\n6. Add oil\n7. Cook\n8. Flip\n9. Serve\n10. Enjoy";

            case "poha":
                return "🧾 Ingredients:\n" +
                        "- Poha\n- Onion\n- Mustard\n\n" +
                        "👨‍🍳 Steps:\n" +
                        "1. Wash poha\n2. Heat oil\n3. Add mustard\n4. Add onion\n5. Add poha\n6. Mix\n7. Cook\n8. Add lemon\n9. Serve\n10. Enjoy";

            case "upma":
                return "🧾 Ingredients:\n" +
                        "- Rava\n- Onion\n- Water\n\n" +
                        "👨‍🍳 Steps:\n" +
                        "1. Roast rava\n2. Heat oil\n3. Add onion\n4. Add water\n5. Add salt\n6. Add rava\n7. Mix\n8. Cook\n9. Serve\n10. Enjoy";

            case "fruit salad":
                return "🧾 Ingredients:\n" +
                        "- Apple\n- Banana\n- Orange\n\n" +
                        "👨‍🍳 Steps:\n" +
                        "1. Cut fruits\n2. Mix\n3. Add honey\n4. Chill\n5. Serve\n6. Enjoy\n7. Fresh\n8. Healthy\n9. Ready\n10. Done";

            case "milkshake":
                return "🧾 Ingredients:\n" +
                        "- Milk\n- Banana\n- Sugar\n\n" +
                        "👨‍🍳 Steps:\n" +
                        "1. Add milk\n2. Add banana\n3. Add sugar\n4. Blend\n5. Serve\n6. Chill\n7. Enjoy\n8. Fresh\n9. Ready\n10. Done";

            case "toast butter":
                return "🧾 Ingredients:\n" +
                        "- Bread\n- Butter\n\n" +
                        "👨‍🍳 Steps:\n" +
                        "1. Toast bread\n2. Apply butter\n3. Serve\n4. Enjoy\n5. Hot\n6. Crispy\n7. Ready\n8. Eat\n9. Done\n10. Finish";

            default:
                return "🧾 Ingredients:\n- Basic items\n\n👨‍🍳 Steps:\n1. Cook\n2. Serve";
        }
    }
}