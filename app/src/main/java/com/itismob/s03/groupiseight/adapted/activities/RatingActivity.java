package com.itismob.s03.groupiseight.adapted.activities;

import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.itismob.s03.groupiseight.adapted.R;
import com.itismob.s03.groupiseight.adapted.data.SampleMediaData;
import com.itismob.s03.groupiseight.adapted.models.MediaItem;

public class RatingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        MaterialToolbar toolbar = findViewById(R.id.rating_toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        int mediaId = getIntent().getIntExtra("extra_media_id", -1);
        MediaItem item = SampleMediaData.getMediaById(mediaId);

        if (item != null) {
            ((TextView) findViewById(R.id.rating_media_title)).setText("Rate " + item.getTitle());
        }

        RatingBar ratingBar = findViewById(R.id.media_rating_bar);

        findViewById(R.id.btn_submit_rating).setOnClickListener(v -> {
            float rating = ratingBar.getRating();
            if (rating == 0.0f) {
                Toast.makeText(this, "Please select a rating", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Rated " + rating + " stars!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}