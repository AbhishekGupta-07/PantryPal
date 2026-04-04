package com.example.pantrypal.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pantrypal.PantryDao;
import com.example.pantrypal.PantryDatabase;
import com.example.pantrypal.PantryItem;
import com.example.pantrypal.R;
import com.example.pantrypal.SavedRecipesActivity;
import com.example.pantrypal.ui.AddItemOptionsActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.HashSet;

public class HomeFragment extends Fragment {

    private TextView tvTotal, tvSoon, tvExpired, tvSafe;
    private TextView tvFavCount, tvSavedCount;

    private View addItemBox, cardFavorites, cardSaved;

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

        tvTotal = view.findViewById(R.id.tvTotalValue);
        tvSoon = view.findViewById(R.id.tvSoonValue);
        tvExpired = view.findViewById(R.id.tvExpiredValue);
        tvSafe = view.findViewById(R.id.tvSafeValue);

        tvFavCount = view.findViewById(R.id.tvFavCount);
        tvSavedCount = view.findViewById(R.id.tvSavedCount);

        addItemBox = view.findViewById(R.id.addItemBox);
        cardFavorites = view.findViewById(R.id.cardFavorites);
        cardSaved = view.findViewById(R.id.cardSaved);

        pantryDao = PantryDatabase.getInstance(requireContext()).pantryDao();

        addItemBox.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), AddItemOptionsActivity.class)));

        cardFavorites.setOnClickListener(v -> {
            Intent i = new Intent(requireContext(), SavedRecipesActivity.class);
            i.putExtra("type", "fav");
            startActivity(i);
        });

        cardSaved.setOnClickListener(v -> {
            Intent i = new Intent(requireContext(), SavedRecipesActivity.class);
            i.putExtra("type", "saved");
            startActivity(i);
        });

        loadDashboardData();
        updateCounts();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDashboardData();
        updateCounts();
    }

    // ✅ FIXED (IMPORTANT)
    private void updateCounts() {

        SharedPreferences prefs = requireContext().getSharedPreferences("recipes", Context.MODE_PRIVATE);

        Set<String> fav = prefs.getStringSet("fav", new HashSet<>());
        Set<String> saved = prefs.getStringSet("saved", new HashSet<>());

        int favCount = (fav != null) ? fav.size() : 0;
        int savedCount = (saved != null) ? saved.size() : 0;

        tvFavCount.setText(favCount + " Recipes");
        tvSavedCount.setText(savedCount + " Recipes");
    }

    private void loadDashboardData() {

        new Thread(() -> {

            List<PantryItem> list = pantryDao.getAllItems();

            double total = 0;
            int soon = 0, expired = 0, safe = 0;

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date today = new Date();

            if (list != null) {
                for (PantryItem item : list) {

                    total += item.getPrice();

                    try {
                        String expiryStr = item.getExpiryDate();

                        if (expiryStr != null && !expiryStr.isEmpty()) {

                            Date expiry = sdf.parse(expiryStr);

                            if (expiry != null) {

                                long diff = expiry.getTime() - today.getTime();

                                if (diff < 0) expired++;
                                else if (diff <= 3L * 24 * 60 * 60 * 1000) soon++;
                                else safe++;
                            }
                        }

                    } catch (Exception ignored) {}
                }
            }

            final double finalTotal = total;
            final int finalSoon = soon;
            final int finalExpired = expired;
            final int finalSafe = safe;

            final String totalStr = "₹" + String.format(Locale.getDefault(), "%.0f", finalTotal);

            if (isAdded()) {
                requireActivity().runOnUiThread(() -> {
                    tvTotal.setText(totalStr);
                    tvSoon.setText(String.valueOf(finalSoon));
                    tvExpired.setText(String.valueOf(finalExpired));
                    tvSafe.setText(String.valueOf(finalSafe));
                });
            }

        }).start();
    }
}