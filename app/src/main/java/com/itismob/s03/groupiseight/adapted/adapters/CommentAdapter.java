package com.itismob.s03.groupiseight.adapted.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import com.itismob.s03.groupiseight.adapted.R;
import com.itismob.s03.groupiseight.adapted.models.CommentModel;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private final List<CommentModel> comments = new ArrayList<>();

    public CommentAdapter(List<CommentModel> comments) {
        if (comments != null) {
            this.comments.addAll(comments);
        }
    }

    public void updateComments(List<CommentModel> newComments) {
        this.comments.clear();
        if (newComments != null) {
            this.comments.addAll(newComments);
        }
        notifyDataSetChanged();
    }

    public void addComment(CommentModel comment) {
        if (comment != null) {
            this.comments.add(0, comment);
            notifyItemInserted(0);
        }
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.bind(comments.get(position));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        private final ImageView avatarImg;
        private final TextView authorText;
        private final TextView timeText;
        private final TextView commentText;
        private final MaterialButton likeBtn;

        CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarImg = itemView.findViewById(R.id.img_comment_avatar);
            authorText = itemView.findViewById(R.id.txt_comment_author);
            timeText = itemView.findViewById(R.id.txt_comment_time);
            commentText = itemView.findViewById(R.id.txt_comment_text);
            likeBtn = itemView.findViewById(R.id.btn_comment_like);
        }

        void bind(CommentModel comment) {
            authorText.setText(comment.getAuthorName());
            timeText.setText(comment.getTimestamp());
            commentText.setText(comment.getCommentText());
            likeBtn.setText(String.valueOf(comment.getLikesCount()));

            if (comment.getAvatarResId() != 0) {
                avatarImg.setImageResource(comment.getAvatarResId());
            }

            likeBtn.setOnClickListener(v -> {
                comment.toggleLike();
                likeBtn.setText(String.valueOf(comment.getLikesCount()));
            });
        }
    }
}
