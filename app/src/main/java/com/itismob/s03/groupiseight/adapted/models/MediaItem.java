package com.itismob.s03.groupiseight.adapted.models;

/**
 * One entry in the media library (a book, movie, TV series, or song/album).
 *
 * "creator" holds the author, director, or artist depending on the category.
 * publisher is only used for books and album only for music; both may be null.
 * adaptationNote is a short line describing the adaptation link (or null if there is none).
 */
public class MediaItem {

    private final int id;
    private final String title;
    private final String creator;
    private final MediaCategory category;
    private final int year;
    private final float rating;
    private final String publisher;
    private final String album;
    private final String adaptationNote;
    private final int coverResId;

    public MediaItem(int id, String title, String creator, MediaCategory category, int year,
                     float rating, String publisher, String album, String adaptationNote,
                     int coverResId) {
        this.id = id;
        this.title = title;
        this.creator = creator;
        this.category = category;
        this.year = year;
        this.rating = rating;
        this.publisher = publisher;
        this.album = album;
        this.adaptationNote = adaptationNote;
        this.coverResId = coverResId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCreator() {
        return creator;
    }

    public MediaCategory getCategory() {
        return category;
    }

    public int getYear() {
        return year;
    }

    public float getRating() {
        return rating;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getAlbum() {
        return album;
    }

    public String getAdaptationNote() {
        return adaptationNote;
    }

    public boolean hasAdaptationNote() {
        return adaptationNote != null && !adaptationNote.isEmpty();
    }

    public int getCoverResId() {
        return coverResId;
    }
}
