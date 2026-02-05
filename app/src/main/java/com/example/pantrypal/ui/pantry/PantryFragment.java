package com.example.pantrypal.ui.pantry;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

    private RecyclerView rvPantry;
    private TextView tvEmpty;

    private PantryAdapter adapter;
    private PantryDao pantryDao;

    private enum Filter { ALL, EXPIRED, SOON, SAFE }
    private Filter currentFilter = Filter.ALL;

    public PantryFragment() {}

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

        setHasOptionsMenu(true);

        rvPantry = view.findViewById(R.id.rvPantry);
        tvEmpty = view.findViewById(R.id.tvEmpty);

        rvPantry.setLayoutManager(new LinearLayoutManager(requireContext()));

        pantryDao = PantryDatabase.getInstance(requireContext()).pantryDao();

        adapter = new PantryAdapter(requireContext(), new ArrayList<>());
        rvPantry.setAdapter(adapter);

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
            List<PantryItem> filtered = applyFilter(list);

            if (!isAdded()) return;

            requireActivity().runOnUiThread(() -> {
                adapter.updateList(filtered);
                updateEmptyState(filtered);
            });
        }).start();
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

    // ---------------- MENU (Filter Icon) ----------------

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.pantry_filter_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_filter) {
            // icon click - kuch nahi, kyunki actual options menu me items already hain
            return true;
        }

        if (id == R.id.filter_all) currentFilter = Filter.ALL;
        else if (id == R.id.filter_expired) currentFilter = Filter.EXPIRED;
        else if (id == R.id.filter_soon) currentFilter = Filter.SOON;
        else if (id == R.id.filter_safe) currentFilter = Filter.SAFE;
        else return super.onOptionsItemSelected(item);

        loadItems();
        return true;
    }
}
