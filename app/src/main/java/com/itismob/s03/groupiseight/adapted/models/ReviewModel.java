package com.itismob.s03.groupiseight.adapted.models;

/**
 * Data model for Module H: Reviews and Community Interaction.
 * Represents a community review for a media item.
 */
public class ReviewModel {
    private final int id;
    private final int mediaId;
    private final String authorName;
    private final float rating;
    private final String reviewText;
    private final String dateString;
    private int likeCount;
    private int commentCount;
    private boolean isLiked;

    public ReviewModel(int id, int mediaId, String authorName, float rating, String reviewText, String dateString, int likeCount, int commentCount, boolean isLiked) {
        this.id = id;
        this.mediaId = mediaId;
        this.authorName = authorName;
        this.rating = rating;
        this.reviewText = reviewText;
        this.dateString = dateString;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.isLiked = isLiked;
    }

    public int getId() {
        return id;
    }

    public int getMediaId() {
        return mediaId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public float getRating() {
        return rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public String getDateString() {
        return dateString;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public void toggleLike() {
        if (isLiked) {
            isLiked = false;
            likeCount = Math.max(0, likeCount - 1);
        } else {
            isLiked = true;
            likeCount++;
        }
    }

    public void incrementCommentCount() {
        this.commentCount++;
    }
}
