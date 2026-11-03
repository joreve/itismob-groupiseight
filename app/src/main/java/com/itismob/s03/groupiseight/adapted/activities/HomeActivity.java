package com.itismob.s03.groupiseight.adapted.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.itismob.s03.groupiseight.adapted.R;
import com.itismob.s03.groupiseight.adapted.adapters.MediaAdapter;
import com.itismob.s03.groupiseight.adapted.data.SampleMediaData;
import com.itismob.s03.groupiseight.adapted.helpers.AuthManager;
import com.itismob.s03.groupiseight.adapted.helpers.WindowInsetsHelper;
import com.itismob.s03.groupiseight.adapted.models.MediaCategory;
import com.itismob.s03.groupiseight.adapted.models.MediaItem;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowInsetsHelper.enableEdgeToEdge(this);
        setContentView(R.layout.activity_home);
        WindowInsetsHelper.applySystemBarPadding(findViewById(R.id.home_root), false);

        findViewById(R.id.home_logout_button).setOnClickListener(v -> logout());
        findViewById(R.id.home_search_bar).setOnClickListener(v -> openExplore(null, true));

        setupCategoryTile(R.id.tile_books, R.drawable.ic_category_book,
                R.string.category_books_plural, MediaCategory.BOOK);
        setupCategoryTile(R.id.tile_movies, R.drawable.ic_category_movie,
                R.string.category_movies_plural, MediaCategory.MOVIE);
        setupCategoryTile(R.id.tile_tv_series, R.drawable.ic_category_tv,
                R.string.category_tv_series, MediaCategory.TV_SERIES);
        setupCategoryTile(R.id.tile_music, R.drawable.ic_category_music,
                R.string.category_music, MediaCategory.MUSIC);

        setupSection(R.id.section_trending, R.id.list_trending,
                R.string.home_section_trending, SampleMediaData.getTrendingMedia());
        setupSection(R.id.section_trending_adaptations, R.id.list_trending_adaptations,
                R.string.home_section_trending_adaptations, SampleMediaData.getTrendingAdaptations());
        setupSection(R.id.section_recently_added, R.id.list_recently_added,
                R.string.home_section_recently_added, SampleMediaData.getRecentlyAdded());
        setupSection(R.id.section_recommended, R.id.list_recommended,
                R.string.home_section_recommended, SampleMediaData.getRecommendedMedia());

        setupBottomNavigation();
    }

    /** One of the four category shortcuts: opens Explore already filtered to that category. */
    private void setupCategoryTile(@IdRes int tileId, @DrawableRes int iconRes,
                                   @StringRes int labelRes, MediaCategory category) {
        View tile = findViewById(tileId);
        ((ImageView) tile.findViewById(R.id.tile_icon)).setImageResource(iconRes);
        ((TextView) tile.findViewById(R.id.tile_label)).setText(labelRes);
        tile.setOnClickListener(v -> openExplore(category, false));
    }

    /** One home section: a title with "See all" plus a horizontally scrolling row of media cards. */
    private void setupSection(@IdRes int headerId, @IdRes int listId, @StringRes int titleRes,
                              List<MediaItem> items) {
        View header = findViewById(headerId);
        ((TextView) header.findViewById(R.id.section_title)).setText(titleRes);
        header.findViewById(R.id.section_see_all).setOnClickListener(v -> openExplore(null, false));

        RecyclerView list = findViewById(listId);
        list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        list.setAdapter(new MediaAdapter(items, true));
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_home);
        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_explore) {
                openExplore(null, false);
                return false; // keep Home highlighted; Explore opens as its own screen
            }
            return true;
        });
    }

    /**
     * Opens Explore.
     *
     * @param category    category to pre-select, or null for All
     * @param focusSearch true to put the cursor in the search field straight away
     */
    private void openExplore(MediaCategory category, boolean focusSearch) {
        Intent intent = new Intent(this, ExploreActivity.class);
        if (category != null) {
            intent.putExtra(ExploreActivity.EXTRA_CATEGORY, category.name());
        }
        intent.putExtra(ExploreActivity.EXTRA_FOCUS_SEARCH, focusSearch);
        startActivity(intent);
    }

    private void logout() {
        new AuthManager(this).logout();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
