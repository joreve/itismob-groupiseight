package com.itismob.s03.groupiseight.adapted.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.itismob.s03.groupiseight.adapted.R;
import com.itismob.s03.groupiseight.adapted.adapters.MediaAdapter;
import com.itismob.s03.groupiseight.adapted.api.MediaApiService;
import com.itismob.s03.groupiseight.adapted.data.SampleMediaData;
import com.itismob.s03.groupiseight.adapted.helpers.MediaSearchHelper;
import com.itismob.s03.groupiseight.adapted.helpers.WindowInsetsHelper;
import com.itismob.s03.groupiseight.adapted.models.MediaCategory;
import com.itismob.s03.groupiseight.adapted.models.MediaItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Explore / Media Library: browse every media entry, search it, and filter it by category.
 * Search text and the selected category are applied together.
 */
public class ExploreActivity extends AppCompatActivity {

    /** Optional extra: name of a MediaCategory to pre-select. Omit for "All". */
    public static final String EXTRA_CATEGORY = "extra_category";
    /** Optional extra (boolean): true to focus the search field and open the keyboard. */
    public static final String EXTRA_FOCUS_SEARCH = "extra_focus_search";

    private List<MediaItem> allMedia;
    private MediaAdapter adapter;

    private static final long SEARCH_DEBOUNCE_MS = 400; // while typing
    private static final long BROWSE_DEBOUNCE_MS = 150; // category change / reset: merges bursts of refreshes

    /** True once results (API or fallback) are on screen for {@link #shownCategory}. */
    private boolean shownAny = false;
    private MediaCategory shownCategory = null;
    private final Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable pendingSearch;
    private int searchRequestId = 0; // ignores responses from outdated searches

    private TextInputEditText searchInput;
    private ChipGroup categoryChips;
    private TextView resultCountText;
    private View resetButton;
    private RecyclerView resultsList;
    private View emptyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowInsetsHelper.enableEdgeToEdge(this);
        setContentView(R.layout.activity_explore);
        WindowInsetsHelper.applySystemBarPadding(findViewById(R.id.explore_root), false);

        allMedia = SampleMediaData.getAllMedia();

        searchInput = findViewById(R.id.explore_search_input);
        categoryChips = findViewById(R.id.explore_category_chips);
        resultCountText = findViewById(R.id.explore_result_count);
        resetButton = findViewById(R.id.explore_reset_button);
        resultsList = findViewById(R.id.explore_results);
        emptyState = findViewById(R.id.explore_empty_state);

        adapter = new MediaAdapter(new ArrayList<>(), false); // filled from the APIs; samples only on failure
        resultsList.setLayoutManager(new GridLayoutManager(this, 2));
        resultsList.setAdapter(adapter);

        setupSearch();
        setupCategoryChips();
        resetButton.setOnClickListener(v -> resetFilters());
        findViewById(R.id.explore_empty_reset_button).setOnClickListener(v -> resetFilters());
        setupBottomNavigation();

        applyIntentExtras();
        refreshResults();
    }

    private void setupSearch() {
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                refreshResults();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        searchInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard();
                return true;
            }
            return false;
        });
    }

    private void setupCategoryChips() {
        categoryChips.setOnCheckedStateChangeListener((group, checkedIds) -> refreshResults());
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_explore);
        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                // Go back to the existing Home screen instead of stacking a new one.
                Intent intent = new Intent(this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return false;
            } else if (item.getItemId() == R.id.nav_my_library) {
                Intent intent = new Intent(this, MyLibraryActivity.class);
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

    /** Applies the category / search focus requested by whoever opened this screen. */
    private void applyIntentExtras() {
        String categoryName = getIntent().getStringExtra(EXTRA_CATEGORY);
        if (categoryName != null) {
            MediaCategory category = MediaCategory.valueOf(categoryName);
            categoryChips.check(chipIdFor(category));
        }
        if (getIntent().getBooleanExtra(EXTRA_FOCUS_SEARCH, false)) {
            searchInput.post(() -> {
                searchInput.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.showSoftInput(searchInput, InputMethodManager.SHOW_IMPLICIT);
            });
        }
    }

    /**
     * Loads results from the APIs for the current search text and category (null = All):
     * - empty search box: popular media (Google Books / TMDB popular / Apple RSS PH music chart)
     * - text typed: matching media (Google Books / TMDB / iTunes Search for music)
     * Sample data is used ONLY if the request fails. A successful empty response shows the empty state.
     * Responses from outdated requests are ignored via {@link #searchRequestId}.
     */
    private void refreshResults() {
        String query = searchInput.getText() == null ? "" : searchInput.getText().toString().trim();
        MediaCategory category = getSelectedCategory();

        resetButton.setVisibility((!query.isEmpty() || category != null) ? View.VISIBLE : View.INVISIBLE);

        if (pendingSearch != null) {
            searchHandler.removeCallbacks(pendingSearch);
        }
        final int requestId = ++searchRequestId; // invalidates every earlier request

        // Different category (or nothing shown yet): clear the old list so it can't be mistaken for the new one.
        if (!shownAny || shownCategory != category) {
            shownAny = false;
            adapter.submitList(new ArrayList<>());
            resultsList.setVisibility(View.GONE);
            emptyState.setVisibility(View.GONE);
        }
        resultCountText.setText(R.string.explore_searching);

        MediaApiService.Callback callback = new MediaApiService.Callback() {
            @Override
            public void onSuccess(List<MediaItem> items) {
                if (requestId == searchRequestId && !isFinishing() && !isDestroyed()) {
                    showResults(items, category); // may be empty -> empty state, never samples
                }
            }

            @Override
            public void onError(Exception error) {
                if (requestId == searchRequestId && !isFinishing() && !isDestroyed()) {
                    Toast.makeText(ExploreActivity.this, R.string.explore_search_error, Toast.LENGTH_SHORT).show();
                    // Fallback only: the request failed, so show the local sample data.
                    showResults(MediaSearchHelper.filter(allMedia, query, category), category);
                }
            }
        };
        pendingSearch = query.isEmpty()
                ? () -> MediaApiService.browse(category, callback)
                : () -> MediaApiService.search(query, category, callback);
        searchHandler.postDelayed(pendingSearch, query.isEmpty() ? BROWSE_DEBOUNCE_MS : SEARCH_DEBOUNCE_MS);
    }

    /** Updates the list, count and empty state. */
    private void showResults(List<MediaItem> results, MediaCategory category) {
        shownAny = true;
        shownCategory = category;
        adapter.submitList(results);
        boolean hasResults = !results.isEmpty();
        resultsList.setVisibility(hasResults ? View.VISIBLE : View.GONE);
        emptyState.setVisibility(hasResults ? View.GONE : View.VISIBLE);
        resultCountText.setText(getResources().getQuantityString(
                R.plurals.explore_result_count, results.size(), results.size()));
    }

    /** Clears the search text and goes back to the "All" category. */
    private void resetFilters() {
        searchInput.setText("");
        categoryChips.check(R.id.chip_all);
        searchInput.clearFocus();
        hideKeyboard();
        refreshResults();
    }

    /** Returns the selected category, or null when "All" is selected. */
    private MediaCategory getSelectedCategory() {
        int checkedId = categoryChips.getCheckedChipId();
        if (checkedId == R.id.chip_books) {
            return MediaCategory.BOOK;
        } else if (checkedId == R.id.chip_movies) {
            return MediaCategory.MOVIE;
        } else if (checkedId == R.id.chip_tv_series) {
            return MediaCategory.TV_SERIES;
        } else if (checkedId == R.id.chip_music) {
            return MediaCategory.MUSIC;
        }
        return null;
    }

    private int chipIdFor(MediaCategory category) {
        switch (category) {
            case BOOK:
                return R.id.chip_books;
            case MOVIE:
                return R.id.chip_movies;
            case TV_SERIES:
                return R.id.chip_tv_series;
            case MUSIC:
                return R.id.chip_music;
            default:
                return R.id.chip_all;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        searchHandler.removeCallbacksAndMessages(null);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
    }
}
