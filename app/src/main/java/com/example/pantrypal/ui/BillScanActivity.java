package com.example.pantrypal.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.pantrypal.R;
import com.example.pantrypal.AddItemActivity;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

public class BillScanActivity extends AppCompatActivity {

    ImageView imageView;
    Button btnCapture;
    TextView tvResult;

    private static final int CAMERA_REQUEST = 101;
    private static final int PERMISSION_CODE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_scan);

        imageView = findViewById(R.id.imageView);
        btnCapture = findViewById(R.id.btnCapture);
        tvResult = findViewById(R.id.tvResult);

        btnCapture.setOnClickListener(v -> checkPermission());
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    PERMISSION_CODE);
        }
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA_REQUEST);
        } else {
            Toast.makeText(this, "Camera not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {

            if (data != null && data.getExtras() != null) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(photo);

                runOCR(photo);

            } else {
                Toast.makeText(this, "Image capture failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 🔥 FINAL OCR METHOD (CLEAN + STABLE)
    private void runOCR(Bitmap bitmap) {

        tvResult.setText("Scanning...");

        InputImage image = InputImage.fromBitmap(bitmap, 0);

        TextRecognizer recognizer =
                TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        recognizer.process(image)
                .addOnSuccessListener(text -> {

                    String raw = text.getText();

                    if (raw == null || raw.trim().isEmpty()) {
                        tvResult.setText("No text found ❌");
                        return;
                    }

                    String[] lines = raw.split("\n");

                    String price = "";
                    String expiry = "";
                    String name = "";

                    for (String line : lines) {

                        String l = line.toLowerCase();

                        // 💰 PRICE (MRP)
                        if (price.isEmpty() && l.contains("mrp")) {
                            price = line.replaceAll("[^0-9.]", "");
                        }

                        // 📅 EXPIRY
                        if (expiry.isEmpty() &&
                                (l.contains("exp") || l.contains("use"))) {

                            expiry = line.replaceAll("[^0-9/]", "");
                        }

                        // 🧾 NAME
                        if (name.isEmpty() &&
                                line.length() > 5 &&
                                !line.matches(".*\\d.*") &&
                                !l.contains("mrp") &&
                                !l.contains("exp")) {

                            name = line.trim();
                        }
                    }

                    // 🔁 fallback price
                    if (price.isEmpty()) {
                        price = raw.replaceAll(".*?(\\d{1,3}\\.\\d{1,2}).*", "$1");
                    }

                    // 🔁 fallback expiry
                    if (expiry.isEmpty()) {
                        expiry = raw.replaceAll(".*?(\\d{2}/\\d{2}/\\d{2,4}).*", "$1");
                    }

                    if (name.isEmpty()) {
                        name = "Item";
                    }

                    tvResult.setText(
                            "Item: " + name + "\n" +
                                    "Price: ₹" + price + "\n" +
                                    "Expiry: " + expiry
                    );

                    // 🚀 OPEN ADD SCREEN
                    Intent intent = new Intent(BillScanActivity.this, AddItemActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("price", price);
                    intent.putExtra("expiry", expiry);

                    startActivity(intent);

                })
                .addOnFailureListener(e -> {
                    tvResult.setText("Scan Failed ❌");
                });
    }
}