package com.example.pantrypal;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListActivity extends AppCompatActivity {

    private RecyclerView rvShopping;
    private View layoutEmpty;
    private ImageButton btnAddItem;

    private ShoppingDao dao;
    private final List<ShoppingItem> list = new ArrayList<>();
    private ShoppingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        rvShopping = findViewById(R.id.rvShopping);
        layoutEmpty = findViewById(R.id.layoutEmpty);
        btnAddItem = findViewById(R.id.btnAddItem);

        dao = PantryDatabase.getInstance(this).shoppingDao();

        adapter = new ShoppingAdapter(this, list, dao, new ShoppingAdapter.OnListChangedListener() {
            @Override
            public void onListChanged() {
                refreshList();
            }
        });

        rvShopping.setLayoutManager(new LinearLayoutManager(this));
        rvShopping.setAdapter(adapter);

        refreshList(); // ✅ load sorted list

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDialog();
            }
        });
    }

    private void showAddDialog() {
        final EditText input = new EditText(this);
        input.setHint("e.g., Milk, Rice, Bread");
        input.setSingleLine(true);

        int pad = (int) (16 * getResources().getDisplayMetrics().density);
        input.setPadding(pad, pad, pad, pad);

        new AlertDialog.Builder(this)
                .setTitle("Add item")
                .setView(input)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Add", (dialog, which) -> {
                    String name = input.getText().toString().trim();
                    if (TextUtils.isEmpty(name)) return;

                    dao.insert(new ShoppingItem(name));
                    refreshList(); // ✅ keep sorting after add
                })
                .show();
    }

    private void refreshList() {
        list.clear();
        list.addAll(dao.getAllItemsSorted()); // ✅ auto sort
        adapter.notifyDataSetChanged();
        updateEmptyState();
    }

    private void updateEmptyState() {
        if (list.isEmpty()) {
            layoutEmpty.setVisibility(View.VISIBLE);
            rvShopping.setVisibility(View.GONE);
        } else {
            layoutEmpty.setVisibility(View.GONE);
            rvShopping.setVisibility(View.VISIBLE);
        }
    }
}
