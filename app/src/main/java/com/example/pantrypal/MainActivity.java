package com.example.pantrypal;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
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
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostFragment);

        if (navHostFragment == null) {
            throw new IllegalStateException("NavHostFragment not found. Check activity_main.xml navHostFragment id.");
        }

        navController = navHostFragment.getNavController();

        // ✅ Important: menu item ids MUST match nav_graph fragment ids
        // menu:    @id/homeFragment, @id/pantryFragment, @id/recipesFragment, @id/shoppingFragment, @id/profileFragment
        // navGraph: same ids
        NavigationUI.setupWithNavController(bottomNav, navController);

        // ✅ Prevent "re-click" from doing weird backstack behavior
        bottomNav.setOnItemReselectedListener(item -> {
            // do nothing
        });

        // ✅ Optional: log destination changes (helps when it "exits" due to crash in that fragment)
        navController.addOnDestinationChangedListener((controller, destination, arguments) ->
                Log.d("NAV", "Now at: " + destination.getId())
        );
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController != null && navController.navigateUp() || super.onSupportNavigateUp();
    }
}
