package com.itismob.s03.groupiseight.adapted.api;

import android.util.Log;

import com.itismob.s03.groupiseight.adapted.BuildConfig;
import com.itismob.s03.groupiseight.adapted.models.MediaCategory;
import com.itismob.s03.groupiseight.adapted.models.MediaItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Google Books search. Everything is logged under the "GoogleBooks" Logcat tag:
 * request URL (the API key is never logged), HTTP code, response body / error body, result count.
 * Must be called off the main thread (MediaApiService does that).
 */
public final class GoogleBooksClient {

    static final String TAG = "GoogleBooks";
    private static final String BASE = "https://www.googleapis.com/books/v1/volumes";
    private static final int LOG_BODY_CHARS = 400;

    private GoogleBooksClient() {
    }

    public static List<MediaItem> search(String query) throws Exception {
        String url = BASE + "?printType=books&maxResults=20&q=" + enc(query);
        String key = BuildConfig.GOOGLE_BOOKS_API_KEY;
        boolean hasKey = key != null && !key.isEmpty();
        Log.d(TAG, "Request: GET " + url + " (api key: " + (hasKey ? "set" : "none") + ")");
        try {
            String body = fetch(hasKey ? url + "&key=" + enc(key) : url);
            List<MediaItem> items = parse(body);
            Log.d(TAG, "Parsed " + items.size() + " book(s) for query \"" + query + "\"");
            return items;
        } catch (Exception e) {
            Log.e(TAG, "Search failed for query \"" + query + "\": " + e, e);
            throw e;
        }
    }

    /** Performs the GET. Package-private so it can be tested against a local server. */
    static String fetch(String urlString) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(urlString).openConnection();
        try {
            conn.setConnectTimeout(10_000);
            conn.setReadTimeout(10_000);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("User-Agent", "Adapted/1.0 (Android)");
            int code = conn.getResponseCode();
            Log.d(TAG, "HTTP response code: " + code);

            InputStream stream = code >= 400 ? conn.getErrorStream() : conn.getInputStream();
            String body = stream == null ? "" : read(stream);
            if (code != HttpURLConnection.HTTP_OK) {
                // 429 / 403 here usually means the anonymous Google Books quota is exhausted.
                Log.e(TAG, "Request failed, HTTP " + code + ", error body: " + truncate(body));
                throw new IOException("Google Books HTTP " + code);
            }
            Log.d(TAG, "Response body: " + truncate(body));
            return body;
        } finally {
            conn.disconnect();
        }
    }

    /** Converts a Google Books "volumes" JSON response into MediaItems (and registers them in the cache). */
    static List<MediaItem> parse(String json) throws Exception {
        JSONObject root = new JSONObject(json);
        Log.d(TAG, "totalItems reported by API: " + root.optInt("totalItems", -1));
        JSONArray arr = root.optJSONArray("items");
        List<MediaItem> out = new ArrayList<>();
        if (arr == null) {
            Log.w(TAG, "Response has no \"items\" array (no matches)");
            return out;
        }
        for (int i = 0; i < arr.length(); i++) {
            try {
                JSONObject o = arr.getJSONObject(i);
                JSONObject info = o.optJSONObject("volumeInfo");
                if (info == null) {
                    continue;
                }
                JSONArray authors = info.optJSONArray("authors");
                String creator = authors != null && authors.length() > 0
                        ? authors.getString(0) : "Unknown author";

                String cover = null;
                JSONObject links = info.optJSONObject("imageLinks");
                if (links != null) {
                    cover = links.optString("thumbnail", links.optString("smallThumbnail", ""));
                    cover = cover.isEmpty() ? null
                            : cover.replace("http://", "https://").replace("&edge=curl", "");
                }
                String publisher = info.optString("publisher", "");

                int id = ApiMediaCache.idFor("BOOK:" + o.getString("id"));
                MediaItem item = new MediaItem(id, info.optString("title", "Untitled"), creator,
                        MediaCategory.BOOK, year(info.optString("publishedDate", "")),
                        (float) info.optDouble("averageRating", 0), publisher.isEmpty() ? null : publisher,
                        null, null, null, 0, cover);
                ApiMediaCache.put(item);
                out.add(item);
            } catch (Exception e) {
                Log.w(TAG, "Skipping malformed item #" + i + ": " + e);
            }
        }
        return out;
    }

    private static int year(String date) {
        if (date.length() < 4) {
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

    private static String read(InputStream in) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }
    }

    private static String truncate(String s) {
        return s.length() <= LOG_BODY_CHARS ? s : s.substring(0, LOG_BODY_CHARS) + "…(" + s.length() + " chars)";
    }
}
