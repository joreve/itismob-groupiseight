package com.itismob.s03.groupiseight.adapted.helpers;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.itismob.s03.groupiseight.adapted.R;
import com.itismob.s03.groupiseight.adapted.models.MediaItem;

/** Shows a cover from a URL (API items) or from a drawable resource (sample items). */
public final class CoverLoader {

    private CoverLoader() {
    }

    public static void load(ImageView view, MediaItem item) {
        if (item.hasCoverUrl()) {
            Glide.with(view)
                    .load(item.getCoverUrl())
                    .placeholder(R.color.gray_light)
                    .error(R.color.gray_light)
                    .centerCrop()
                    .into(view);
        } else {
            Glide.with(view).clear(view); // cancel any pending load from a recycled view
            view.setImageResource(item.getCoverResId());
        }
    }
}
