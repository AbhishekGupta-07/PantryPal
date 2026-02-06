package com.example.pantrypal.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pantrypal.AddItemActivity;
import com.example.pantrypal.PantryDao;
import com.example.pantrypal.PantryDatabase;
import com.example.pantrypal.PantryItem;
import com.example.pantrypal.R;
import com.example.pantrypal.RecipeSuggestionActivity;
import com.example.pantrypal.ShoppingListActivity;
import com.example.pantrypal.utils.ExpiryUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class HomeFragment extends Fragment {

    private static final String RESULT_KEY = "pantry_filter_request";
    private static final String FILTER_KEY = "filter";

    private TextView tvTotal, tvSoon, tvExpired, tvSafe;
    private MaterialButton btnAddItem, btnAskAI, btnShopping;
    private ImageButton btnProfile;

    private PantryDao pantryDao;

    public HomeFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvTotal = view.findViewById(R.id.tvTotalValue);
        tvSoon = view.findViewById(R.id.tvSoonValue);
        tvExpired = view.findViewById(R.id.tvExpiredValue);
        tvSafe = view.findViewById(R.id.tvSafeValue);

        btnAddItem = view.findViewById(R.id.btnAddItem);
        btnAskAI = view.findViewById(R.id.btnAskAI);
        btnShopping = view.findViewById(R.id.btnShopping);
        btnProfile = view.findViewById(R.id.btnProfile);

        pantryDao = PantryDatabase.getInstance(requireContext()).pantryDao();

        // ---------------- Quick Actions ----------------

        btnAddItem.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), AddItemActivity.class))
        );

        btnAskAI.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), RecipeSuggestionActivity.class))
        );

        btnShopping.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), ShoppingListActivity.class))
        );

        btnProfile.setOnClickListener(v -> {
            // Future profile navigation
        });

        // ---------------- Dashboard Click ----------------

        setClickable(tvTotal, "ALL");
        setClickable(tvExpired, "EXPIRED");
        setClickable(tvSoon, "SOON");
        setClickable(tvSafe, "SAFE");

        loadDashboardCounts();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDashboardCounts();
    }

    private void setClickable(TextView tv, String filter) {
        if (tv == null) return;

        // click on number itself
        tv.setOnClickListener(v -> openPantryWithFilter(filter));

        // click on parent container
        View parent = (tv.getParent() instanceof View) ? (View) tv.getParent() : null;
        if (parent != null) parent.setOnClickListener(v -> openPantryWithFilter(filter));

        // click on parent's parent too (in case card layout is nested)
        View parent2 = (parent != null && parent.getParent() instanceof View) ? (View) parent.getParent() : null;
        if (parent2 != null) parent2.setOnClickListener(v -> openPantryWithFilter(filter));
    }

    private void openPantryWithFilter(String filter) {
        if (!isAdded()) return;

        Bundle b = new Bundle();
        b.putString(FILTER_KEY, filter);
        getParentFragmentManager().setFragmentResult(RESULT_KEY, b);

        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottomNav);
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(R.id.nav_pantry);
        }
    }

    private void loadDashboardCounts() {

        new Thread(() -> {

            List<PantryItem> all = pantryDao.getAllItems();

            int total = all.size();
            int expired = 0;
            int soon = 0;
            int safe = 0;

            for (PantryItem item : all) {
                String status = ExpiryUtils.getExpiryStatus(item.getExpiryDate());

                if ("Expired".equalsIgnoreCase(status)) expired++;
                else if ("Expiring Soon".equalsIgnoreCase(status)) soon++;
                else safe++;
            }

            final int finalTotal = total;
            final int finalExpired = expired;
            final int finalSoon = soon;
            final int finalSafe = safe;

            if (isAdded()) {
                requireActivity().runOnUiThread(() -> {
                    tvTotal.setText(String.valueOf(finalTotal));
                    tvExpired.setText(String.valueOf(finalExpired));
                    tvSoon.setText(String.valueOf(finalSoon));
                    tvSafe.setText(String.valueOf(finalSafe));
                });
            }

        }).start();
    }
}
