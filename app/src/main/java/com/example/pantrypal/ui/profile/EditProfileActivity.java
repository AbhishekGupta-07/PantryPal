package com.example.pantrypal.ui.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pantrypal.R;
import com.example.pantrypal.utils.Prefs;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

public class EditProfileActivity extends AppCompatActivity {

    private ShapeableImageView imgAvatar;
    private EditText etName;
    private Uri selectedUri = null;

    // ✅ FIX: Use OpenDocument + Persist Permission (No crash on return)
    private final ActivityResultLauncher<String[]> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.OpenDocument(), uri -> {
                if (uri != null) {
                    try {
                        // ✅ Persist permission so you can load later in ProfileFragment
                        final int flags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                        getContentResolver().takePersistableUriPermission(uri, flags);

                        selectedUri = uri;
                        imgAvatar.setImageURI(uri);
                    } catch (Exception e) {
                        Toast.makeText(this, "Failed to load photo", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        imgAvatar = findViewById(R.id.imgEditAvatar);
        etName = findViewById(R.id.etEditName);

        MaterialButton btnPickPhoto = findViewById(R.id.btnPickPhoto);
        MaterialButton btnSave = findViewById(R.id.btnSaveProfile);

        // Load existing name
        String currentName = Prefs.getUserName(this, getString(R.string.profile_name_default));
        etName.setText(currentName);

        // Load existing avatar
        String avatar = Prefs.getAvatarUri(this);
        if (avatar != null) {
            try {
                imgAvatar.setImageURI(Uri.parse(avatar));
            } catch (Exception e) {
                imgAvatar.setImageResource(R.drawable.ic_profile_avatar);
            }
        } else {
            imgAvatar.setImageResource(R.drawable.ic_profile_avatar);
        }

        // ✅ FIX: launch OpenDocument
        btnPickPhoto.setOnClickListener(v -> pickImageLauncher.launch(new String[]{"image/*"}));

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            if (name.isEmpty()) {
                etName.setError("Enter name");
                return;
            }

            Prefs.setUserName(this, name);

            if (selectedUri != null) {
                Prefs.setAvatarUri(this, selectedUri.toString());
            }

            Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
