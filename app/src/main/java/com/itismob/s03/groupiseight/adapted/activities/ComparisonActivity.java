package com.itismob.s03.groupiseight.adapted.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;

import com.itismob.s03.groupiseight.adapted.R;
import com.itismob.s03.groupiseight.adapted.adapters.CommentAdapter;
import com.itismob.s03.groupiseight.adapted.adapters.ComparisonAdapter;
import com.itismob.s03.groupiseight.adapted.data.SampleMediaData;
import com.itismob.s03.groupiseight.adapted.helpers.WindowInsetsHelper;
import com.itismob.s03.groupiseight.adapted.models.CommentModel;
import com.itismob.s03.groupiseight.adapted.models.ComparisonModel;
import com.itismob.s03.groupiseight.adapted.models.MediaItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Module G: Adaptation Comparison Activity
 * Allows users to compare an original work (e.g. Book) side-by-side with its adaptation (e.g. Movie/TV),
 * view adaptation faithfulness metrics, aspect breakdown, and participate in community voting and comments.
 */
public class ComparisonActivity extends AppCompatActivity {

    public static final String EXTRA_SOURCE_MEDIA_ID = "extra_source_media_id";
    public static final String EXTRA_LINKED_MEDIA_ID = "extra_linked_media_id";
    private static final String PREFS_VOTES = "comparison_votes_prefs";

    private AutoCompleteTextView pairSpinner;
    private MaterialCardView sourceCard;
    private ImageView sourceCoverImg;
    private TextView sourceBadgeTxt;
    private TextView sourceTitleTxt;
    private TextView sourceCreatorTxt;
    private TextView sourceMetaTxt;

    private MaterialCardView targetCard;
    private ImageView targetCoverImg;
    private TextView targetBadgeTxt;
    private TextView targetTitleTxt;
    private TextView targetCreatorTxt;
    private TextView targetMetaTxt;

    private TextView faithfulnessScoreTxt;
    private LinearProgressIndicator faithfulnessProgress;
    private TextView comparisonVerdictTxt;

    private MaterialButton voteSourceBtn;
    private MaterialButton voteTargetBtn;
    private LinearProgressIndicator communityPollProgress;
    private TextView voteStatusTxt;

    private RecyclerView aspectsRecyclerView;
    private ComparisonAdapter adapter;

    private RecyclerView commentsRecyclerView;
    private CommentAdapter commentAdapter;
    private TextInputEditText etCommentInput;
    private MaterialButton btnPostComment;
    private final List<CommentModel> currentComments = new ArrayList<>();

    private int currentSourceId = -1;
    private int currentTargetId = -1;
    private ComparisonModel currentComparison;
    private final List<PairOption> availablePairs = new ArrayList<>();

    private static class PairOption {
        final int sourceId;
        final int targetId;
        final String displayName;

        PairOption(int sourceId, int targetId, String displayName) {
            this.sourceId = sourceId;
            this.targetId = targetId;
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowInsetsHelper.enableEdgeToEdge(this);
        setContentView(R.layout.activity_comparison);
        WindowInsetsHelper.applySystemBarPadding(findViewById(R.id.comparison_root), false);

        initViews();
        setupToolbar();
        setupAvailablePairs();
        parseIntentExtras();
        setupAspectsList();
        setupCommentsList();
        setupClickListeners();

        updateComparisonView();
    }

    private void initViews() {
        pairSpinner = findViewById(R.id.spinner_comparison_pairs);

        sourceCard = findViewById(R.id.card_source_media);
        sourceCoverImg = findViewById(R.id.img_source_cover);
        sourceBadgeTxt = findViewById(R.id.txt_source_badge);
        sourceTitleTxt = findViewById(R.id.txt_source_title);
        sourceCreatorTxt = findViewById(R.id.txt_source_creator);
        sourceMetaTxt = findViewById(R.id.txt_source_meta);

        targetCard = findViewById(R.id.card_target_media);
        targetCoverImg = findViewById(R.id.img_target_cover);
        targetBadgeTxt = findViewById(R.id.txt_target_badge);
        targetTitleTxt = findViewById(R.id.txt_target_title);
        targetCreatorTxt = findViewById(R.id.txt_target_creator);
        targetMetaTxt = findViewById(R.id.txt_target_meta);

        faithfulnessScoreTxt = findViewById(R.id.txt_faithfulness_score);
        faithfulnessProgress = findViewById(R.id.progress_faithfulness);
        comparisonVerdictTxt = findViewById(R.id.txt_comparison_verdict);

        voteSourceBtn = findViewById(R.id.btn_vote_source);
        voteTargetBtn = findViewById(R.id.btn_vote_target);
        communityPollProgress = findViewById(R.id.progress_community_poll);
        voteStatusTxt = findViewById(R.id.txt_vote_status);

        aspectsRecyclerView = findViewById(R.id.rv_comparison_aspects);

        commentsRecyclerView = findViewById(R.id.rv_comparison_comments);
        etCommentInput = findViewById(R.id.et_comment_input);
        btnPostComment = findViewById(R.id.btn_post_comment);
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.comparison_toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupAvailablePairs() {
        availablePairs.clear();

        List<MediaItem> allMedia = SampleMediaData.getAllMedia();
        for (MediaItem item : allMedia) {
            if (item.getLinkedMediaId() != null) {
                int sourceId = item.getId();
                int linkedId = item.getLinkedMediaId();

                // Avoid duplicate pair options in reverse direction
                boolean exists = false;
                for (PairOption option : availablePairs) {
                    if ((option.sourceId == sourceId && option.targetId == linkedId) ||
                        (option.sourceId == linkedId && option.targetId == sourceId)) {
                        exists = true;
                        break;
                    }
                }

                if (!exists) {
                    MediaItem linkedItem = SampleMediaData.getMediaById(linkedId);
                    if (linkedItem != null) {
                        String label = item.getTitle() + " (" +
                                getString(item.getCategory().getLabelResId()) + " vs " +
                                getString(linkedItem.getCategory().getLabelResId()) + ")";
                        availablePairs.add(new PairOption(sourceId, linkedId, label));
                    }
                }
            }
        }

        ArrayAdapter<PairOption> spinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, availablePairs);
        pairSpinner.setAdapter(spinnerAdapter);
        pairSpinner.setOnItemClickListener((parent, view, position, id) -> {
            PairOption selected = availablePairs.get(position);
            currentSourceId = selected.sourceId;
            currentTargetId = selected.targetId;
            updateComparisonView();
        });
    }

    private void parseIntentExtras() {
        int intentSource = getIntent().getIntExtra(EXTRA_SOURCE_MEDIA_ID, -1);
        int intentLinked = getIntent().getIntExtra(EXTRA_LINKED_MEDIA_ID, -1);

        if (intentSource != -1 && intentLinked != -1) {
            currentSourceId = intentSource;
            currentTargetId = intentLinked;
        } else if (!availablePairs.isEmpty()) {
            currentSourceId = availablePairs.get(0).sourceId;
            currentTargetId = availablePairs.get(0).targetId;
        } else {
            // Default pair (Dune Book vs Dune Movie)
            currentSourceId = 2;
            currentTargetId = 14;
        }
    }

    private void setupAspectsList() {
        adapter = new ComparisonAdapter(new ArrayList<>(), "", "");
        aspectsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        aspectsRecyclerView.setAdapter(adapter);
    }

    private void setupCommentsList() {
        commentAdapter = new CommentAdapter(currentComments);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentsRecyclerView.setAdapter(commentAdapter);
    }

    private void setupClickListeners() {
        sourceCard.setOnClickListener(v -> openMediaDetails(currentSourceId));
        targetCard.setOnClickListener(v -> openMediaDetails(currentTargetId));

        findViewById(R.id.btn_swap_comparison).setOnClickListener(v -> swapSides());

        voteSourceBtn.setOnClickListener(v -> handleUserVote(true));
        voteTargetBtn.setOnClickListener(v -> handleUserVote(false));

        btnPostComment.setOnClickListener(v -> postUserComment());
    }

    private void updateComparisonView() {
        MediaItem sourceItem = SampleMediaData.getMediaById(currentSourceId);
        MediaItem targetItem = SampleMediaData.getMediaById(currentTargetId);

        if (sourceItem == null || targetItem == null) {
            return;
        }

        // Update pair selector dropdown text matching selected pair
        for (int i = 0; i < availablePairs.size(); i++) {
            PairOption option = availablePairs.get(i);
            if ((option.sourceId == currentSourceId && option.targetId == currentTargetId) ||
                (option.sourceId == currentTargetId && option.targetId == currentSourceId)) {
                pairSpinner.setText(option.displayName, false);
                break;
            }
        }

        // Apply primary focus highlighting to source card (left)
        int primaryColor = MaterialColors.getColor(sourceCard, androidx.appcompat.R.attr.colorPrimary, 0);
        int outlineColor = MaterialColors.getColor(targetCard, android.R.attr.textColorSecondary, 0);
        int twoDp = (int) (2 * getResources().getDisplayMetrics().density);
        int oneDp = (int) (1 * getResources().getDisplayMetrics().density);

        sourceCard.setStrokeColor(primaryColor);
        sourceCard.setStrokeWidth(twoDp);

        targetCard.setStrokeColor(outlineColor);
        targetCard.setStrokeWidth(oneDp);

        // Bind Source Card (Primary Focus)
        sourceCoverImg.setImageResource(sourceItem.getCoverResId());
        sourceBadgeTxt.setText("★ " + getString(sourceItem.getCategory().getLabelResId()) + " (PRIMARY FOCUS)");
        sourceBadgeTxt.setTextColor(primaryColor);
        sourceTitleTxt.setText(sourceItem.getTitle());
        sourceCreatorTxt.setText(sourceItem.getCreator());
        sourceMetaTxt.setText(String.format(Locale.getDefault(), "%d • ★ %.1f",
                sourceItem.getYear(), sourceItem.getRating()));

        // Bind Target Card (Secondary)
        targetCoverImg.setImageResource(targetItem.getCoverResId());
        targetBadgeTxt.setText(getString(targetItem.getCategory().getLabelResId()));
        targetBadgeTxt.setTextColor(MaterialColors.getColor(targetCard, android.R.attr.textColorSecondary, 0));
        targetTitleTxt.setText(targetItem.getTitle());
        targetCreatorTxt.setText(targetItem.getCreator());
        targetMetaTxt.setText(String.format(Locale.getDefault(), "%d • ★ %.1f",
                targetItem.getYear(), targetItem.getRating()));

        // Load comparison model metrics
        currentComparison = ComparisonModel.getComparisonFor(currentSourceId, currentTargetId);
        faithfulnessScoreTxt.setText(String.format(Locale.getDefault(), "%d%%", currentComparison.getFaithfulnessPercentage()));
        faithfulnessProgress.setProgress(currentComparison.getFaithfulnessPercentage());
        comparisonVerdictTxt.setText(currentComparison.getVerdict());

        // Update votes and poll display
        updatePollDisplay(sourceItem, targetItem);

        // Update aspect breakdown RecyclerView
        String sourceLabel = getString(sourceItem.getCategory().getLabelResId()).toUpperCase(Locale.getDefault()) + " VERSION";
        String targetLabel = getString(targetItem.getCategory().getLabelResId()).toUpperCase(Locale.getDefault()) + " VERSION";
        adapter.updateData(currentComparison.getAspects(), sourceLabel, targetLabel);

        // Load comments for current pair
        loadSampleComments();
    }

    private void updatePollDisplay(MediaItem sourceItem, MediaItem targetItem) {
        SharedPreferences prefs = getSharedPreferences(PREFS_VOTES, Context.MODE_PRIVATE);
        String voteKey = getVoteKey(currentSourceId, currentTargetId);
        int savedVotedMediaId = prefs.getInt(voteKey, -1);

        int baseSourceVotes = currentComparison.getSourceVotes();
        int baseTargetVotes = currentComparison.getTargetVotes();

        if (savedVotedMediaId == currentSourceId) {
            baseSourceVotes++;
        } else if (savedVotedMediaId == currentTargetId) {
            baseTargetVotes++;
        }

        int totalVotes = baseSourceVotes + baseTargetVotes;
        int sourcePct = totalVotes > 0 ? Math.round((baseSourceVotes * 100f) / totalVotes) : 50;
        int targetPct = 100 - sourcePct;

        String sourceCategory = getString(sourceItem.getCategory().getLabelResId());
        String targetCategory = getString(targetItem.getCategory().getLabelResId());

        voteSourceBtn.setText(getString(R.string.comparison_vote_source_format,
                sourceCategory, formatVoteCount(baseSourceVotes)));
        voteTargetBtn.setText(getString(R.string.comparison_vote_target_format,
                targetCategory, formatVoteCount(baseTargetVotes)));

        communityPollProgress.setProgress(sourcePct);
        voteStatusTxt.setText(getString(R.string.comparison_vote_status_format,
                sourcePct, sourceCategory, targetPct, targetCategory));

        // Highlight selected vote state
        if (savedVotedMediaId == currentSourceId) {
            voteSourceBtn.setEnabled(true);
            voteTargetBtn.setEnabled(true);
            voteSourceBtn.setText("✓ " + voteSourceBtn.getText());
        } else if (savedVotedMediaId == currentTargetId) {
            voteSourceBtn.setEnabled(true);
            voteTargetBtn.setEnabled(true);
            voteTargetBtn.setText("✓ " + voteTargetBtn.getText());
        } else {
            voteSourceBtn.setEnabled(true);
            voteTargetBtn.setEnabled(true);
        }
    }

    private void handleUserVote(boolean isSourceVote) {
        SharedPreferences prefs = getSharedPreferences(PREFS_VOTES, Context.MODE_PRIVATE);
        String voteKey = getVoteKey(currentSourceId, currentTargetId);
        int votedMediaId = isSourceVote ? currentSourceId : currentTargetId;

        prefs.edit().putInt(voteKey, votedMediaId).apply();

        MediaItem votedItem = SampleMediaData.getMediaById(votedMediaId);
        String votedName = votedItem != null ? votedItem.getTitle() : "selected version";

        Toast.makeText(this, getString(R.string.comparison_voted_source, votedName), Toast.LENGTH_SHORT).show();
        updateComparisonView();
    }

    private void postUserComment() {
        if (etCommentInput == null) return;
        String text = etCommentInput.getText() != null ? etCommentInput.getText().toString().trim() : "";
        if (TextUtils.isEmpty(text)) {
            etCommentInput.setError(getString(R.string.comparison_comment_hint));
            return;
        }

        String pairKey = getVoteKey(currentSourceId, currentTargetId);
        CommentModel newComment = new CommentModel(
                (int) System.currentTimeMillis(),
                "You",
                "Just now",
                text,
                R.drawable.ic_adapted_logo,
                pairKey,
                0
        );

        commentAdapter.addComment(newComment);
        etCommentInput.setText("");
        Toast.makeText(this, getString(R.string.comparison_comment_posted), Toast.LENGTH_SHORT).show();
    }

    private void loadSampleComments() {
        currentComments.clear();
        String pairKey = getVoteKey(currentSourceId, currentTargetId);

        if (currentSourceId == 2 || currentTargetId == 2) {
            // Dune
            currentComments.add(new CommentModel(1, "Marcus V.", "2 hours ago",
                    "Denis Villeneuve captured the scale of Arrakis insanely well. The book's internal monologues were translated into Hans Zimmer's score and visual pacing.",
                    R.drawable.ic_adapted_logo, pairKey, 24));
            currentComments.add(new CommentModel(2, "Elena R.", "5 hours ago",
                    "The book spends so much time on ecology and water discipline that felt slightly trimmed in the movie, but visually it's a masterpiece.",
                    R.drawable.ic_adapted_logo, pairKey, 18));
            currentComments.add(new CommentModel(3, "David K.", "1 day ago",
                    "Splitting Dune into two parts was the best decision for a faithful adaptation!",
                    R.drawable.ic_adapted_logo, pairKey, 31));
        } else if (currentSourceId == 1 || currentTargetId == 1) {
            // Hunger Games
            currentComments.add(new CommentModel(4, "Sarah T.", "3 hours ago",
                    "Jennifer Lawrence embodied Katniss's internal trauma perfectly even without the first-person narrator voice.",
                    R.drawable.ic_adapted_logo, pairKey, 15));
            currentComments.add(new CommentModel(5, "Chris M.", "6 hours ago",
                    "Adding the Gamemaker control room scenes behind the scenes made the movie adaptation even richer than the book POV.",
                    R.drawable.ic_adapted_logo, pairKey, 22));
        } else {
            // Default sample comments
            currentComments.add(new CommentModel(6, "Jordan P.", "4 hours ago",
                    "The adaptation captures the main dramatic arcs well while tailoring the story pacing for the screen.",
                    R.drawable.ic_adapted_logo, pairKey, 12));
            currentComments.add(new CommentModel(7, "Taylor S.", "8 hours ago",
                    "Great character performances that bring the original work to life!",
                    R.drawable.ic_adapted_logo, pairKey, 9));
        }

        if (commentAdapter != null) {
            commentAdapter.updateComments(currentComments);
        }
    }

    private void swapSides() {
        int temp = currentSourceId;
        currentSourceId = currentTargetId;
        currentTargetId = temp;
        updateComparisonView();
    }

    private void openMediaDetails(int mediaId) {
        Intent intent = new Intent(this, MediaDetailsActivity.class);
        intent.putExtra("extra_media_id", mediaId);
        startActivity(intent);
    }

    private String getVoteKey(int sourceId, int targetId) {
        int min = Math.min(sourceId, targetId);
        int max = Math.max(sourceId, targetId);
        return "vote_pair_" + min + "_" + max;
    }

    private String formatVoteCount(int count) {
        if (count >= 1000) {
            return String.format(Locale.getDefault(), "%,d", count);
        }
        return String.valueOf(count);
    }
}
