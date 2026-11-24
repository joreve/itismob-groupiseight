package com.itismob.s03.groupiseight.adapted.api;

import android.os.Handler;
import android.os.Looper;

import com.itismob.s03.groupiseight.adapted.BuildConfig;
import com.itismob.s03.groupiseight.adapted.models.MediaCategory;
import com.itismob.s03.groupiseight.adapted.models.MediaItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Fetches media from Google Books (books), TMDB (movies, TV) and iTunes (music).
 * Uses plain HttpURLConnection + org.json (no extra dependencies). All callbacks run on the main thread.
 * Results are temporary: they are only registered in {@link ApiMediaCache}, never persisted.
 */
public final class MediaApiService {

    public interface Callback {
        void onSuccess(List<MediaItem> items);

        void onError(Exception error);
    }

    private interface Task {
        List<MediaItem> run() throws Exception;
    }

    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(4);
    private static final Handler MAIN = new Handler(Looper.getMainLooper());

    private static final String TMDB_BASE = "https://api.themoviedb.org/3";
    private static final String TMDB_IMAGE = "https://image.tmdb.org/t/p/w342";

    private MediaApiService() {
    }

    // ------------------------------------------------------------------ public API

    /** Searches one category, or all four when category is null. */
    public static void search(String query, MediaCategory category, Callback callback) {
        List<Task> tasks = new ArrayList<>();
        if (category == null || category == MediaCategory.BOOK) {
            tasks.add(() -> booksSearch(query));
        }
        if (category == null || category == MediaCategory.MOVIE) {
            tasks.add(() -> tmdb("/search/movie", "&query=" + enc(query), MediaCategory.MOVIE));
        }
        if (category == null || category == MediaCategory.TV_SERIES) {
            tasks.add(() -> tmdb("/search/tv", "&query=" + enc(query), MediaCategory.TV_SERIES));
        }
        if (category == null || category == MediaCategory.MUSIC) {
            tasks.add(() -> itunes(query));
        }
        runAll(tasks, callback);
    }

    /** Popular items for one category, for the Home screen sections. */
    public static void getPopular(MediaCategory category, Callback callback) {
        List<Task> tasks = new ArrayList<>();
        switch (category) {
            case BOOK:
                tasks.add(() -> booksSearch("subject:fiction"));
                break;
            case MOVIE:
                tasks.add(() -> tmdb("/trending/movie/week", "", MediaCategory.MOVIE));
                break;
            case TV_SERIES:
                tasks.add(() -> tmdb("/trending/tv/week", "", MediaCategory.TV_SERIES));
                break;
            default:
                tasks.add(() -> itunes("top hits"));
                break;
        }
        runAll(tasks, callback);
    }

    // ------------------------------------------------------------------ task runner

    /** Runs the tasks in parallel. Succeeds if at least one provider answers; errors only if all fail. */
    private static void runAll(List<Task> tasks, Callback callback) {
        final int total = tasks.size();
        final List<List<MediaItem>> results = new ArrayList<>();
        final Exception[] lastError = new Exception[1];
        final int[] done = {0};
        final int[] failed = {0};

        for (Task task : tasks) {
            EXECUTOR.execute(() -> {
                List<MediaItem> list = null;
                Exception error = null;
                try {
                    list = task.run();
                } catch (Exception e) {
                    error = e;
                }
                final List<MediaItem> finalList = list;
                final Exception finalError = error;
                MAIN.post(() -> {
                    done[0]++;
                    if (finalError != null) {
                        failed[0]++;
                        lastError[0] = finalError;
                    } else {
                        results.add(finalList);
                    }
                    if (done[0] == total) {
                        if (failed[0] == total) {
                            callback.onError(lastError[0]);
                        } else {
                            callback.onSuccess(interleave(results));
                        }
                    }
                });
            });
        }
    }

    /** Round-robin merge so "All" shows a mix of categories instead of 20 books first. */
    private static List<MediaItem> interleave(List<List<MediaItem>> lists) {
        List<MediaItem> merged = new ArrayList<>();
        int max = 0;
        for (List<MediaItem> l : lists) {
            max = Math.max(max, l.size());
        }
        for (int i = 0; i < max; i++) {
            for (List<MediaItem> l : lists) {
                if (i < l.size()) {
                    merged.add(l.get(i));
                }
            }
        }
        return merged;
    }

    // ------------------------------------------------------------------ Google Books

    private static List<MediaItem> booksSearch(String query) throws Exception {
        return GoogleBooksClient.search(query);
    }

    // ------------------------------------------------------------------ TMDB

    private static List<MediaItem> tmdb(String path, String extraParams, MediaCategory category)
            throws Exception {
        if (BuildConfig.TMDB_API_KEY.isEmpty()) {
            throw new IllegalStateException("TMDB_API_KEY missing in local.properties");
        }
        String url = TMDB_BASE + path + "?api_key=" + BuildConfig.TMDB_API_KEY
                + "&include_adult=false" + extraParams;
        JSONArray arr = new JSONObject(get(url)).optJSONArray("results");
        List<MediaItem> out = new ArrayList<>();
        if (arr == null) {
            return out;
        }
        boolean movie = category == MediaCategory.MOVIE;
        for (int i = 0; i < arr.length() && out.size() < 20; i++) {
            JSONObject o = arr.getJSONObject(i);
            String poster = o.optString("poster_path", "");
            String cover = poster.isEmpty() || poster.equals("null") ? null : TMDB_IMAGE + poster;
            String title = o.optString(movie ? "title" : "name", "Untitled");
            String date = o.optString(movie ? "release_date" : "first_air_date");
            // Search results don't include director/creator: leave it null (pending) unless we already
            // fetched it earlier; MediaAdapter then calls loadCreator() lazily for visible cards.
            String key = category.name() + ":" + o.getInt("id");
            out.add(build(key, title, ApiMediaCache.getCreator(ApiMediaCache.idFor(key)),
                    category, year(date), (float) (o.optDouble("vote_average", 0) / 2.0), null, null, cover));
        }
        return out;
    }

    // ------------------------------------------------------------------ TMDB director / creator (lazy)

    public static final String DIRECTOR_UNAVAILABLE = "Director unavailable";
    public static final String CREATOR_UNAVAILABLE = "Creator unavailable";

    public interface CreatorCallback {
        /** Main thread. Either the name(s), or "Director unavailable" / "Creator unavailable". */
        void onCreatorLoaded(String creator);
    }

    /** Callbacks waiting for an in-flight lookup, so scrolling never fires duplicate requests. Main thread only. */
    private static final java.util.Map<Integer, List<CreatorCallback>> PENDING_CREATORS = new java.util.HashMap<>();

    /**
     * Fetches the director (movies: /movie/{id}/credits) or creator (TV: /tv/{id} created_by) for one
     * TMDB item. One request per item, cached in {@link ApiMediaCache}; call from the main thread.
     */
    public static void loadCreator(MediaItem item, CreatorCallback callback) {
        MediaCategory category = item.getCategory();
        String key = ApiMediaCache.keyFor(item.getId());
        if (key == null || (category != MediaCategory.MOVIE && category != MediaCategory.TV_SERIES)) {
            return;
        }
        final int id = item.getId();
        final boolean movie = category == MediaCategory.MOVIE;
        final String unavailable = movie ? DIRECTOR_UNAVAILABLE : CREATOR_UNAVAILABLE;

        String cached = ApiMediaCache.getCreator(id);
        if (cached != null) {
            callback.onCreatorLoaded(cached);
            return;
        }
        List<CreatorCallback> waiting = PENDING_CREATORS.get(id);
        if (waiting != null) {
            waiting.add(callback);
            return;
        }
        waiting = new ArrayList<>();
        waiting.add(callback);
        PENDING_CREATORS.put(id, waiting);

        final String tmdbId = key.substring(key.indexOf(':') + 1);
        EXECUTOR.execute(() -> {
            String names = null; // null = request failed
            try {
                if (BuildConfig.TMDB_API_KEY.isEmpty()) {
                    throw new IllegalStateException("TMDB_API_KEY missing in local.properties");
                }
                String url = movie
                        ? TMDB_BASE + "/movie/" + tmdbId + "/credits?api_key=" + BuildConfig.TMDB_API_KEY
                        : TMDB_BASE + "/tv/" + tmdbId + "?api_key=" + BuildConfig.TMDB_API_KEY;
                String body = get(url);
                names = movie ? directorsFrom(body) : creatorsFrom(body);
            } catch (Exception ignored) {
                // falls through to "unavailable"; not cached, so a later search can retry
            }
            final String result = names == null ? unavailable : (names.isEmpty() ? unavailable : names);
            final boolean cacheIt = names != null;
            MAIN.post(() -> {
                if (cacheIt) {
                    ApiMediaCache.putCreator(id, result);
                }
                List<CreatorCallback> done = PENDING_CREATORS.remove(id);
                if (done != null) {
                    for (CreatorCallback cb : done) {
                        cb.onCreatorLoaded(result);
                    }
                }
            });
        });
    }

    /** "Director" crew entries from a /movie/{id}/credits response, comma-separated (max 3); "" if none. */
    static String directorsFrom(String json) throws Exception {
        JSONArray crew = new JSONObject(json).optJSONArray("crew");
        List<String> names = new ArrayList<>();
        if (crew != null) {
            for (int i = 0; i < crew.length() && names.size() < 3; i++) {
                JSONObject c = crew.getJSONObject(i);
                String name = c.optString("name", "");
                if ("Director".equals(c.optString("job")) && !name.isEmpty() && !names.contains(name)) {
                    names.add(name);
                }
            }
        }
        return join(names);
    }

    /** created_by names from a /tv/{id} response, comma-separated (max 3); "" if none. */
    static String creatorsFrom(String json) throws Exception {
        JSONArray createdBy = new JSONObject(json).optJSONArray("created_by");
        List<String> names = new ArrayList<>();
        if (createdBy != null) {
            for (int i = 0; i < createdBy.length() && names.size() < 3; i++) {
                String name = createdBy.getJSONObject(i).optString("name", "");
                if (!name.isEmpty() && !names.contains(name)) {
                    names.add(name);
                }
            }
        }
        return join(names);
    }

    private static String join(List<String> names) {
        StringBuilder sb = new StringBuilder();
        for (String n : names) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(n);
        }
        return sb.toString();
    }

    // ------------------------------------------------------------------ iTunes

    private static List<MediaItem> itunes(String term) throws Exception {
        String url = "https://itunes.apple.com/search?media=music&entity=song&limit=20&term=" + enc(term);
        JSONArray arr = new JSONObject(get(url)).optJSONArray("results");
        List<MediaItem> out = new ArrayList<>();
        if (arr == null) {
            return out;
        }
        for (int i = 0; i < arr.length(); i++) {
            JSONObject o = arr.getJSONObject(i);
            if (!o.has("trackId")) {
                continue;
            }
            String art = o.optString("artworkUrl100", null);
            if (art != null) {
                art = art.replace("100x100bb", "400x400bb");
            }
            out.add(build("MUSIC:" + o.getLong("trackId"), o.optString("trackName", "Untitled"),
                    o.optString("artistName", "Unknown artist"), MediaCategory.MUSIC,
                    year(o.optString("releaseDate")), 0f, null, o.optString("collectionName", null), art));
        }
        return out;
    }

    // ------------------------------------------------------------------ helpers

    private static MediaItem build(String key, String title, String creator, MediaCategory category,
                                   int year, float rating, String publisher, String album, String coverUrl) {
        MediaItem item = new MediaItem(ApiMediaCache.idFor(key), title, creator, category, year, rating,
                publisher, album, null, null, 0, coverUrl);
        ApiMediaCache.put(item);
        return item;
    }

    /** First 4 digits of "2008-09-14", "2008" or "2020-03-20T12:00:00Z"; 0 if absent. */
    private static int year(String date) {
        if (date == null || date.length() < 4) {
            return 0;
        }
        try {
            return Integer.parseInt(date.substring(0, 4));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static String enc(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (java.io.UnsupportedEncodingException e) {
            return s;
        }
    }

    private static String get(String urlString) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(urlString).openConnection();
        try {
            conn.setConnectTimeout(10_000);
            conn.setReadTimeout(10_000);
            conn.setRequestProperty("Accept", "application/json");
            int code = conn.getResponseCode();
            if (code != HttpURLConnection.HTTP_OK) {
                throw new java.io.IOException("HTTP " + code);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                return sb.toString();
            }
        } finally {
            conn.disconnect();
        }
    }
}
