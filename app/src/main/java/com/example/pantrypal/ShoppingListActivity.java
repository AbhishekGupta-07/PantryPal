package com.example.pantrypal;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar; // 🔥 IMPORTANT

import java.util.ArrayList;
import java.util.List;

public class ShoppingListActivity extends AppCompatActivity {

    private RecyclerView rvShopping;
    private View layoutEmpty;
    private ImageButton btnAddItem, btnDeleteAll;
    private TextView tvCount;

    private ShoppingDao dao;
    private final List<ShoppingItem> list = new ArrayList<>();
    private ShoppingAdapter adapter;

    private String filter = "ALL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        rvShopping = findViewById(R.id.recyclerShopping);
        layoutEmpty = findViewById(R.id.layoutEmpty);
        btnAddItem = findViewById(R.id.btnAddItem);
        btnDeleteAll = findViewById(R.id.btnDeleteAll);
        tvCount = findViewById(R.id.tvCount);

        dao = PantryDatabase.getInstance(this).shoppingDao();

        adapter = new ShoppingAdapter(this, list, dao, this::refreshList);

        rvShopping.setLayoutManager(new LinearLayoutManager(this));
        rvShopping.setAdapter(adapter);

        setupSwipeToDelete();

        refreshList();

        btnAddItem.setOnClickListener(v -> showAddDialog());

        btnDeleteAll.setOnClickListener(v -> showDeleteAllDialog());

        findViewById(R.id.btnAll).setOnClickListener(v -> {
            filter = "ALL";
            refreshList();
        });

        findViewById(R.id.btnPending).setOnClickListener(v -> {
            filter = "PENDING";
            refreshList();
        });

        findViewById(R.id.btnDone).setOnClickListener(v -> {
            filter = "DONE";
            refreshList();
        });
    }

    // 🔥 SWIPE DELETE + UNDO
    private void setupSwipeToDelete() {
        ItemTouchHelper.SimpleCallback swipe =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

                    @Override
                    public boolean onMove(@NonNull RecyclerView rv,
                                          @NonNull RecyclerView.ViewHolder vh,
                                          @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder vh, int dir) {
                        int pos = vh.getAdapterPosition();
                        ShoppingItem deletedItem = list.get(pos);

                        new Thread(() -> {
                            dao.delete(deletedItem);

                            runOnUiThread(() -> {
                                refreshList();

                                // 🔥 SNACKBAR UNDO
                                Snackbar.make(rvShopping, "Item deleted", Snackbar.LENGTH_LONG)
                                        .setAction("UNDO", v -> {

                                            new Thread(() -> {
                                                dao.insert(deletedItem);

                                                runOnUiThread(() -> refreshList());
                                            }).start();

                                        })
                                        .show();
                            });

                        }).start();
                    }
                };

        new ItemTouchHelper(swipe).attachToRecyclerView(rvShopping);
    }

    // ➕ ADD ITEM
    private void showAddDialog() {
        final EditText input = new EditText(this);
        input.setHint("e.g., Milk, Rice, Bread");

        new AlertDialog.Builder(this)
                .setTitle("Add item")
                .setView(input)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Add", (dialog, which) -> {
                    String name = input.getText().toString().trim();
                    if (TextUtils.isEmpty(name)) return;

                    new Thread(() -> {
                        dao.insert(new ShoppingItem(name));
                        runOnUiThread(this::refreshList);
                    }).start();
                })
                .show();
    }

    // 🗑 DELETE ALL
    private void showDeleteAllDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete All")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", (d, w) -> {
                    new Thread(() -> {
                        dao.deleteAll();
                        runOnUiThread(this::refreshList);
                    }).start();
                })
                .setNegativeButton("No", null)
                .show();
    }

    // 🔄 REFRESH + FILTER + COUNT
    private void refreshList() {
        new Thread(() -> {

            List<ShoppingItem> items;

            if (filter.equals("PENDING")) {
                items = dao.getPendingItems();
            } else if (filter.equals("DONE")) {
                items = dao.getCompletedItems();
            } else {
                items = dao.getAllItemsSorted();
            }

            runOnUiThread(() -> {
                list.clear();
                list.addAll(items);
                adapter.notifyDataSetChanged();
                updateEmptyState();
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