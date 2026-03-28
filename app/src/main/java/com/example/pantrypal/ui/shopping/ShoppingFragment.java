package com.example.pantrypal.ui.shopping;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pantrypal.PantryDatabase;
import com.example.pantrypal.R;
import com.example.pantrypal.ShoppingAdapter;
import com.example.pantrypal.ShoppingDao;
import com.example.pantrypal.ShoppingItem;

import java.util.ArrayList;
import java.util.List;

public class ShoppingFragment extends Fragment {

    private RecyclerView recyclerView;
    private EditText etItem;
    private Button btnAdd;
    private TextView tvCount;

    private ShoppingDao dao;
    private final List<ShoppingItem> list = new ArrayList<>();
    private ShoppingAdapter adapter;

    private String filter = "ALL";

    public ShoppingFragment() {
        super(R.layout.fragment_shopping);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerShopping);
        etItem = view.findViewById(R.id.etItem);
        btnAdd = view.findViewById(R.id.btnAdd);
        tvCount = view.findViewById(R.id.tvCount);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dao = PantryDatabase.getInstance(requireContext()).shoppingDao();

        adapter = new ShoppingAdapter(getContext(), list, dao, this::loadItems);
        recyclerView.setAdapter(adapter);

        setupFilters(view);

        loadItems();

        // ➕ ADD ITEM
        btnAdd.setOnClickListener(v -> {

            String text = etItem.getText().toString().trim();

            if (TextUtils.isEmpty(text)) {
                etItem.setError("Enter item");
                return;
            }

            new Thread(() -> {
                dao.insert(new ShoppingItem(text));

                if (!isAdded()) return;

                requireActivity().runOnUiThread(() -> {
                    etItem.setText("");
                    loadItems();
                });

            }).start();
        });
    }

    // 🔥 FILTER BUTTONS
    private void setupFilters(View view) {
        view.findViewById(R.id.btnAll).setOnClickListener(v -> {
            filter = "ALL";
            loadItems();
        });

        view.findViewById(R.id.btnPending).setOnClickListener(v -> {
            filter = "PENDING";
            loadItems();
        });

        view.findViewById(R.id.btnDone).setOnClickListener(v -> {
            filter = "DONE";
            loadItems();
        });
    }

    // 🔄 LOAD ITEMS
    private void loadItems() {

        new Thread(() -> {

            List<ShoppingItem> items;

            if (filter.equals("PENDING")) {
                items = dao.getPendingItems();
            } else if (filter.equals("DONE")) {
                items = dao.getCompletedItems();
            } else {
                items = dao.getAllItemsSorted();
            }

            if (!isAdded()) return;

            requireActivity().runOnUiThread(() -> {
                list.clear();
                list.addAll(items);
                adapter.notifyDataSetChanged();
                updateCount(items);
            });

        }).start();
    }

    // 📊 COUNT
    private void updateCount(List<ShoppingItem> items) {
        int done = 0;
        for (ShoppingItem i : items) {
            if (i.isPurchased()) done++;
        }
        int pending = items.size() - done;

        tvCount.setText(pending + " Pending • " + done + " Done");
    }
}