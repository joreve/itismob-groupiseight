package com.itismob.s03.groupiseight.adapted.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itismob.s03.groupiseight.adapted.R;
import com.itismob.s03.groupiseight.adapted.models.ComparisonModel;

import java.util.ArrayList;
import java.util.List;

public class ComparisonAdapter extends RecyclerView.Adapter<ComparisonAdapter.AspectViewHolder> {

    private final List<ComparisonModel.ComparisonAspect> aspects = new ArrayList<>();
    private String sourceLabel = "Original";
    private String targetLabel = "Adaptation";

    public ComparisonAdapter(List<ComparisonModel.ComparisonAspect> aspects, String sourceLabel, String targetLabel) {
        if (aspects != null) {
            this.aspects.addAll(aspects);
        }
        if (sourceLabel != null) {
            this.sourceLabel = sourceLabel;
        }
        if (targetLabel != null) {
            this.targetLabel = targetLabel;
        }
    }

    public void updateData(List<ComparisonModel.ComparisonAspect> newAspects, String sourceLabel, String targetLabel) {
        this.aspects.clear();
        if (newAspects != null) {
            this.aspects.addAll(newAspects);
        }
        this.sourceLabel = sourceLabel;
        this.targetLabel = targetLabel;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AspectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comparison, parent, false);
        return new AspectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AspectViewHolder holder, int position) {
        holder.bind(aspects.get(position), sourceLabel, targetLabel);
    }

    @Override
    public int getItemCount() {
        return aspects.size();
    }

    static class AspectViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleText;
        private final TextView scoreChip;
        private final LinearLayout sourceContainer;
        private final TextView sourceLabelText;
        private final TextView sourceDetailText;
        private final LinearLayout targetContainer;
        private final TextView targetLabelText;
        private final TextView targetDetailText;

        AspectViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.txt_aspect_title);
            scoreChip = itemView.findViewById(R.id.txt_aspect_score_chip);
            sourceContainer = itemView.findViewById(R.id.container_aspect_source);
            sourceLabelText = itemView.findViewById(R.id.txt_aspect_source_label);
            sourceDetailText = itemView.findViewById(R.id.txt_aspect_source_detail);
            targetContainer = itemView.findViewById(R.id.container_aspect_target);
            targetLabelText = itemView.findViewById(R.id.txt_aspect_target_label);
            targetDetailText = itemView.findViewById(R.id.txt_aspect_target_detail);
        }

        void bind(ComparisonModel.ComparisonAspect aspect, String sourceLabel, String targetLabel) {
            Context context = itemView.getContext();
            titleText.setText(aspect.getTitle());
            scoreChip.setText(context.getString(R.string.comparison_match_format, aspect.getFaithfulnessScore()));
            sourceLabelText.setText(sourceLabel);
            sourceDetailText.setText(aspect.getSourceDetail());
            targetLabelText.setText(targetLabel);
            targetDetailText.setText(aspect.getTargetDetail());

            if (sourceContainer != null) {
                sourceContainer.setBackgroundResource(R.drawable.bg_aspect_highlight_primary);
            }
            if (targetContainer != null) {
                targetContainer.setBackgroundResource(R.drawable.bg_aspect_neutral);
            }
        }
    }
}
