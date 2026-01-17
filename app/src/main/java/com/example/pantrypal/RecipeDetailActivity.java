package com.example.pantrypal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // 🔹 Toolbar setup (Back button)
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.recipe_details));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView tvTitle = findViewById(R.id.tvRecipeTitle);
        TextView tvContent = findViewById(R.id.tvRecipeContent);
        Button btnShare = findViewById(R.id.btnShareRecipe);

        // 🔹 Data received from Intent
        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");

        // 🔹 Safe handling
        tvTitle.setText(title != null ? title : getString(R.string.recipe_details));
        tvContent.setText(content != null ? content : "No recipe details available");

        // 📤 SHARE RECIPE
        btnShare.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");

            String shareText =
                    "🍽️ " + tvTitle.getText().toString() + "\n\n" +
                            tvContent.getText().toString() + "\n\n" +
                            "Shared via PantryPal ❤️";

            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(shareIntent, "Share Recipe via"));
        });
    }

    // 🔙 Back button support
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
