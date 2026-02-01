package com.example.pantrypal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button btnAdd, btnView, btnRecipe, btnShopping, btnLogout;

    private FirebaseAuth auth;
    private GoogleSignInClient googleClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        btnAdd = findViewById(R.id.btnAddItem);
        btnView = findViewById(R.id.btnViewPantry);
        btnRecipe = findViewById(R.id.btnRecipe);
        btnShopping = findViewById(R.id.btnShoppingList);
        btnLogout = findViewById(R.id.btnLogout);

        btnAdd.setOnClickListener(v ->
                startActivity(new Intent(this, AddItemActivity.class)));

        btnView.setOnClickListener(v ->
                startActivity(new Intent(this, PantryListActivity.class)));

        btnRecipe.setOnClickListener(v ->
                startActivity(new Intent(this, RecipeSuggestionActivity.class)));

        btnShopping.setOnClickListener(v ->
                startActivity(new Intent(this, ShoppingListActivity.class)));

        // 🔴 LOGOUT
        btnLogout.setOnClickListener(v -> logoutUser());
    }

    private void logoutUser() {
        // Firebase logout
        auth.signOut();

        // Google logout
        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build();

        googleClient = GoogleSignIn.getClient(this, gso);
        googleClient.signOut().addOnCompleteListener(task -> {
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        });
    }
}
