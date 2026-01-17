package com.example.pantrypal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnAdd, btnView, btnRecipe, btnShopping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAddItem);
        btnView = findViewById(R.id.btnViewPantry);
        btnRecipe = findViewById(R.id.btnRecipe);
        btnShopping = findViewById(R.id.btnShoppingList); // ✅ NEW

        btnAdd.setOnClickListener(v ->
                startActivity(new Intent(this, AddItemActivity.class)));

        btnView.setOnClickListener(v ->
                startActivity(new Intent(this, PantryListActivity.class)));

        btnRecipe.setOnClickListener(v ->
                startActivity(new Intent(this, RecipeSuggestionActivity.class)));

        // ✅ SHOPPING LIST NAVIGATION
        btnShopping.setOnClickListener(v ->
                startActivity(new Intent(this, ShoppingListActivity.class)));
    }
}
