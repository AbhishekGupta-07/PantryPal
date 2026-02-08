package com.example.pantrypal.ui.profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.pantrypal.LoginActivity;
import com.example.pantrypal.PantryDao;
import com.example.pantrypal.PantryDatabase;
import com.example.pantrypal.R;
import com.example.pantrypal.utils.Prefs;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.materialswitch.MaterialSwitch;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

// ✅ ADD THIS
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment implements ExpiryReminderBottomSheet.OnReminderSelectedListener {

    private MaterialSwitch switchDarkMode, switchNotifications;
    private TextView tvExpiryValue, tvUserName, tvUserEmail, tvAppVersion;
    private ShapeableImageView imgAvatar;

    public ProfileFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        // Header
        imgAvatar = v.findViewById(R.id.imgAvatar);
        tvUserName = v.findViewById(R.id.tvUserName);
        tvUserEmail = v.findViewById(R.id.tvUserEmail);

        // Prefs
        switchDarkMode = v.findViewById(R.id.switchDarkMode);
        switchNotifications = v.findViewById(R.id.switchNotifications);
        tvExpiryValue = v.findViewById(R.id.tvExpiryValue);

        // Footer
        tvAppVersion = v.findViewById(R.id.tvAppVersion);

        // Clickables
        v.findViewById(R.id.rowEditProfile).setOnClickListener(view ->
                startActivity(new Intent(requireContext(), EditProfileActivity.class))
        );

        v.findViewById(R.id.rowExpiryReminder).setOnClickListener(view -> {
            ExpiryReminderBottomSheet sheet =
                    ExpiryReminderBottomSheet.newInstance(Prefs.getExpiryDays(requireContext()));
            sheet.setTargetFragment(this, 0);
            sheet.show(getParentFragmentManager(), "expiry_sheet");
        });

        v.findViewById(R.id.rowClearPantry).setOnClickListener(view -> confirmClearPantry());
        v.findViewById(R.id.rowLogout).setOnClickListener(view -> confirmLogout());
        v.findViewById(R.id.rowAbout).setOnClickListener(view -> showAbout());

        bindUser();
        bindPrefs();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        bindUser();
    }

    private void bindUser() {
        Context c = requireContext();

        String name = Prefs.getUserName(c, getString(R.string.profile_name_default));
        tvUserName.setText(name);

        tvUserEmail.setText(getString(R.string.profile_guest_mode));

        String avatar = Prefs.getAvatarUri(c);
        if (avatar != null && !avatar.trim().isEmpty()) {
            try {
                imgAvatar.setImageURI(Uri.parse(avatar));
            } catch (Exception e) {
                imgAvatar.setImageResource(R.drawable.ic_profile_avatar);
            }
        } else {
            imgAvatar.setImageResource(R.drawable.ic_profile_avatar);
        }

        tvAppVersion.setText(getString(R.string.app_version_format, "1.0"));
    }

    private void bindPrefs() {
        Context c = requireContext();

        boolean isDark = Prefs.isDarkMode(c);
        boolean notif = Prefs.isNotificationsEnabled(c);
        int days = Prefs.getExpiryDays(c);

        switchDarkMode.setChecked(isDark);
        switchNotifications.setChecked(notif);
        tvExpiryValue.setText(getString(R.string.days_before_format, days));

        switchDarkMode.setOnCheckedChangeListener((buttonView, checked) -> {
            Prefs.setDarkMode(c, checked);
            AppCompatDelegate.setDefaultNightMode(
                    checked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );
        });

        switchNotifications.setOnCheckedChangeListener((buttonView, checked) -> {
            Prefs.setNotificationsEnabled(c, checked);
            Toast.makeText(c, checked ? "Notifications ON" : "Notifications OFF", Toast.LENGTH_SHORT).show();
        });
    }

    private void confirmClearPantry() {
        new AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.clear_pantry_title))
                .setMessage(getString(R.string.clear_pantry_msg))
                .setNegativeButton(getString(R.string.cancel), null)
                .setPositiveButton(getString(R.string.delete), (d, which) -> clearPantryNow())
                .show();
    }

    private void clearPantryNow() {
        new Thread(() -> {
            PantryDao dao = PantryDatabase.getInstance(requireContext()).pantryDao();
            dao.deleteAll();

            requireActivity().runOnUiThread(() ->
                    Toast.makeText(requireContext(), "Pantry cleared", Toast.LENGTH_SHORT).show()
            );
        }).start();
    }

    private void confirmLogout() {
        new AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.logout_title))
                .setMessage(getString(R.string.logout_msg))
                .setNegativeButton(getString(R.string.cancel), null)
                .setPositiveButton(getString(R.string.logout), (d, which) -> doLogout())
                .show();
    }

    private void doLogout() {
        // ✅ IMPORTANT: Firebase sign out (otherwise LoginActivity auto-goes Home)
        FirebaseAuth.getInstance().signOut();

        // ✅ clear local profile + login flag
        Prefs.clearUser(requireContext());

        // Google sign-out (safe even if not logged-in)
        try {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build();
            GoogleSignInClient client = GoogleSignIn.getClient(requireContext(), gso);
            client.signOut().addOnCompleteListener(task -> goToLogin());
        } catch (Exception e) {
            goToLogin();
        }
    }

    private void goToLogin() {
        Intent i = new Intent(requireContext(), LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);

        if (getActivity() != null) getActivity().finish();
    }

    private void showAbout() {
        new AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.about_pantry_pal))
                .setMessage("PantryPal helps you track pantry items, expiry status, and smart reminders.\n\nVersion: 1.0")
                .setPositiveButton(getString(R.string.ok), null)
                .show();
    }

    @Override
    public void onReminderSelected(int days) {
        Prefs.setExpiryDays(requireContext(), days);
        tvExpiryValue.setText(getString(R.string.days_before_format, days));
        Toast.makeText(requireContext(), "Expiry reminder set: " + days + " days before", Toast.LENGTH_SHORT).show();
    }
}
