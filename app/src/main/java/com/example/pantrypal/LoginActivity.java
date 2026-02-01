package com.example.pantrypal;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private GoogleSignInClient googleClient;

    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnLogin, btnGoogle;

    private final ActivityResultLauncher<Intent> googleLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

                // User pressed back / cancelled
                if (result.getResultCode() != RESULT_OK) {
                    Toast.makeText(this, "Google sign-in cancelled", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent data = result.getData();
                if (data == null) {
                    Toast.makeText(this, "Google sign-in cancelled", Toast.LENGTH_SHORT).show();
                    return;
                }

                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    if (account == null) {
                        Toast.makeText(this, "Google sign-in cancelled", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    firebaseAuthWithGoogle(account.getIdToken());

                } catch (ApiException e) {
                    Toast.makeText(this, "Google Sign-In failed: " + e.getStatusCode(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Google Sign-In error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1);

        auth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoogle = findViewById(R.id.btnGoogle);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleClient = GoogleSignIn.getClient(this, gso);

        btnLogin.setOnClickListener(v -> doEmailLogin());

        // Always show account chooser (good for testing / multi accounts)
        btnGoogle.setOnClickListener(v ->
                googleClient.signOut().addOnCompleteListener(t ->
                        googleLauncher.launch(googleClient.getSignInIntent())
                )
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Auto-login (best place)
        if (auth.getCurrentUser() != null) {
            goToHome();
        }
    }

    private void doEmailLogin() {
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String pass = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Email and Password required", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(email, pass)
                .addOnSuccessListener(res -> goToHome())
                .addOnFailureListener(err ->
                        Toast.makeText(this, "Login failed: " + err.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    private void firebaseAuthWithGoogle(String idToken) {
        if (idToken == null) {
            Toast.makeText(this, "Missing ID token. Check google-services.json / SHA-1", Toast.LENGTH_LONG).show();
            return;
        }

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnSuccessListener(res -> goToHome())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Firebase auth failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    private void goToHome() {
        Intent i = new Intent(this, MainActivity.class);
        // So user can't go back to login after successful login
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }
}
