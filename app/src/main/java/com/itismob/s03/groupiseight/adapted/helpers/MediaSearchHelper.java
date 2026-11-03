package com.itismob.s03.groupiseight.adapted.helpers;

import com.itismob.s03.groupiseight.adapted.models.MediaCategory;
import com.itismob.s03.groupiseight.adapted.models.MediaItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Search + category filtering used by the Explore screen. Contains no Android code. */
public final class MediaSearchHelper {

    private MediaSearchHelper() {
    }

    /**
     * Returns the items that match BOTH the search query and the category.
     *
     * @param query    text typed by the user; null or blank means "no search"
     * @param category the selected category; null means "All"
     */
    public static List<MediaItem> filter(List<MediaItem> items, String query, MediaCategory category) {
        String cleanQuery = query == null ? "" : query.trim().toLowerCase(Locale.ROOT);
        List<MediaItem> results = new ArrayList<>();
        for (MediaItem item : items) {
            boolean categoryMatches = category == null || item.getCategory() == category;
            if (categoryMatches && matchesQuery(item, cleanQuery)) {
                results.add(item);
            }
        }
        return results;
    }

    /** Checks title, creator (author/director/artist), album and publisher. */
    private static boolean matchesQuery(MediaItem item, String lowerCaseQuery) {
        if (lowerCaseQuery.isEmpty()) {
            return true;
        }
        return contains(item.getTitle(), lowerCaseQuery)
                || contains(item.getCreator(), lowerCaseQuery)
                || contains(item.getAlbum(), lowerCaseQuery)
                || contains(item.getPublisher(), lowerCaseQuery);
    }

    private static boolean contains(String field, String lowerCaseQuery) {
        return field != null && field.toLowerCase(Locale.ROOT).contains(lowerCaseQuery);
    }
}
