package com.example.pantrypal;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

public class AddItemActivity extends AppCompatActivity {

    EditText etName, etQty, etExpiry, etPrice;
    Button btnSave;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_add_item);

        etName = findViewById(R.id.etItemName);
        etQty = findViewById(R.id.etQuantity);
        etExpiry = findViewById(R.id.etExpiry);
        etPrice = findViewById(R.id.etPrice);
        btnSave = findViewById(R.id.btnSaveItem);

        // 🔥 DEFAULT QUANTITY
        etQty.setText("1");

        // 🔥 DATE PICKER (no keyboard)
        etExpiry.setFocusable(false);
        etExpiry.setClickable(true);
        etExpiry.setOnClickListener(v -> showDatePicker());

        // 🔥 AUTO-FILL FROM SCAN (SAFE VERSION)
        if (getIntent() != null) {

            String name = getIntent().getStringExtra("name");
            String price = getIntent().getStringExtra("price");
            String expiry = getIntent().getStringExtra("expiry");

            if (name != null && !name.trim().isEmpty()) {
                etName.setText(name.trim());
            }

            if (price != null && !price.trim().isEmpty()) {
                etPrice.setText(price.trim());
            }

            if (expiry != null && !expiry.trim().isEmpty()) {
                etExpiry.setText(expiry.trim());
            }
        }

        // 🔥 SAVE BUTTON
        btnSave.setOnClickListener(v -> saveItem());
    }

    private void saveItem() {

        String name = etName.getText().toString().trim();
        String qtyStr = etQty.getText().toString().trim();
        String expiry = etExpiry.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();

        // ✅ VALIDATIONS
        if (TextUtils.isEmpty(name)) {
            etName.setError("Enter item name");
            return;
        }

        if (TextUtils.isEmpty(expiry)) {
            Toast.makeText(this, "Select expiry date", Toast.LENGTH_SHORT).show();
            return;
        }

        // ✅ FINAL FIX (String quantity)
        String qty = qtyStr;

        double price;

        try {
            price = TextUtils.isEmpty(priceStr) ? 0 : Double.parseDouble(priceStr);
        } catch (Exception e) {
            price = 0;
        }

        PantryItem item = new PantryItem(name, qty, expiry, price);

        // 🔥 SAVE TO ROOM DB (SAFE THREAD)
        new Thread(() -> {
            PantryDatabase.getInstance(getApplicationContext())
                    .pantryDao()
                    .insertItem(item);

            runOnUiThread(() -> {
                Toast.makeText(this, "Item Added ✅", Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }

    private void showDatePicker() {

        Calendar calendar = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, day) -> {

                    String date = String.format(Locale.getDefault(),
                            "%02d/%02d/%04d",
                            day, month + 1, year);

                    etExpiry.setText(date);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        dialog.show();
    }
}