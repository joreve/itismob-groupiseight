package com.itismob.s03.groupiseight.adapted.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.ChipGroup;
import com.itismob.s03.groupiseight.adapted.R;
import com.itismob.s03.groupiseight.adapted.adapters.MediaAdapter;
import com.itismob.s03.groupiseight.adapted.data.SampleMediaData;
import com.itismob.s03.groupiseight.adapted.helpers.WindowInsetsHelper;
import com.itismob.s03.groupiseight.adapted.models.MediaCategory;
import com.itismob.s03.groupiseight.adapted.models.MediaItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Module I: Personal Media Library & Tracking.
 * Keeps track of media items by tracking status (Currently Consuming, Completed, Plan to Consume, Dropped)
 * and filters by media type (Books, Movies, TV Series, Music).
 */
public class MyLibraryActivity extends AppCompatActivity {

    private MediaAdapter adapter;

    private TextView statTotalCount;
    private TextView statConsumingCount;
    private TextView statCompletedCount;
    private TextView statPlanCount;
    private TextView statDroppedCount;

    private ChipGroup statusChips;
    private ChipGroup typeChips;

    private TextView resultCountText;
    private RecyclerView resultsList;
    private View emptyState;

    private List<MediaItem> allTrackedItems;
    private List<MediaItem> currentlyConsumingItems;
    private List<MediaItem> completedItems;
    private List<MediaItem> planToConsumeItems;
    private List<MediaItem> droppedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowInsetsHelper.enableEdgeToEdge(this);
        setContentView(R.layout.activity_my_library);
        WindowInsetsHelper.applySystemBarPadding(findViewById(R.id.library_root), false);

        initSampleLibraryData();

        statTotalCount = findViewById(R.id.stat_total_count);
        statConsumingCount = findViewById(R.id.stat_consuming_count);
        statCompletedCount = findViewById(R.id.stat_completed_count);
        statPlanCount = findViewById(R.id.stat_plan_count);
        statDroppedCount = findViewById(R.id.stat_dropped_count);

        statusChips = findViewById(R.id.library_status_chips);
        typeChips = findViewById(R.id.library_type_chips);

        resultCountText = findViewById(R.id.library_result_count);
        resultsList = findViewById(R.id.library_results);
        emptyState = findViewById(R.id.library_empty_state);

        adapter = new MediaAdapter(allTrackedItems, false);
        resultsList.setLayoutManager(new GridLayoutManager(this, 2));
        resultsList.setAdapter(adapter);

        findViewById(R.id.library_empty_explore_button).setOnClickListener(v -> {
            Intent intent = new Intent(this, ExploreActivity.class);
            startActivity(intent);
        });

        statusChips.setOnCheckedStateChangeListener((group, checkedIds) -> refreshResults());
        typeChips.setOnCheckedStateChangeListener((group, checkedIds) -> refreshResults());

        updateStatsSummary();
        setupBottomNavigation();
        refreshResults();
    }

    private void initSampleLibraryData() {
        currentlyConsumingItems = Arrays.asList(
                SampleMediaData.getMediaById(2),  // Dune (Book)
                SampleMediaData.getMediaById(27), // Stranger Things (TV Series)
                SampleMediaData.getMediaById(28), // Blinding Lights (Music)
                SampleMediaData.getMediaById(35)  // Dune Soundtrack (Music)
        );

        completedItems = Arrays.asList(
                SampleMediaData.getMediaById(1),  // The Hunger Games (Book)
                SampleMediaData.getMediaById(8),  // A Game of Thrones (Book)
                SampleMediaData.getMediaById(13), // The Hunger Games (Movie)
                SampleMediaData.getMediaById(20), // Parasite (Movie)
                SampleMediaData.getMediaById(22), // Game of Thrones (TV Series)
                SampleMediaData.getMediaById(26), // Breaking Bad (TV Series)
                SampleMediaData.getMediaById(31)  // Bohemian Rhapsody (Music)
        );

        planToConsumeItems = Arrays.asList(
                SampleMediaData.getMediaById(6),  // Little Women (Book)
                SampleMediaData.getMediaById(21), // Inception (Movie)
                SampleMediaData.getMediaById(24), // The Witcher (TV Series)
                SampleMediaData.getMediaById(29)  // Flowers (Music)
        );

        droppedItems = Arrays.asList(
                SampleMediaData.getMediaById(7),  // Crazy Rich Asians (Book)
                SampleMediaData.getMediaById(25)  // Bridgerton (TV Series)
        );

        allTrackedItems = new ArrayList<>();
        allTrackedItems.addAll(currentlyConsumingItems);
        allTrackedItems.addAll(completedItems);
        allTrackedItems.addAll(planToConsumeItems);
        allTrackedItems.addAll(droppedItems);
    }

    private void updateStatsSummary() {
        statTotalCount.setText(String.valueOf(allTrackedItems.size()));
        statConsumingCount.setText(String.valueOf(currentlyConsumingItems.size()));
        statCompletedCount.setText(String.valueOf(completedItems.size()));
        statPlanCount.setText(String.valueOf(planToConsumeItems.size()));
        statDroppedCount.setText(String.valueOf(droppedItems.size()));
    }

    private void refreshResults() {
        List<MediaItem> statusFiltered = getSelectedStatusList();
        MediaCategory category = getSelectedCategory();

        List<MediaItem> filtered = new ArrayList<>();
        for (MediaItem item : statusFiltered) {
            if (category == null || item.getCategory() == category) {
                filtered.add(item);
            }
        }

        adapter.submitList(filtered);

        boolean hasItems = !filtered.isEmpty();
        resultsList.setVisibility(hasItems ? View.VISIBLE : View.GONE);
        emptyState.setVisibility(hasItems ? View.GONE : View.VISIBLE);

        resultCountText.setText(getResources().getQuantityString(
                R.plurals.library_result_count, filtered.size(), filtered.size()));
    }

    private List<MediaItem> getSelectedStatusList() {
        int checkedId = statusChips.getCheckedChipId();
        if (checkedId == R.id.chip_status_consuming) {
            return currentlyConsumingItems;
        } else if (checkedId == R.id.chip_status_completed) {
            return completedItems;
        } else if (checkedId == R.id.chip_status_plan) {
            return planToConsumeItems;
        } else if (checkedId == R.id.chip_status_dropped) {
            return droppedItems;
        }
        return allTrackedItems;
    }

    private MediaCategory getSelectedCategory() {
        int checkedId = typeChips.getCheckedChipId();
        if (checkedId == R.id.chip_type_books) {
            return MediaCategory.BOOK;
        } else if (checkedId == R.id.chip_type_movies) {
            return MediaCategory.MOVIE;
        } else if (checkedId == R.id.chip_type_tv_series) {
            return MediaCategory.TV_SERIES;
        } else if (checkedId == R.id.chip_type_music) {
            return MediaCategory.MUSIC;
        }
        return null; // All categories
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_my_library);
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
            } else if (item.getItemId() == R.id.nav_profile) {
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                return false;
            }
            return true;
        });
    }
}