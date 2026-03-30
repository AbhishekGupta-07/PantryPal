package com.example.pantrypal.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.pantrypal.R;
import com.example.pantrypal.AddItemActivity;

public class AddItemOptionsActivity extends AppCompatActivity {

    CardView scanBill, scanBarcode, manualEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_options);

        scanBill = findViewById(R.id.scanBill);
        scanBarcode = findViewById(R.id.scanBarcode);
        manualEntry = findViewById(R.id.manualEntry);

        // 🔥 FIX: BillScanActivity open karega
        scanBill.setOnClickListener(v ->
                startActivity(new Intent(this, BillScanActivity.class))
        );

        // 🔍 Barcode (abhi placeholder)
        scanBarcode.setOnClickListener(v ->
                Toast.makeText(this, "Scan Barcode Clicked", Toast.LENGTH_SHORT).show()
        );

        // ✍️ Manual Entry
        manualEntry.setOnClickListener(v ->
                startActivity(new Intent(this, AddItemActivity.class))
        );
    }
}