package com.itismob.s03.groupiseight.adapted.adapters;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itismob.s03.groupiseight.adapted.R;
import com.itismob.s03.groupiseight.adapted.models.ReviewModel;

import java.util.List;
import java.util.Locale;

/**
 * Adapter for Module H: Reviews and Community Interaction.
 * Renders review cards, handles liking reviews, and navigating to comments.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    public interface OnReviewActionListener {
        void onLikeClick(ReviewModel review, int position);
        void onCommentClick(ReviewModel review);
    }

    private final List<ReviewModel> reviews;
    private final OnReviewActionListener listener;

    public ReviewAdapter(List<ReviewModel> reviews, OnReviewActionListener listener) {
        this.reviews = reviews;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        ReviewModel review = reviews.get(position);

        holder.tvAuthor.setText(review.getAuthorName());
        holder.tvDate.setText(review.getDateString());
        holder.tvRating.setText(String.format(Locale.ROOT, "★ %.1f", review.getRating()));
        holder.tvReviewText.setText(review.getReviewText());
        holder.tvLikeCount.setText(String.valueOf(review.getLikeCount()));
        holder.tvCommentCount.setText(String.valueOf(review.getCommentCount()));

        int primaryColor = holder.tvRating.getCurrentTextColor();
        int defaultColor = holder.tvDate.getCurrentTextColor();

        if (review.isLiked()) {
            holder.imgLikeIcon.setImageTintList(ColorStateList.valueOf(primaryColor));
            holder.tvLikeCount.setTextColor(primaryColor);
        } else {
            holder.imgLikeIcon.setImageTintList(ColorStateList.valueOf(defaultColor));
            holder.tvLikeCount.setTextColor(defaultColor);
        }

        holder.btnLike.setOnClickListener(v -> {
            if (listener != null) {
                int pos = holder.getBindingAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    listener.onLikeClick(review, pos);
                }
            }
        });

        holder.btnComments.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCommentClick(review);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        final TextView tvAuthor;
        final TextView tvDate;
        final TextView tvRating;
        final TextView tvReviewText;
        final TextView tvLikeCount;
        final TextView tvCommentCount;
        final ImageView imgLikeIcon;
        final View btnLike;
        final View btnComments;

        ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tv_review_author);
            tvDate = itemView.findViewById(R.id.tv_review_date);
            tvRating = itemView.findViewById(R.id.tv_review_rating);
            tvReviewText = itemView.findViewById(R.id.tv_review_text);
            tvLikeCount = itemView.findViewById(R.id.tv_like_count);
            tvCommentCount = itemView.findViewById(R.id.tv_comment_count);
            imgLikeIcon = itemView.findViewById(R.id.img_like_icon);
            btnLike = itemView.findViewById(R.id.btn_like_review);
            btnComments = itemView.findViewById(R.id.btn_view_comments);
        }
    }
}
