package com.example.pantrypal;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pantrypal.utils.ExpiryUtils;

import java.util.ArrayList;
import java.util.List;

public class PantryListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private View emptyView;
    private PantryAdapter adapter;

    private final List<PantryItem> allItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.pantryRecycler);
        emptyView = findViewById(R.id.emptyView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // ✅ Load from DB
        List<PantryItem> dbItems =
                PantryDatabase.getInstance(this)
                        .pantryDao()
                        .getAllItems();

        allItems.clear();
        if (dbItems != null) allItems.addAll(dbItems);

        adapter = new PantryAdapter(this, new ArrayList<>(allItems));
        recyclerView.setAdapter(adapter);

        // 🔥 APPLY FILTER FROM HOME (IMPORTANT)
        String filter = getIntent().getStringExtra("filter");

        if (filter != null) {
            if (filter.equals("expired")) {
                applyFilter("Expired");
            } else if (filter.equals("soon")) {
                applyFilter("Expiring Soon");
            } else if (filter.equals("safe")) {
                applyFilter("Safe");
            } else {
                applyFilter("All");
            }
        } else {
            applyFilter("All");
        }

        toggleEmptyState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.filter_all) {
            applyFilter("All");
        } else if (id == R.id.filter_expired) {
            applyFilter("Expired");
        } else if (id == R.id.filter_expiring_soon) {
            applyFilter("Expiring Soon");
        } else if (id == R.id.filter_safe) {
            applyFilter("Safe");
        }
        return true;
    }

    private void applyFilter(String filter) {
        List<PantryItem> filtered = new ArrayList<>();

        if ("All".equals(filter)) {
            filtered.addAll(allItems);
        } else {
            for (PantryItem item : allItems) {
                String status = ExpiryUtils.getExpiryStatus(item.getExpiryDate());
                if (status.equalsIgnoreCase(filter)) {
                    filtered.add(item);
                }
            }
        }

        adapter.updateList(filtered);
        toggleEmptyState();
    }

    private void toggleEmptyState() {
        if (adapter.getItemCount() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}