package com.example.pantrypal;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ShoppingListActivity extends AppCompatActivity {

    EditText etItem;
    Button btnAdd;
    RecyclerView rvShopping;
    TextView tvEmpty; // ✅ EMPTY STATE

    ShoppingDao dao;
    List<ShoppingItem> list;
    ShoppingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        etItem = findViewById(R.id.etItem);
        btnAdd = findViewById(R.id.btnAdd);
        rvShopping = findViewById(R.id.rvShopping);
        tvEmpty = findViewById(R.id.tvEmpty); // ✅

        dao = PantryDatabase.getInstance(this).shoppingDao();
        list = dao.getAllItems();

        adapter = new ShoppingAdapter(this, list, dao);
        rvShopping.setLayoutManager(new LinearLayoutManager(this));
        rvShopping.setAdapter(adapter);

        updateEmptyState(); // ✅ FIRST CHECK

        btnAdd.setOnClickListener(v -> {
            String name = etItem.getText().toString().trim();
            if (!name.isEmpty()) {
                dao.insert(new ShoppingItem(name));
                list.clear();
                list.addAll(dao.getAllItems());
                adapter.notifyDataSetChanged();
                etItem.setText("");
                updateEmptyState(); // ✅ AFTER ADD
            }
        });
    }

    // ✅ EMPTY STATE HANDLER
    private void updateEmptyState() {
        if (list.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvShopping.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvShopping.setVisibility(View.VISIBLE);
        }
    }
}
