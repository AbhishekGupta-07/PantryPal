package com.example.pantrypal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pantrypal.utils.ExpiryUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RecipeSuggestionActivity extends AppCompatActivity {

    private final List<PantryItem> pantryItems = new ArrayList<>();

    private EditText etMood;
    private Button btnGetRecipe, btnWatchVideo, btnShareRecipe,
            btnSaveRecipe, btnViewSavedRecipes;
    private TextView tvDishName, tvRecipeResult;
    private ProgressBar progressBar;

    private String lastRecipeTitle = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_suggestion);

        etMood = findViewById(R.id.etMood);
        btnGetRecipe = findViewById(R.id.btnGetRecipe);
        btnWatchVideo = findViewById(R.id.btnWatchVideo);
        btnShareRecipe = findViewById(R.id.btnShareRecipe);
        btnSaveRecipe = findViewById(R.id.btnSaveRecipe);
        btnViewSavedRecipes = findViewById(R.id.btnViewSavedRecipes);
        tvDishName = findViewById(R.id.tvDishName);
        tvRecipeResult = findViewById(R.id.tvRecipeResult);
        progressBar = findViewById(R.id.progressBar);

        hideAll();

        // Load pantry items
        Executors.newSingleThreadExecutor().execute(() -> {
            List<PantryItem> items =
                    PantryDatabase.getInstance(getApplicationContext())
                            .pantryDao()
                            .getAllItems();

            runOnUiThread(() -> {
                pantryItems.clear();
                pantryItems.addAll(items);
            });
        });

        btnGetRecipe.setOnClickListener(v -> {

            String mood = etMood.getText().toString().trim();
            if (mood.isEmpty()) {
                Toast.makeText(this, "Enter your mood", Toast.LENGTH_SHORT).show();
                return;
            }

            hideAll();
            progressBar.setVisibility(View.VISIBLE);

            Executors.newSingleThreadExecutor().execute(() -> {

                RecipeData data = prepareRecipeData(mood, pantryItems);

                runOnUiThread(() -> {
                    lastRecipeTitle = data.recipeTitle;
                    tvDishName.setText("🍽 " + lastRecipeTitle);
                });

                String prompt = buildAIPrompt(
                        data.recipeTitle,
                        data.ingredients,
                        data.season
                );

                fetchAISteps(prompt);
            });
        });

        btnWatchVideo.setOnClickListener(v -> {
            String query = lastRecipeTitle + " easy indian recipe";
            startActivity(new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/results?search_query="
                            + Uri.encode(query))
            ));
        });

        btnShareRecipe.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT,
                    "🍽 " + lastRecipeTitle + "\n\n" +
                            tvRecipeResult.getText().toString());
            startActivity(Intent.createChooser(intent, "Share Recipe"));
        });

        btnSaveRecipe.setOnClickListener(v -> {
            Executors.newSingleThreadExecutor().execute(() ->
                    PantryDatabase.getInstance(this)
                            .savedRecipeDao()
                            .insert(new SavedRecipe(
                                    lastRecipeTitle,
                                    tvRecipeResult.getText().toString()
                            ))
            );
            Toast.makeText(this, "Recipe saved ❤️", Toast.LENGTH_SHORT).show();
        });

        btnViewSavedRecipes.setOnClickListener(v ->
                startActivity(new Intent(this, SavedRecipesActivity.class))
        );
    }

    private void hideAll() {
        tvDishName.setVisibility(View.GONE);
        tvRecipeResult.setVisibility(View.GONE);
        btnWatchVideo.setVisibility(View.GONE);
        btnShareRecipe.setVisibility(View.GONE);
        btnSaveRecipe.setVisibility(View.GONE);
        btnViewSavedRecipes.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    private void showAll() {
        tvDishName.setVisibility(View.VISIBLE);
        tvRecipeResult.setVisibility(View.VISIBLE);
        btnWatchVideo.setVisibility(View.VISIBLE);
        btnShareRecipe.setVisibility(View.VISIBLE);
        btnSaveRecipe.setVisibility(View.VISIBLE);
        btnViewSavedRecipes.setVisibility(View.VISIBLE);
    }

    // ---------- AI PREP ----------

    private RecipeData prepareRecipeData(String mood, List<PantryItem> items) {

        List<String> ingredients = new ArrayList<>();
        for (PantryItem item : items) {
            if (ExpiryUtils.isExpiringSoon(item.getExpiryDate())
                    && ingredients.size() < 5) {
                ingredients.add(item.getName());
            }
        }

        if (ingredients.isEmpty()) {
            ingredients.add("Onion");
            ingredients.add("Tomato");
            ingredients.add("Oil");
        }

        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        String season =
                (month <= 6) ? "Summer" :
                        (month <= 10) ? "Monsoon" : "Winter";

        String recipeType =
                mood.equalsIgnoreCase("happy") ? "Quick Stir Fry" :
                        mood.equalsIgnoreCase("sad") ? "Comfort Food" :
                                mood.equalsIgnoreCase("lazy") ? "One Pot Meal" :
                                        mood.equalsIgnoreCase("healthy") ? "Healthy Bowl" :
                                                "Simple Homemade Dish";

        return new RecipeData(recipeType, ingredients, season);
    }

    private String buildAIPrompt(String recipeType, List<String> ingredients, String season) {
        return "You are an expert Indian home chef.\n\n" +
                "Create a detailed step-by-step cooking recipe.\n\n" +
                "Dish: " + recipeType + "\n" +
                "Season: " + season + "\n" +
                "Ingredients: " + String.join(", ", ingredients) + "\n\n" +
                "Rules:\n" +
                "- Steps must be strictly based on these ingredients\n" +
                "- Explain preparation, order, flame level, and timing\n" +
                "- Avoid generic steps\n" +
                "- Write 6 to 10 clear steps\n";
    }

    // ---------- AI CALL ----------

    private void fetchAISteps(String prompt) {

        OkHttpClient client = new OkHttpClient();

        try {
            JSONObject body = new JSONObject();
            body.put("model", "gpt-3.5-turbo");

            JSONArray messages = new JSONArray();
            messages.put(new JSONObject()
                    .put("role", "user")
                    .put("content", prompt));
            body.put("messages", messages);

            Request request = new Request.Builder()
                    .url("https://api.openai.com/v1/chat/completions")
                    .addHeader("Authorization", "Bearer YOUR_OPENAI_API_KEY")
                    .addHeader("Content-Type", "application/json")
                    .post(RequestBody.create(
                            body.toString(),
                            MediaType.parse("application/json")
                    ))
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    String res = response.body().string();
                    try {
                        JSONObject json = new JSONObject(res);
                        String aiText = json.getJSONArray("choices")
                                .getJSONObject(0)
                                .getJSONObject("message")
                                .getString("content");

                        runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            tvRecipeResult.setText(aiText);
                            showAll();
                        });

                    } catch (Exception e) {
                        runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            tvRecipeResult.setText("AI response error");
                            showAll();
                        });
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        tvRecipeResult.setText("AI unavailable");
                        showAll();
                    });
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class RecipeData {
        String recipeTitle;
        List<String> ingredients;
        String season;

        RecipeData(String r, List<String> i, String s) {
            recipeTitle = r;
            ingredients = i;
            season = s;
        }
    }
}
