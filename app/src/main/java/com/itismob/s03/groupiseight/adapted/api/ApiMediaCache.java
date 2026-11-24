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
    private static final Map<Integer, String> ID_TO_KEY = new ConcurrentHashMap<>();
    private static final Map<Integer, String> CREATORS = new ConcurrentHashMap<>();
    private static final Map<Integer, String> SOURCE_URLS = new ConcurrentHashMap<>();

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
        int id = raced != null ? raced : newId;
        ID_TO_KEY.put(id, key);
        return id;
    }

    /** The provider key ("MOVIE:603") an API id was created from, or null for non-API ids. */
    public static String keyFor(int id) {
        return ID_TO_KEY.get(id);
    }

    /** Director/creator already fetched for this id, or null if not fetched yet. */
    public static String getCreator(int id) {
        return CREATORS.get(id);
    }

    public static void putCreator(int id, String creator) {
        CREATORS.put(id, creator);
    }

    /** Provider page for an item (e.g. the Apple Music song URL), or null. Not shown in the UI yet. */
    public static String getSourceUrl(int id) {
        return SOURCE_URLS.get(id);
    }

    public static void putSourceUrl(int id, String url) {
        SOURCE_URLS.put(id, url);
    }

    public static void put(MediaItem item) {
        BY_ID.put(item.getId(), item);
    }

    /** Returns the cached API item, or null if the id is not an API item. */
    public static MediaItem get(int id) {
        return BY_ID.get(id);
    }
}
