package com.itismob.s03.groupiseight.adapted.models;

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
    private final Integer linkedMediaId;
    private final int coverResId;
    private final String coverUrl; // only set for API items; null for sample items

    public MediaItem(int id, String title, String creator, MediaCategory category, int year,
                     float rating, String publisher, String album, String adaptationNote,
                     Integer linkedMediaId, int coverResId) {
        this(id, title, creator, category, year, rating, publisher, album, adaptationNote,
                linkedMediaId, coverResId, null);
    }

    public MediaItem(int id, String title, String creator, MediaCategory category, int year,
                     float rating, String publisher, String album, String adaptationNote,
                     Integer linkedMediaId, int coverResId, String coverUrl) {
        this.id = id;
        this.title = title;
        this.creator = creator;
        this.category = category;
        this.year = year;
        this.rating = rating;
        this.publisher = publisher;
        this.album = album;
        this.adaptationNote = adaptationNote;
        this.linkedMediaId = linkedMediaId;
        this.coverResId = coverResId;
        this.coverUrl = coverUrl;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getCreator() { return creator; }
    public MediaCategory getCategory() { return category; }
    public int getYear() { return year; }
    public float getRating() { return rating; }
    public String getPublisher() { return publisher; }
    public String getAlbum() { return album; }
    public String getAdaptationNote() { return adaptationNote; }
    public boolean hasAdaptationNote() { return adaptationNote != null && !adaptationNote.isEmpty(); }
    public Integer getLinkedMediaId() { return linkedMediaId; }
    public int getCoverResId() { return coverResId; }
    public String getCoverUrl() { return coverUrl; }
    public boolean hasCoverUrl() { return coverUrl != null && !coverUrl.isEmpty(); }
}