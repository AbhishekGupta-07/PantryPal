package com.example.pantrypal;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.navHostFragment);

        if (navHostFragment == null) {
            throw new IllegalStateException("NavHostFragment not found.");
        }

        navController = navHostFragment.getNavController();

        // 🔥 IMPORTANT: connect bottom nav to fragments
        NavigationUI.setupWithNavController(bottomNav, navController);

        // Debug log (optional)
        navController.addOnDestinationChangedListener((controller, destination, arguments) ->
                Log.d("NAV", "Now at: " + destination.getId())
        );

        // 🔥 SIMPLE CLEAN NAVIGATION (NO ACTIVITY)
        bottomNav.setOnItemSelectedListener(item ->
                NavigationUI.onNavDestinationSelected(item, navController)
        );

        bottomNav.setOnItemReselectedListener(item -> {
            // do nothing
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController != null && navController.navigateUp()
                || super.onSupportNavigateUp();
    }
}