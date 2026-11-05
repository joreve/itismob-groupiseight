package com.itismob.s03.groupiseight.adapted.models;

/**
 * Model representing a user comment / review post on an adaptation or media item.
 */
public class CommentModel {

    private final int id;
    private final String authorName;
    private final String timestamp;
    private final String commentText;
    private final int avatarResId;
    private final String pairKey;
    private int likesCount;
    private boolean isLikedByCurrentUser;

    public CommentModel(int id, String authorName, String timestamp, String commentText,
                        int avatarResId, String pairKey, int likesCount) {
        this.id = id;
        this.authorName = authorName;
        this.timestamp = timestamp;
        this.commentText = commentText;
        this.avatarResId = avatarResId;
        this.pairKey = pairKey;
        this.likesCount = likesCount;
        this.isLikedByCurrentUser = false;
    }

    public int getId() {
        return id;
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

    public boolean isLikedByCurrentUser() {
        return isLikedByCurrentUser;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public void setLikedByCurrentUser(boolean likedByCurrentUser) {
        isLikedByCurrentUser = likedByCurrentUser;
    }

    public void toggleLike() {
        if (isLikedByCurrentUser) {
            isLikedByCurrentUser = false;
            likesCount--;
        } else {
            isLikedByCurrentUser = true;
            likesCount++;
        }
    }
}
