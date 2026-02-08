package com.example.pantrypal;

import android.content.Intent;
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

    // last non-shopping selected tab id
    private int lastNonShoppingTabId = 0;

    // shopping tab id (found dynamically)
    private int shoppingTabId = 0;

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

        // Connect bottom nav with navController
        NavigationUI.setupWithNavController(bottomNav, navController);

        // Find shopping tab id by title (no hardcoded id)
        shoppingTabId = findMenuItemIdByTitle(bottomNav.getMenu(), "shopping");

        // Default last tab
        lastNonShoppingTabId = bottomNav.getSelectedItemId();
        if (lastNonShoppingTabId == 0) {
            lastNonShoppingTabId = navController.getGraph().getStartDestinationId();
        }

        navController.addOnDestinationChangedListener((controller, destination, arguments) ->
                Log.d("NAV", "Now at: " + destination.getId())
        );

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            // If shopping id not found, behave normally
            if (shoppingTabId == 0) {
                return NavigationUI.onNavDestinationSelected(item, navController);
            }

            // If NOT shopping, navigate + update last tab
            if (id != shoppingTabId) {
                lastNonShoppingTabId = id;
                return NavigationUI.onNavDestinationSelected(item, navController);
            }

            // Shopping clicked -> open activity
            startActivity(new Intent(MainActivity.this, ShoppingListActivity.class));

            // Immediately reselect last tab so UI doesn't stay on shopping
            bottomNav.post(() -> bottomNav.setSelectedItemId(lastNonShoppingTabId));

            return true; // handled
        });

        bottomNav.setOnItemReselectedListener(item -> {
            // do nothing
        });
    }

    private int findMenuItemIdByTitle(Menu menu, String keywordLower) {
        if (menu == null) return 0;
        for (int i = 0; i < menu.size(); i++) {
            MenuItem it = menu.getItem(i);
            CharSequence title = it.getTitle();
            if (title != null && title.toString().toLowerCase().contains(keywordLower)) {
                return it.getItemId();
            }
        }
        return 0;
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController != null && navController.navigateUp() || super.onSupportNavigateUp();
    }
}
