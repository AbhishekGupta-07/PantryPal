package com.example.pantrypal.ui.shopping;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

import java.util.List;

public class ShoppingFragment extends Fragment {

    private RecyclerView recyclerView;
    private EditText etItem;
    private Button btnAdd;

    public ShoppingFragment() {
        super(R.layout.fragment_shopping);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerShopping);
        etItem = view.findViewById(R.id.etItem);
        btnAdd = view.findViewById(R.id.btnAdd);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        loadItems();

        // ➕ ADD ITEM
        btnAdd.setOnClickListener(v -> {

            String text = etItem.getText().toString().trim();

            if (text.isEmpty()) {
                etItem.setError("Enter item");
                return;
            }

            new Thread(() -> {

                PantryDatabase db = PantryDatabase.getInstance(requireContext());

                db.shoppingDao().insert(new ShoppingItem(text));

                requireActivity().runOnUiThread(() -> {
                    etItem.setText("");
                    loadItems(); // refresh
                });

            }).start();
        });
    }

    private void loadItems() {

        new Thread(() -> {

            PantryDatabase db = PantryDatabase.getInstance(requireContext());
            ShoppingDao dao = db.shoppingDao();

            List<ShoppingItem> list = dao.getAllItems();

            requireActivity().runOnUiThread(() -> {

                ShoppingAdapter adapter = new ShoppingAdapter(
                        requireContext(),
                        list,
                        dao,
                        this::loadItems // 🔥 auto refresh after delete/update
                );

                recyclerView.setAdapter(adapter);
            });

        }).start();
    }
}