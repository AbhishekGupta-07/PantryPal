package com.example.pantrypal.ui.pantry;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pantrypal.PantryAdapter;
import com.example.pantrypal.PantryDatabase;
import com.example.pantrypal.PantryItem;
import com.example.pantrypal.R;
import com.example.pantrypal.utils.ExpiryUtils;

import java.util.ArrayList;
import java.util.List;

public class PantryFragment extends Fragment {

    private RecyclerView rvPantry;
    private ImageButton btnFilter;
    private View layoutEmpty;

    private PantryAdapter adapter;

    private final List<PantryItem> allItems = new ArrayList<>();

    public PantryFragment() {
        super(R.layout.fragment_pantry);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPantry = view.findViewById(R.id.rvPantry);
        btnFilter = view.findViewById(R.id.btnFilter);
        layoutEmpty = view.findViewById(R.id.layoutEmpty);

        rvPantry.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new PantryAdapter(requireContext(), new ArrayList<>());
        rvPantry.setAdapter(adapter);

        btnFilter.setOnClickListener(v -> showFilterMenu());

        refreshList();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshList();
    }

    private void refreshList() {
        new Thread(() -> {
            List<PantryItem> dbItems = PantryDatabase.getInstance(requireContext()).pantryDao().getAllItems();
            allItems.clear();
            if (dbItems != null) allItems.addAll(dbItems);

            requireActivity().runOnUiThread(() -> {
                adapter.updateList(allItems);
                updateEmptyState(allItems.isEmpty());
            });
        }).start();
    }

    private void showFilterMenu() {
        PopupMenu popup = new PopupMenu(requireContext(), btnFilter);
        popup.getMenuInflater().inflate(R.menu.filter_menu, popup.getMenu());

        popup.setOnMenuItemClickListener((MenuItem item) -> {
            int id = item.getItemId();

            if (id == R.id.filter_all) applyFilter("All");
            else if (id == R.id.filter_expired) applyFilter("Expired");
            else if (id == R.id.filter_expiring_soon) applyFilter("Expiring Soon");
            else if (id == R.id.filter_safe) applyFilter("Safe");

            return true;
        });

        popup.show();
    }

    private void applyFilter(String filter) {
        List<PantryItem> filtered = new ArrayList<>();

        if ("All".equals(filter)) {
            filtered.addAll(allItems);
        } else {
            for (PantryItem item : allItems) {
                String status = ExpiryUtils.getExpiryStatus(item.getExpiryDate());
                if (filter.equals(status)) filtered.add(item);
            }
        }

        adapter.updateList(filtered);
        updateEmptyState(filtered.isEmpty());
    }

    private void updateEmptyState(boolean isEmpty) {
        layoutEmpty.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        rvPantry.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }
}
