package com.example.pantrypal;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeSuggestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_suggestion);

        EditText etMood = findViewById(R.id.etMood);
        Button btnGetRecipe = findViewById(R.id.btnGetRecipe);

        btnGetRecipe.setOnClickListener(v -> {
            String mood = etMood.getText().toString().trim();

            if (mood.isEmpty()) {
                Toast.makeText(this,
                        "Please enter your mood",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,
                        "AI will suggest recipes based on mood: " + mood,
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
