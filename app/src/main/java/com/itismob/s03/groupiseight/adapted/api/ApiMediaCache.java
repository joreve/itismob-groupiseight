package com.itismob.s03.groupiseight.adapted.api;

import com.itismob.s03.groupiseight.adapted.models.MediaItem;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * In-memory registry of API results (never written to a database or the personal library).
 * Lets the existing screens that look items up by int id (details, rating, reviews) resolve
 * API items. Cleared automatically when the app process ends.
 */
public final class ApiMediaCache {

    /** Sample data uses ids 1..~35, so API ids start well above that. */
    private static final int FIRST_API_ID = 100_000;

    private static final AtomicInteger NEXT_ID = new AtomicInteger(FIRST_API_ID);
    private static final Map<String, Integer> KEY_TO_ID = new ConcurrentHashMap<>();
    private static final Map<Integer, MediaItem> BY_ID = new ConcurrentHashMap<>();

    private ApiMediaCache() {
    }

    /** Stable id for a provider item (e.g. "MOVIE:603") so repeated searches reuse the same id. */
    public static int idFor(String key) {
        Integer existing = KEY_TO_ID.get(key);
        if (existing != null) {
            return existing;
        }
        int newId = NEXT_ID.getAndIncrement();
        Integer raced = KEY_TO_ID.putIfAbsent(key, newId);
        return raced != null ? raced : newId;
    }

    public static void put(MediaItem item) {
        BY_ID.put(item.getId(), item);
    }

    /** Returns the cached API item, or null if the id is not an API item. */
    public static MediaItem get(int id) {
        return BY_ID.get(id);
    }
}
