package com.itismob.s03.groupiseight.adapted.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.itismob.s03.groupiseight.adapted.R;
import com.itismob.s03.groupiseight.adapted.data.SampleMediaData;
import com.itismob.s03.groupiseight.adapted.models.MediaItem;

public class MediaDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_details);

        MaterialToolbar toolbar = findViewById(R.id.details_toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        int mediaId = getIntent().getIntExtra("extra_media_id", -1);
        MediaItem item = SampleMediaData.getMediaById(mediaId);

        if (item == null) {
            Toast.makeText(this, "Media not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ((ImageView) findViewById(R.id.details_cover)).setImageResource(item.getCoverResId());
        ((TextView) findViewById(R.id.details_title)).setText(item.getTitle());
        ((TextView) findViewById(R.id.details_creator)).setText(item.getCreator());
        ((TextView) findViewById(R.id.details_metadata)).setText(item.getYear() + " • Rating: " + item.getRating());

        findViewById(R.id.btn_rate_media).setOnClickListener(v -> {
            Intent intent = new Intent(this, RatingActivity.class);
            intent.putExtra("extra_media_id", item.getId());
            startActivity(intent);
        });

        View btnViewReviews = findViewById(R.id.btn_view_reviews);
        if (btnViewReviews != null) {
            btnViewReviews.setOnClickListener(v -> {
                Intent intent = new Intent(this, ReviewsActivity.class);
                intent.putExtra(ReviewsActivity.EXTRA_MEDIA_ID, item.getId());
                startActivity(intent);
            });
        }

        MaterialButton compareBtn = findViewById(R.id.btn_compare_adaptation);
        if (item.getLinkedMediaId() != null) {
            compareBtn.setVisibility(View.VISIBLE);
            compareBtn.setText(item.hasAdaptationNote() ? item.getAdaptationNote() : "View Adaptation");
            compareBtn.setOnClickListener(v -> {
                Intent intent = new Intent(this, ComparisonActivity.class);
                intent.putExtra("extra_source_media_id", item.getId());
                intent.putExtra("extra_linked_media_id", item.getLinkedMediaId());
                startActivity(intent);
            });
        }
    }
}