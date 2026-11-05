package com.itismob.s03.groupiseight.adapted.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.itismob.s03.groupiseight.adapted.R;
import com.itismob.s03.groupiseight.adapted.adapters.ReviewAdapter;
import com.itismob.s03.groupiseight.adapted.data.SampleMediaData;
import com.itismob.s03.groupiseight.adapted.models.MediaItem;
import com.itismob.s03.groupiseight.adapted.models.ReviewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for Module H: Reviews and Community Interaction.
 * Displays community reviews for a selected media item.
 */
public class ReviewsActivity extends AppCompatActivity implements ReviewAdapter.OnReviewActionListener {

    public static final String EXTRA_MEDIA_ID = "extra_media_id";

    private int mediaId;
    private final List<ReviewModel> reviewsList = new ArrayList<>();
    private ReviewAdapter adapter;
    private RecyclerView rvReviews;
    private View emptyStateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        mediaId = getIntent().getIntExtra(EXTRA_MEDIA_ID, -1);
        MediaItem mediaItem = SampleMediaData.getMediaById(mediaId);

        if (mediaItem == null) {
            Toast.makeText(this, "Media not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        MaterialToolbar toolbar = findViewById(R.id.reviews_toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        TextView tvTitle = findViewById(R.id.tv_reviews_media_title);
        TextView tvSubtitle = findViewById(R.id.tv_reviews_media_subtitle);

        tvTitle.setText(mediaItem.getTitle());
        String categoryLabel = getString(mediaItem.getCategory().getLabelResId());
        tvSubtitle.setText(categoryLabel + " • " + mediaItem.getCreator() + " (" + mediaItem.getYear() + ")");

        rvReviews = findViewById(R.id.rv_reviews);
        emptyStateView = findViewById(R.id.layout_reviews_empty);

        rvReviews.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReviewAdapter(reviewsList, this);
        rvReviews.setAdapter(adapter);

        loadReviews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadReviews();
    }

    private void loadReviews() {
        reviewsList.clear();
        List<ReviewModel> loaded = SampleMediaData.getReviewsForMedia(mediaId);
        reviewsList.addAll(loaded);

        if (reviewsList.isEmpty()) {
            rvReviews.setVisibility(View.GONE);
            emptyStateView.setVisibility(View.VISIBLE);
        } else {
            rvReviews.setVisibility(View.VISIBLE);
            emptyStateView.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLikeClick(ReviewModel review, int position) {
        SampleMediaData.toggleReviewLike(review.getId());
        adapter.notifyItemChanged(position);
    }

    @Override
    public void onCommentClick(ReviewModel review) {
        Intent intent = new Intent(this, CommentsActivity.class);
        intent.putExtra(CommentsActivity.EXTRA_REVIEW_ID, review.getId());
        startActivity(intent);
    }
}
