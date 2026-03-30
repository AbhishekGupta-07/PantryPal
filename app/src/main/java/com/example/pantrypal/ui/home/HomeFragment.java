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

import com.example.pantrypal.PantryDao;
import com.example.pantrypal.PantryDatabase;
import com.example.pantrypal.PantryItem;
import com.example.pantrypal.R;
import com.example.pantrypal.ui.AddItemOptionsActivity; // ✅ IMPORTANT CHANGE
import com.example.pantrypal.utils.ExpiryUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private static final String RESULT_KEY = "pantry_filter_request";
    private static final String FILTER_KEY = "filter";

    private TextView tvTotal, tvSoon, tvExpired, tvSafe;
    private ImageButton btnProfile;
    private View addItemBox;

    private PantryDao pantryDao;

    public HomeFragment() {}

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

        // 🔹 INIT VIEWS
        tvTotal = view.findViewById(R.id.tvTotalValue);
        tvSoon = view.findViewById(R.id.tvSoonValue);
        tvExpired = view.findViewById(R.id.tvExpiredValue);
        tvSafe = view.findViewById(R.id.tvSafeValue);

        btnProfile = view.findViewById(R.id.btnProfile);
        addItemBox = view.findViewById(R.id.addItemBox);

        pantryDao = PantryDatabase.getInstance(requireContext()).pantryDao();

        // 🔥 ADD ITEM CLICK (UPDATED)
        if (addItemBox != null) {
            addItemBox.setOnClickListener(v ->
                    startActivity(new Intent(requireContext(), AddItemOptionsActivity.class))
            );
        }

        // 🔹 PROFILE CLICK
        if (btnProfile != null) {
            btnProfile.setOnClickListener(v -> {});
        }

        // 🔹 FILTER CLICK
        setClickable(tvTotal, "ALL");
        setClickable(tvExpired, "EXPIRED");
        setClickable(tvSoon, "SOON");
        setClickable(tvSafe, "SAFE");

        loadDashboardData();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDashboardData();
    }

    private void setClickable(TextView tv, String filter) {
        if (tv == null) return;

        tv.setOnClickListener(v -> openPantryWithFilter(filter));
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

    private void loadDashboardData() {

        new Thread(() -> {

            List<PantryItem> all = pantryDao.getAllItems();

            int expired = 0;
            int soon = 0;
            int safe = 0;
            double totalValue = 0;

            if (all != null) {
                for (PantryItem item : all) {

                    totalValue += item.getPrice();

                    String status = ExpiryUtils.getExpiryStatus(item.getExpiryDate());

                    if ("Expired".equalsIgnoreCase(status)) expired++;
                    else if ("Expiring Soon".equalsIgnoreCase(status)) soon++;
                    else safe++;
                }
            }

            final int fExpired = expired;
            final int fSoon = soon;
            final int fSafe = safe;

            final String formattedValue =
                    "₹" + String.format(Locale.getDefault(), "%.0f", totalValue);

            if (isAdded()) {
                requireActivity().runOnUiThread(() -> {

                    if (tvTotal != null) tvTotal.setText(formattedValue);
                    if (tvExpired != null) tvExpired.setText(String.valueOf(fExpired));
                    if (tvSoon != null) tvSoon.setText(String.valueOf(fSoon));
                    if (tvSafe != null) tvSafe.setText(String.valueOf(fSafe));
                });
            }

        }).start();
    }
}