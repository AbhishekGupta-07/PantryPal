package com.example.pantrypal.ui.pantry;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pantrypal.PantryAdapter;
import com.example.pantrypal.PantryDao;
import com.example.pantrypal.PantryDatabase;
import com.example.pantrypal.PantryItem;
import com.example.pantrypal.R;
import com.example.pantrypal.utils.ExpiryUtils;

import java.util.ArrayList;
import java.util.List;

public class PantryFragment extends Fragment {

    // ✅ Fragment Result keys (must match HomeFragment)
    private static final String RESULT_KEY = "pantry_filter_request";
    private static final String FILTER_KEY = "filter";

    private String pendingFilter = null;

    private RecyclerView rvPantry;
    private TextView tvEmpty;
    private ImageButton btnFilter;

    private PantryAdapter adapter;
    private PantryDao pantryDao;

    private enum Filter { ALL, EXPIRED, SOON, SAFE }
    private Filter currentFilter = Filter.ALL;

    public PantryFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ✅ Listen filter request from Home
        getParentFragmentManager().setFragmentResultListener(
                RESULT_KEY,
                this,
                (requestKey, bundle) -> {
                    pendingFilter = bundle.getString(FILTER_KEY, "ALL");
                    applyPendingFilterIfPossible();
                    loadItems();
                }
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pantry, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPantry = view.findViewById(R.id.rvPantry);
        tvEmpty = view.findViewById(R.id.tvEmpty);
        btnFilter = view.findViewById(R.id.btnFilter);

        rvPantry.setLayoutManager(new LinearLayoutManager(requireContext()));

        pantryDao = PantryDatabase.getInstance(requireContext()).pantryDao();

        adapter = new PantryAdapter(requireContext(), new ArrayList<>());
        rvPantry.setAdapter(adapter);

        // ✅ GUARANTEED click working
        btnFilter.setOnClickListener(v -> showFilterDialog());

        applyPendingFilterIfPossible();
        loadItems();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadItems(); // add/edit/delete ke baad refresh
    }

    private void loadItems() {
        new Thread(() -> {
            List<PantryItem> list = pantryDao.getAllItems();

            applyPendingFilterIfPossible();
            List<PantryItem> filtered = applyFilter(list);

            if (!isAdded()) return;

            requireActivity().runOnUiThread(() -> {
                adapter.updateList(filtered);
                updateEmptyState(filtered);
            });
        }).start();
    }

    private void applyPendingFilterIfPossible() {
        if (pendingFilter == null) return;

        switch (pendingFilter) {
            case "EXPIRED":
                currentFilter = Filter.EXPIRED;
                break;
            case "SOON":
                currentFilter = Filter.SOON;
                break;
            case "SAFE":
                currentFilter = Filter.SAFE;
                break;
            default:
                currentFilter = Filter.ALL;
                break;
        }
        pendingFilter = null;
    }

    private List<PantryItem> applyFilter(List<PantryItem> list) {
        if (list == null) return new ArrayList<>();
        if (currentFilter == Filter.ALL) return list;

        List<PantryItem> out = new ArrayList<>();

        for (PantryItem item : list) {
            String status = ExpiryUtils.getExpiryStatus(item.getExpiryDate());

            if (currentFilter == Filter.EXPIRED && "Expired".equalsIgnoreCase(status)) {
                out.add(item);
            } else if (currentFilter == Filter.SOON && "Expiring Soon".equalsIgnoreCase(status)) {
                out.add(item);
            } else if (currentFilter == Filter.SAFE && "Safe".equalsIgnoreCase(status)) {
                out.add(item);
            }
        }
        return out;
    }

    private void updateEmptyState(List<PantryItem> list) {
        boolean empty = (list == null || list.isEmpty());

        if (empty) {
            rvPantry.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);

            if (currentFilter == Filter.EXPIRED) tvEmpty.setText("No expired items ✅");
            else if (currentFilter == Filter.SOON) tvEmpty.setText("No items expiring soon ✅");
            else if (currentFilter == Filter.SAFE) tvEmpty.setText("No safe items found");
            else tvEmpty.setText("No pantry items yet. Add your first item!");
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvPantry.setVisibility(View.VISIBLE);
        }
    }

    private void showFilterDialog() {
        final String[] options = {"All Items", "Expired", "Expiring Soon", "Safe"};

        new AlertDialog.Builder(requireContext())
                .setTitle("Filter")
                .setItems(options, (dialog, which) -> {

                    if (which == 0) currentFilter = Filter.ALL;
                    else if (which == 1) currentFilter = Filter.EXPIRED;
                    else if (which == 2) currentFilter = Filter.SOON;
                    else currentFilter = Filter.SAFE;

                    loadItems();
                })
                .show();
    }
}
