package com.itismob.s03.groupiseight.adapted.models;

import com.itismob.s03.groupiseight.adapted.R;

/**
 * Data model for comments used by Module H (Reviews & Community Interaction)
 * and Module G (Adaptation Comparison).
 */
public class CommentModel {
    private final int id;
    private final int reviewId;
    private final String authorName;
    private final String timestamp;
    private final String commentText;
    private final int avatarResId;
    private final String pairKey;
    private int likesCount;
    private boolean isLiked;

    // Module H constructor
    public CommentModel(int id, int reviewId, String authorName, String commentText, String timestamp) {
        this(id, reviewId, authorName, timestamp, commentText, R.drawable.ic_person_avatar, null, 0);
    }

    // Module G constructor
    public CommentModel(int id, String authorName, String timestamp, String commentText, int avatarResId, String pairKey, int likesCount) {
        this(id, -1, authorName, timestamp, commentText, avatarResId, pairKey, likesCount);
    }

    // Master constructor
    public CommentModel(int id, int reviewId, String authorName, String timestamp, String commentText, int avatarResId, String pairKey, int likesCount) {
        this.id = id;
        this.reviewId = reviewId;
        this.authorName = authorName;
        this.timestamp = timestamp;
        this.commentText = commentText;
        this.avatarResId = avatarResId != 0 ? avatarResId : R.drawable.ic_person_avatar;
        this.pairKey = pairKey;
        this.likesCount = likesCount;
        this.isLiked = false;
    }

    public int getId() {
        return id;
    }

    public int getReviewId() {
        return reviewId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getCommentText() {
        return commentText;
    }

    public int getAvatarResId() {
        return avatarResId;
    }

    public String getPairKey() {
        return pairKey;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
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
            likesCount = Math.max(0, likesCount - 1);
        } else {
            isLiked = true;
            likesCount++;
        }
    }
}
