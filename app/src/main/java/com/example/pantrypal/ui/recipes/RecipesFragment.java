package com.example.pantrypal.ui.recipes;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pantrypal.PantryDatabase;
import com.example.pantrypal.PantryItem;
import com.example.pantrypal.R;
import com.example.pantrypal.SavedRecipe;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import okhttp3.*;

public class RecipesFragment extends Fragment {

    private final String API_KEY = "PASTE_NEW_KEY";

    public RecipesFragment() {
        super(R.layout.fragment_recipes);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        EditText etInput = view.findViewById(R.id.etUserInput);
        Button btnGetRecipe = view.findViewById(R.id.btnGetRecipe);
        TextView tvDish = view.findViewById(R.id.tvDishName);
        TextView tvResult = view.findViewById(R.id.tvRecipeResult);
        Button btnSave = view.findViewById(R.id.btnSaveRecipe);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);

        btnGetRecipe.setOnClickListener(v -> {

            String input = etInput.getText().toString().trim();

            if (input.isEmpty()) {
                etInput.setError("Enter something");
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            new Thread(() -> {
                try {

                    List<PantryItem> items =
                            PantryDatabase.getInstance(requireContext())
                                    .pantryDao()
                                    .getAllItems();

                    StringBuilder pantry = new StringBuilder();

                    for (PantryItem item : items) {
                        pantry.append(item.getName()).append(", ");
                    }

                    String prompt = "Give recipe for: " + input + "\nPantry: " + pantry;

                    OkHttpClient client = new OkHttpClient();

                    JSONObject part = new JSONObject();
                    part.put("text", prompt);

                    JSONArray parts = new JSONArray();
                    parts.put(part);

                    JSONObject content = new JSONObject();
                    content.put("parts", parts);

                    JSONArray contents = new JSONArray();
                    contents.put(content);

                    JSONObject body = new JSONObject();
                    body.put("contents", contents);

                    Request request = new Request.Builder()
                            .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + API_KEY)
                            .post(RequestBody.create(
                                    body.toString(),
                                    MediaType.parse("application/json")
                            ))
                            .build();

                    Response response = client.newCall(request).execute();

                    String res = response.body().string();

                    Log.d("AI_RESPONSE", res);

                    JSONObject json = new JSONObject(res);

                    String result = json
                            .getJSONArray("candidates")
                            .getJSONObject(0)
                            .getJSONObject("content")
                            .getJSONArray("parts")
                            .getJSONObject(0)
                            .getString("text");

                    requireActivity().runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        tvDish.setText("AI Recipe 🍽");
                        tvResult.setText(result);
                        btnSave.setVisibility(View.VISIBLE);
                    });

                } catch (Exception e) {
                    e.printStackTrace();

                    requireActivity().runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Error ❌", Toast.LENGTH_SHORT).show();
                    });
                }

            }).start();
        });

        btnSave.setOnClickListener(v -> {

            String dish = tvDish.getText().toString();
            String recipe = tvResult.getText().toString();

            if (recipe.isEmpty()) return;

            new Thread(() -> {
                PantryDatabase.getInstance(requireContext())
                        .savedRecipeDao()
                        .insert(new SavedRecipe(dish, recipe));
            }).start();

            Toast.makeText(getContext(), "Saved ❤️", Toast.LENGTH_SHORT).show();
        });
    }
}