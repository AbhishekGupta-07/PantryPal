package com.example.pantrypal.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.pantrypal.R;
import com.example.pantrypal.AddItemActivity;

public class AddItemOptionsActivity extends AppCompatActivity {

    CardView manualEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_options);

        manualEntry = findViewById(R.id.manualEntry);

        // ✍️ Manual Entry only
        manualEntry.setOnClickListener(v ->
                startActivity(new Intent(this, AddItemActivity.class))
        );
    }
}