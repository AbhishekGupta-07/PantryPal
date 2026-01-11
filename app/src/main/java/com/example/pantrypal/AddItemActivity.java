package com.example.pantrypal;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_add_item);

        EditText etName = findViewById(R.id.etItemName);
        EditText etQty = findViewById(R.id.etQuantity);
        EditText etExpiry = findViewById(R.id.etExpiry);
        Button btnSave = findViewById(R.id.btnSaveItem);

        btnSave.setOnClickListener(v -> {
            PantryItem item = new PantryItem(
                    etName.getText().toString(),
                    etQty.getText().toString(),
                    etExpiry.getText().toString()
            );

            PantryDatabase.getInstance(this)
                    .pantryDao()
                    .insertItem(item);

            Toast.makeText(this, "Item Added", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
