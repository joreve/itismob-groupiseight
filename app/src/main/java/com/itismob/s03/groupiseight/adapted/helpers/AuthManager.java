package com.itismob.s03.groupiseight.adapted.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

/**
 * Simple local authentication backed by SharedPreferences (prototype only - no backend).
 *
 * Accounts are stored on the device, keyed by email. Passwords are stored as a SHA-256 hash
 * instead of plain text. Replace this class when real online authentication is added.
 */
public class AuthManager {

    private static final String PREFS_NAME = "adapted_auth_prefs";
    private static final String KEY_SESSION_EMAIL = "session_email";
    private static final String KEY_PASSWORD_HASH_PREFIX = "password_hash_";
    private static final String KEY_BIRTHDAY_PREFIX = "birthday_";

    private final SharedPreferences prefs;

    public AuthManager(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    /** Creates an account. Returns false if an account with this email already exists. */
    public boolean register(String email, String birthday, String password) {
        String normalizedEmail = normalizeEmail(email);
        if (accountExists(normalizedEmail)) {
            return false;
        }
        prefs.edit()
                .putString(KEY_PASSWORD_HASH_PREFIX + normalizedEmail, hashPassword(password))
                .putString(KEY_BIRTHDAY_PREFIX + normalizedEmail, birthday)
                .apply();
        return true;
    }

    /** Checks the credentials and, if they are correct, starts a session. */
    public boolean login(String email, String password) {
        String normalizedEmail = normalizeEmail(email);
        String savedHash = prefs.getString(KEY_PASSWORD_HASH_PREFIX + normalizedEmail, null);
        if (savedHash == null || !savedHash.equals(hashPassword(password))) {
            return false;
        }
        prefs.edit().putString(KEY_SESSION_EMAIL, normalizedEmail).apply();
        return true;
    }

    public boolean isLoggedIn() {
        String sessionEmail = prefs.getString(KEY_SESSION_EMAIL, null);
        return sessionEmail != null && accountExists(sessionEmail);
    }

    /** Email of the logged-in user, or null if nobody is logged in. */
    public String getLoggedInEmail() {
        return isLoggedIn() ? prefs.getString(KEY_SESSION_EMAIL, null) : null;
    }

    public void logout() {
        prefs.edit().remove(KEY_SESSION_EMAIL).apply();
    }

    private boolean accountExists(String normalizedEmail) {
        return prefs.contains(KEY_PASSWORD_HASH_PREFIX + normalizedEmail);
    }

    private static String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
    }

    private static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is not available", e);
        }
    }
}
