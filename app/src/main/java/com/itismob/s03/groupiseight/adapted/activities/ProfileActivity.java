package com.itismob.s03.groupiseight.adapted.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.itismob.s03.groupiseight.adapted.R;
import com.itismob.s03.groupiseight.adapted.adapters.MediaAdapter;
import com.itismob.s03.groupiseight.adapted.data.SampleMediaData;
import com.itismob.s03.groupiseight.adapted.helpers.AuthManager;
import com.itismob.s03.groupiseight.adapted.helpers.WindowInsetsHelper;
import com.itismob.s03.groupiseight.adapted.models.MediaItem;

import java.util.Arrays;
import java.util.List;

/**
 * Module J: User Profile.
 * Displays user profile details, activity statistics, favorite adaptations list,
 * account settings options, and logout action.
 */
public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowInsetsHelper.enableEdgeToEdge(this);
        setContentView(R.layout.activity_profile);
        WindowInsetsHelper.applySystemBarPadding(findViewById(R.id.profile_root), false);

        setupUserInfo();
        setupFavoritesList();
        setupAccountOptions();
        setupBottomNavigation();
    }

    private void setupUserInfo() {
        AuthManager authManager = new AuthManager(this);
        String email = authManager.getLoggedInEmail();

        TextView emailText = findViewById(R.id.profile_user_email);
        if (email != null && !email.isEmpty()) {
            emailText.setText(email);
        } else {
            emailText.setText(R.string.profile_default_email);
        }
    }

    private void setupFavoritesList() {
        RecyclerView favoritesRecyclerView = findViewById(R.id.profile_favorites_list);
        favoritesRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Favorite adaptations sample items
        List<MediaItem> favoriteMedia = Arrays.asList(
                SampleMediaData.getMediaById(1),  // The Hunger Games Book
                SampleMediaData.getMediaById(2),  // Dune Book
                SampleMediaData.getMediaById(20), // Parasite Movie
                SampleMediaData.getMediaById(31)  // Bohemian Rhapsody Music
        );

        favoritesRecyclerView.setAdapter(new MediaAdapter(favoriteMedia, true));
    }

    private void setupAccountOptions() {
        findViewById(R.id.option_edit_profile).setOnClickListener(v ->
                Toast.makeText(this, R.string.profile_edit_profile, Toast.LENGTH_SHORT).show());

        findViewById(R.id.option_preferences).setOnClickListener(v ->
                Toast.makeText(this, R.string.profile_preferences, Toast.LENGTH_SHORT).show());

        findViewById(R.id.option_help_support).setOnClickListener(v ->
                Toast.makeText(this, R.string.profile_help_support, Toast.LENGTH_SHORT).show());

        findViewById(R.id.profile_logout_button).setOnClickListener(v -> logout());
    }

    private void logout() {
        new AuthManager(this).logout();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_profile);
        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                Intent intent = new Intent(this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return false;
            } else if (item.getItemId() == R.id.nav_explore) {
                Intent intent = new Intent(this, ExploreActivity.class);
                startActivity(intent);
                return false;
            } else if (item.getItemId() == R.id.nav_my_library) {
                Intent intent = new Intent(this, MyLibraryActivity.class);
                startActivity(intent);
                return false;
            }
            return true;
        });
    }
}