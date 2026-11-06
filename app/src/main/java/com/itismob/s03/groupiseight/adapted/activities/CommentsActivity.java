package com.itismob.s03.groupiseight.adapted.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.itismob.s03.groupiseight.adapted.R;
import com.itismob.s03.groupiseight.adapted.adapters.CommentAdapter;
import com.itismob.s03.groupiseight.adapted.data.SampleMediaData;
import com.itismob.s03.groupiseight.adapted.helpers.AuthManager;
import com.itismob.s03.groupiseight.adapted.models.CommentModel;
import com.itismob.s03.groupiseight.adapted.models.ReviewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for Module H: Reviews and Community Interaction.
 * Displays comment thread for a review and allows posting new comments.
 */
public class CommentsActivity extends AppCompatActivity {

    public static final String EXTRA_REVIEW_ID = "extra_review_id";

    private int reviewId;
    private final List<CommentModel> commentsList = new ArrayList<>();
    private CommentAdapter adapter;
    private RecyclerView rvComments;
    private View emptyStateView;
    private EditText etCommentInput;
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        reviewId = getIntent().getIntExtra(EXTRA_REVIEW_ID, -1);
        ReviewModel review = SampleMediaData.getReviewById(reviewId);

        if (review == null) {
            Toast.makeText(this, "Review not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        authManager = new AuthManager(this);

        MaterialToolbar toolbar = findViewById(R.id.comments_toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        TextView tvHeaderAuthor = findViewById(R.id.tv_comment_header_author);
        TextView tvHeaderReview = findViewById(R.id.tv_comment_header_review);

        tvHeaderAuthor.setText("Review by " + review.getAuthorName() + " (" + String.format("★ %.1f", review.getRating()) + ")");
        tvHeaderReview.setText("\"" + review.getReviewText() + "\"");

        rvComments = findViewById(R.id.rv_comments);
        emptyStateView = findViewById(R.id.layout_comments_empty);
        etCommentInput = findViewById(R.id.et_comment_input);
        MaterialButton btnSend = findViewById(R.id.btn_send_comment);

        rvComments.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommentAdapter(commentsList);
        rvComments.setAdapter(adapter);

        loadComments();

        btnSend.setOnClickListener(v -> postComment());
    }

    private void loadComments() {
        commentsList.clear();
        List<CommentModel> loaded = SampleMediaData.getCommentsForReview(reviewId);
        commentsList.addAll(loaded);

        if (commentsList.isEmpty()) {
            rvComments.setVisibility(View.GONE);
            emptyStateView.setVisibility(View.VISIBLE);
        } else {
            rvComments.setVisibility(View.VISIBLE);
            emptyStateView.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }
    }

    private void postComment() {
        String input = etCommentInput.getText().toString().trim();
        if (TextUtils.isEmpty(input)) {
            Toast.makeText(this, "Please write a comment first", Toast.LENGTH_SHORT).show();
            return;
        }

        String loggedInEmail = authManager.getLoggedInEmail();
        String authorName = loggedInEmail != null ? loggedInEmail : "Community User";

        SampleMediaData.addComment(reviewId, authorName, input);
        etCommentInput.setText("");

        loadComments();
        if (!commentsList.isEmpty()) {
            rvComments.smoothScrollToPosition(commentsList.size() - 1);
        }

        Toast.makeText(this, "Comment posted!", Toast.LENGTH_SHORT).show();
    }
}
