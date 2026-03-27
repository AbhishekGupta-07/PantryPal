package com.example.pantrypal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView tvTitle, tvContent;
    private Button btnShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // 🔹 Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.recipe_details));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // 🔹 Bind Views
        tvTitle = findViewById(R.id.tvRecipeTitle);
        tvContent = findViewById(R.id.tvRecipeContent);
        btnShare = findViewById(R.id.btnShareRecipe);

        // 🔹 Get Data (SAFE)
        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");

        if (title == null || title.isEmpty()) {
            title = getString(R.string.recipe_details);
        }

        if (content == null || content.isEmpty()) {
            content = "No recipe details available";
        }

        tvTitle.setText(title);
        tvContent.setText(content);

        // 📤 SHARE
        btnShare.setOnClickListener(v -> shareRecipe());
    }

    private void shareRecipe() {
        String shareText =
                "🍽 " + tvTitle.getText().toString() + "\n\n" +
                        tvContent.getText().toString() + "\n\n" +
                        "Shared via PantryPal ❤️";

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, shareText);

        startActivity(Intent.createChooser(intent, "Share Recipe"));
    }

    // 🔙 BACK BUTTON
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}