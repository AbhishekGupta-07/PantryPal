package com.example.pantrypal.ui.shopping;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
    private ImageButton btnShare;

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
        btnShare = view.findViewById(R.id.btnShare);

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

        // 🔥 SHARE BUTTON
        btnShare.setOnClickListener(v -> shareList());
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

    // 🔗 SHARE FEATURE (UPGRADED)
    private void shareList() {

        new Thread(() -> {

            List<ShoppingItem> items = dao.getAllItemsSorted();

            if (items == null || items.isEmpty()) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Shopping list is empty", Toast.LENGTH_SHORT).show()
                );
                return;
            }

            StringBuilder text = new StringBuilder("🛒 PantryPal Shopping List\n\n");

            // 🟡 Pending Section
            text.append("🟡 Pending:\n");
            for (ShoppingItem item : items) {
                if (!item.isPurchased()) {
                    text.append("• ")
                            .append(item.getItemName())
                            .append("\n");
                }
            }

            // ✅ Completed Section
            text.append("\n✅ Completed:\n");
            for (ShoppingItem item : items) {
                if (item.isPurchased()) {
                    text.append("✔ ")
                            .append(item.getItemName())
                            .append("\n");
                }
            }

            text.append("\nGenerated by PantryPal 📱");

            requireActivity().runOnUiThread(() -> {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, text.toString());

                startActivity(Intent.createChooser(intent, "Share via"));

            });

        }).start();
    }
}