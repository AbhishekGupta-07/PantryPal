package com.example.pantrypal;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 🔹 Bottom Navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        // 🔹 NavHostFragment
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.navHostFragment);

        if (navHostFragment == null) {
            throw new IllegalStateException("NavHostFragment not found. Check activity_main.xml");
        }

        navController = navHostFragment.getNavController();

        // 🔥 MAIN CONNECTION (IMPORTANT)
        NavigationUI.setupWithNavController(bottomNav, navController);

        // 🔍 DEBUG LOG (optional but useful)
        navController.addOnDestinationChangedListener((controller, destination, arguments) ->
                Log.d("NAV", "Now at: " + destination.getLabel())
        );
    }

    // 🔙 BACK HANDLING
    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (navController != null && navController.getCurrentDestination() != null) {

            int id = navController.getCurrentDestination().getId();

            // 🔥 If not on Home → go to Home
            if (id != R.id.nav_home) {
                navController.navigate(R.id.nav_home);
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }
}