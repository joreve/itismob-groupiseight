package com.itismob.s03.groupiseight.adapted.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.itismob.s03.groupiseight.adapted.R;
import com.itismob.s03.groupiseight.adapted.helpers.AuthManager;
import com.itismob.s03.groupiseight.adapted.helpers.ClearErrorTextWatcher;
import com.itismob.s03.groupiseight.adapted.helpers.InputValidator;
import com.itismob.s03.groupiseight.adapted.helpers.WindowInsetsHelper;

public class LoginActivity extends AppCompatActivity {

    private AuthManager authManager;
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;

    /** Receives the email back from RegisterActivity so it can be pre-filled here. */
    private final ActivityResultLauncher<Intent> registerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String email = result.getData().getStringExtra(RegisterActivity.EXTRA_REGISTERED_EMAIL);
                    emailInput.setText(email);
                    passwordInput.setText("");
                    passwordInput.requestFocus();
                    Snackbar.make(findViewById(R.id.login_root),
                            R.string.message_registration_success, Snackbar.LENGTH_LONG).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowInsetsHelper.enableEdgeToEdge(this);
        setContentView(R.layout.activity_login);
        WindowInsetsHelper.applySystemBarPadding(findViewById(R.id.login_root), true);

        authManager = new AuthManager(this);
        emailLayout = findViewById(R.id.login_email_layout);
        passwordLayout = findViewById(R.id.login_password_layout);
        emailInput = findViewById(R.id.login_email_input);
        passwordInput = findViewById(R.id.login_password_input);

        emailInput.addTextChangedListener(new ClearErrorTextWatcher(emailLayout));
        passwordInput.addTextChangedListener(new ClearErrorTextWatcher(passwordLayout));

        findViewById(R.id.login_button).setOnClickListener(v -> attemptLogin());
        findViewById(R.id.login_register_button).setOnClickListener(v ->
                registerLauncher.launch(new Intent(this, RegisterActivity.class)));
    }

    private void attemptLogin() {
        String email = textOf(emailInput);
        String password = passwordInput.getText() == null ? "" : passwordInput.getText().toString();
        boolean valid = true;

        if (email.isEmpty()) {
            emailLayout.setError(getString(R.string.error_email_required));
            valid = false;
        } else if (!InputValidator.isValidEmail(email)) {
            emailLayout.setError(getString(R.string.error_email_invalid));
            valid = false;
        }
        if (password.isEmpty()) {
            passwordLayout.setError(getString(R.string.error_password_required));
            valid = false;
        }
        if (!valid) {
            return;
        }

        if (authManager.login(email, password)) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            passwordLayout.setError(getString(R.string.error_invalid_credentials));
        }
    }

    private String textOf(TextInputEditText input) {
        return input.getText() == null ? "" : input.getText().toString().trim();
    }
}
