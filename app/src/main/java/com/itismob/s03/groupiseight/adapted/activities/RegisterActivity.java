package com.itismob.s03.groupiseight.adapted.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.itismob.s03.groupiseight.adapted.R;
import com.itismob.s03.groupiseight.adapted.helpers.AuthManager;
import com.itismob.s03.groupiseight.adapted.helpers.ClearErrorTextWatcher;
import com.itismob.s03.groupiseight.adapted.helpers.InputValidator;
import com.itismob.s03.groupiseight.adapted.helpers.WindowInsetsHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class RegisterActivity extends AppCompatActivity {

    /** Result extra: the email that was just registered (read by LoginActivity). */
    public static final String EXTRA_REGISTERED_EMAIL = "registered_email";

    private AuthManager authManager;
    private TextInputLayout emailLayout;
    private TextInputLayout birthdayLayout;
    private TextInputLayout passwordLayout;
    private TextInputLayout confirmPasswordLayout;
    private TextInputEditText emailInput;
    private TextInputEditText birthdayInput;
    private TextInputEditText passwordInput;
    private TextInputEditText confirmPasswordInput;

    /** Birthday chosen in the date picker, in yyyy-MM-dd format (null until chosen). */
    private String selectedBirthday;
    private Long selectedBirthdayMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowInsetsHelper.enableEdgeToEdge(this);
        setContentView(R.layout.activity_register);
        WindowInsetsHelper.applySystemBarPadding(findViewById(R.id.register_root), true);

        authManager = new AuthManager(this);
        emailLayout = findViewById(R.id.register_email_layout);
        birthdayLayout = findViewById(R.id.register_birthday_layout);
        passwordLayout = findViewById(R.id.register_password_layout);
        confirmPasswordLayout = findViewById(R.id.register_confirm_password_layout);
        emailInput = findViewById(R.id.register_email_input);
        birthdayInput = findViewById(R.id.register_birthday_input);
        passwordInput = findViewById(R.id.register_password_input);
        confirmPasswordInput = findViewById(R.id.register_confirm_password_input);

        emailInput.addTextChangedListener(new ClearErrorTextWatcher(emailLayout));
        passwordInput.addTextChangedListener(new ClearErrorTextWatcher(passwordLayout));
        confirmPasswordInput.addTextChangedListener(new ClearErrorTextWatcher(confirmPasswordLayout));

        birthdayInput.setOnClickListener(v -> showBirthdayPicker());
        birthdayLayout.setEndIconOnClickListener(v -> showBirthdayPicker());

        findViewById(R.id.register_button).setOnClickListener(v -> attemptRegister());
        findViewById(R.id.register_login_button).setOnClickListener(v -> finish());
    }

    private void showBirthdayPicker() {
        CalendarConstraints constraints = new CalendarConstraints.Builder()
                .setValidator(DateValidatorPointBackward.now())
                .build();
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.register_birthday_picker_title)
                .setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
                .setCalendarConstraints(constraints);
        if (selectedBirthdayMillis != null) {
            builder.setSelection(selectedBirthdayMillis);
        }

        MaterialDatePicker<Long> picker = builder.build();
        picker.addOnPositiveButtonClickListener(selection -> {
            selectedBirthdayMillis = selection;
            Date date = new Date(selection);
            // The picker returns UTC midnight, so format in UTC to keep the same calendar day.
            selectedBirthday = formatUtc(date, InputValidator.BIRTHDAY_FORMAT);
            birthdayInput.setText(formatUtc(date, "MMM d, yyyy"));
            birthdayLayout.setError(null);
        });
        picker.show(getSupportFragmentManager(), "birthday_picker");
    }

    private static String formatUtc(Date date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format.format(date);
    }

    private void attemptRegister() {
        String email = emailInput.getText() == null ? "" : emailInput.getText().toString().trim();
        String password = passwordInput.getText() == null ? "" : passwordInput.getText().toString();
        String confirmPassword = confirmPasswordInput.getText() == null
                ? "" : confirmPasswordInput.getText().toString();
        boolean valid = true;

        if (email.isEmpty()) {
            emailLayout.setError(getString(R.string.error_email_required));
            valid = false;
        } else if (!InputValidator.isValidEmail(email)) {
            emailLayout.setError(getString(R.string.error_email_invalid));
            valid = false;
        }

        if (selectedBirthday == null) {
            birthdayLayout.setError(getString(R.string.error_birthday_required));
            valid = false;
        } else if (!InputValidator.isValidBirthday(selectedBirthday)) {
            birthdayLayout.setError(getString(R.string.error_birthday_invalid));
            valid = false;
        }

        if (password.isEmpty()) {
            passwordLayout.setError(getString(R.string.error_password_required));
            valid = false;
        } else if (!InputValidator.isValidPassword(password)) {
            passwordLayout.setError(getString(R.string.error_password_too_short,
                    InputValidator.MIN_PASSWORD_LENGTH));
            valid = false;
        }

        if (confirmPassword.isEmpty()) {
            confirmPasswordLayout.setError(getString(R.string.error_confirm_password_required));
            valid = false;
        } else if (!confirmPassword.equals(password)) {
            confirmPasswordLayout.setError(getString(R.string.error_passwords_do_not_match));
            valid = false;
        }

        if (!valid) {
            return;
        }

        if (!authManager.register(email, selectedBirthday, password)) {
            emailLayout.setError(getString(R.string.error_email_taken));
            return;
        }

        // Hand the email back to Login so the user can log in right away.
        Intent result = new Intent().putExtra(EXTRA_REGISTERED_EMAIL, email);
        setResult(RESULT_OK, result);
        finish();
    }
}
