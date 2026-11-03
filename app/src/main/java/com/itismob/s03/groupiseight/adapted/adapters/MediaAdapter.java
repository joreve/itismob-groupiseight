package com.itismob.s03.groupiseight.adapted.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itismob.s03.groupiseight.adapted.R;
import com.itismob.s03.groupiseight.adapted.models.MediaItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Shows MediaItem cards. The same card is used in two places:
 * - horizontal lists on Home (fixed card width), and
 * - the 2-column grid in Explore (card fills its grid cell).
 *
 * Cards are display-only for now: Media Details is a separate module.
 */
public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MediaViewHolder> {

    private final List<MediaItem> items = new ArrayList<>();
    private final boolean horizontalList;

    public MediaAdapter(List<MediaItem> items, boolean horizontalList) {
        this.items.addAll(items);
        this.horizontalList = horizontalList;
    }

    /** Replaces the list contents, e.g. after the search text or category changes. */
    public void submitList(List<MediaItem> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_media, parent, false);
        if (horizontalList) {
            view.getLayoutParams().width = parent.getResources()
                    .getDimensionPixelSize(R.dimen.media_card_width_horizontal);
        }
        return new MediaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class MediaViewHolder extends RecyclerView.ViewHolder {

        private final ImageView coverImage;
        private final TextView titleText;
        private final TextView creatorText;
        private final TextView adaptationText;
        private final TextView categoryText;
        private final TextView ratingText;

        MediaViewHolder(@NonNull View itemView) {
            super(itemView);
            coverImage = itemView.findViewById(R.id.media_cover);
            titleText = itemView.findViewById(R.id.media_title);
            creatorText = itemView.findViewById(R.id.media_creator);
            adaptationText = itemView.findViewById(R.id.media_adaptation_note);
            categoryText = itemView.findViewById(R.id.media_category_year);
            ratingText = itemView.findViewById(R.id.media_rating);
        }

        void bind(MediaItem item) {
            Context context = itemView.getContext();
            coverImage.setImageResource(item.getCoverResId());
            coverImage.setContentDescription(
                    context.getString(R.string.media_cover_description, item.getTitle()));
            titleText.setText(item.getTitle());
            creatorText.setText(item.getCreator());

            if (item.hasAdaptationNote()) {
                adaptationText.setText(item.getAdaptationNote());
                adaptationText.setVisibility(View.VISIBLE);
            } else {
                adaptationText.setVisibility(View.GONE);
            }

            categoryText.setText(context.getString(R.string.media_category_year_format,
                    context.getString(item.getCategory().getLabelResId()), item.getYear()));
            ratingText.setText(context.getString(R.string.media_rating_format,
                    String.format(Locale.getDefault(), "%.1f", item.getRating())));
        }
    }
}
