package com.itismob.s03.groupiseight.adapted.models;

import com.itismob.s03.groupiseight.adapted.data.SampleMediaData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ComparisonModel {

    private final int sourceMediaId;
    private final int targetMediaId;
    private final float faithfulnessScore;
    private final int faithfulnessPercentage;
    private final String verdict;
    private int sourceVotes;
    private int targetVotes;
    private final List<ComparisonAspect> aspects;

    public static class ComparisonAspect {
        private final String title;
        private final String sourceDetail;
        private final String targetDetail;
        private final int faithfulnessScore;

        public ComparisonAspect(String title, String sourceDetail, String targetDetail, int faithfulnessScore) {
            this.title = title;
            this.sourceDetail = sourceDetail;
            this.targetDetail = targetDetail;
            this.faithfulnessScore = faithfulnessScore;
        }

        public String getTitle() {
            return title;
        }

        public String getSourceDetail() {
            return sourceDetail;
        }

        public String getTargetDetail() {
            return targetDetail;
        }

        public int getFaithfulnessScore() {
            return faithfulnessScore;
        }
    }

    public ComparisonModel(int sourceMediaId, int targetMediaId, float faithfulnessScore,
                           int faithfulnessPercentage, String verdict, int sourceVotes,
                           int targetVotes, List<ComparisonAspect> aspects) {
        this.sourceMediaId = sourceMediaId;
        this.targetMediaId = targetMediaId;
        this.faithfulnessScore = faithfulnessScore;
        this.faithfulnessPercentage = faithfulnessPercentage;
        this.verdict = verdict;
        this.sourceVotes = sourceVotes;
        this.targetVotes = targetVotes;
        this.aspects = aspects != null ? aspects : new ArrayList<>();
    }

    public int getSourceMediaId() {
        return sourceMediaId;
    }

    public int getTargetMediaId() {
        return targetMediaId;
    }

    public float getFaithfulnessScore() {
        return faithfulnessScore;
    }

    public int getFaithfulnessPercentage() {
        return faithfulnessPercentage;
    }

    public String getVerdict() {
        return verdict;
    }

    public int getSourceVotes() {
        return sourceVotes;
    }

    public int getTargetVotes() {
        return targetVotes;
    }

    public List<ComparisonAspect> getAspects() {
        return aspects;
    }

    public void setSourceVotes(int sourceVotes) {
        this.sourceVotes = sourceVotes;
    }

    public void setTargetVotes(int targetVotes) {
        this.targetVotes = targetVotes;
    }

    /**
     * Factory method returning detailed comparison data for any pair of media items.
     */
    public static ComparisonModel getComparisonFor(int sourceId, int targetId) {
        // Dune (Book ID 2, Movie ID 14)
        if ((sourceId == 2 && targetId == 14) || (sourceId == 14 && targetId == 2)) {
            boolean isBookSource = (sourceId == 2);
            return new ComparisonModel(
                    sourceId, targetId, 4.6f, 92,
                    "Visually breathtaking and narrative-loyal adaptation that captures the majestic scale, ecology, and political intrigue of Frank Herbert's epic novel.",
                    isBookSource ? 1840 : 1620,
                    isBookSource ? 1620 : 1840,
                    Arrays.asList(
                            new ComparisonAspect(
                                    "Plot & Narrative Scope",
                                    isBookSource ? "Extensive backstory detailing feudal house rivalries, Bene Gesserit prophecies, and Arrakis ecology."
                                                 : "Splits the original novel into two cinematic parts, focusing on Paul's rise among the Fremen.",
                                    isBookSource ? "Splits the original novel into two cinematic parts, focusing on Paul's rise among the Fremen."
                                                 : "Extensive backstory detailing feudal house rivalries, Bene Gesserit prophecies, and Arrakis ecology.",
                                    90),
                            new ComparisonAspect(
                                    "World Building & Atmosphere",
                                    isBookSource ? "Descriptive prose building intense atmosphere around water discipline, desert survival, and giant sandworms."
                                                 : "Groundbreaking scale with brutalist architecture, stunning desert cinematography, and realistic sound design.",
                                    isBookSource ? "Groundbreaking scale with brutalist architecture, stunning desert cinematography, and realistic sound design."
                                                 : "Descriptive prose building intense atmosphere around water discipline, desert survival, and giant sandworms.",
                                    95),
                            new ComparisonAspect(
                                    "Character Arcs & Introspection",
                                    isBookSource ? "Rich internal monologues exposing character fears, political calculations, and prescient visions."
                                                 : "Relies on subtle visual performances, body language, and Hans Zimmer's evocative score.",
                                    isBookSource ? "Relies on subtle visual performances, body language, and Hans Zimmer's evocative score."
                                                 : "Rich internal monologues exposing character fears, political calculations, and prescient visions.",
                                    88),
                            new ComparisonAspect(
                                    "Pacing & Action",
                                    isBookSource ? "Thoughtful, deliberate pacing building toward strategic conflicts."
                                                 : "High-stakes cinematic tension with visceral combat and grand military encounters.",
                                    isBookSource ? "High-stakes cinematic tension with visceral combat and grand military encounters."
                                                 : "Thoughtful, deliberate pacing building toward strategic conflicts.",
                                    93)
                    )
            );
        }

        // The Hunger Games (Book ID 1, Movie ID 13)
        if ((sourceId == 1 && targetId == 13) || (sourceId == 13 && targetId == 1)) {
            boolean isBookSource = (sourceId == 1);
            return new ComparisonModel(
                    sourceId, targetId, 4.4f, 88,
                    "A tense and faithful cinematic adaptation that accurately conveys Katniss Everdeen's grim battle for survival in Panem.",
                    isBookSource ? 1520 : 1380,
                    isBookSource ? 1380 : 1520,
                    Arrays.asList(
                            new ComparisonAspect(
                                    "Narrative Perspective",
                                    isBookSource ? "Intimate first-person POV inside Katniss's cynical thoughts, trauma, and survival instincts."
                                                 : "Expanded third-person perspective showing Gamemaker Seneca Crane and President Snow behind the scenes.",
                                    isBookSource ? "Expanded third-person perspective showing Gamemaker Seneca Crane and President Snow behind the scenes."
                                                 : "Intimate first-person POV inside Katniss's cynical thoughts, trauma, and survival instincts.",
                                    86),
                            new ComparisonAspect(
                                    "Arena Combat & Violence",
                                    isBookSource ? "Raw, harsh depiction of hunger, physical injuries, and brutal arena traps."
                                                 : "Action-oriented PG-13 sequences emphasizing emotional stakes and kinetic camera movement.",
                                    isBookSource ? "Action-oriented PG-13 sequences emphasizing emotional stakes and kinetic camera movement."
                                                 : "Raw, harsh depiction of hunger, physical injuries, and brutal arena traps.",
                                    90),
                            new ComparisonAspect(
                                    "Theme & World Building",
                                    isBookSource ? "Sharp commentary on reality TV exploitation, inequality, and rebellion."
                                                 : "Striking visual contrast between impoverished District 12 and futuristic Capitol extravagance.",
                                    isBookSource ? "Striking visual contrast between impoverished District 12 and futuristic Capitol extravagance."
                                                 : "Sharp commentary on reality TV exploitation, inequality, and rebellion.",
                                    89)
                    )
            );
        }

        // Game of Thrones (Book ID 8, TV ID 22)
        if ((sourceId == 8 && targetId == 22) || (sourceId == 22 && targetId == 8)) {
            boolean isBookSource = (sourceId == 8);
            return new ComparisonModel(
                    sourceId, targetId, 4.5f, 90,
                    "An iconic television adaptation that masterfully translates A Song of Ice and Fire's political complexity and dark fantasy tone.",
                    isBookSource ? 2100 : 1950,
                    isBookSource ? 1950 : 2100,
                    Arrays.asList(
                            new ComparisonAspect(
                                    "Plot & Character Scope",
                                    isBookSource ? "Vast web of point-of-view chapters, historical lore, and dozens of noble houses."
                                                 : "Streamlines side plots while delivering iconic character interactions and dialogue.",
                                    isBookSource ? "Streamlines side plots while delivering iconic character interactions and dialogue."
                                                 : "Vast web of point-of-view chapters, historical lore, and dozens of noble houses.",
                                    87),
                            new ComparisonAspect(
                                    "Production & Atmosphere",
                                    isBookSource ? "Rich description of Westerosi culture, heraldry, feast dishes, and armor."
                                                 : "Breathtaking global filming locations, gritty costumes, and stellar ensemble performances.",
                                    isBookSource ? "Breathtaking global filming locations, gritty costumes, and stellar ensemble performances."
                                                 : "Rich description of Westerosi culture, heraldry, feast dishes, and armor.",
                                    93)
                    )
            );
        }

        // Gone Girl (Book ID 3, Movie ID 15)
        if ((sourceId == 3 && targetId == 15) || (sourceId == 15 && targetId == 3)) {
            boolean isBookSource = (sourceId == 3);
            return new ComparisonModel(
                    sourceId, targetId, 4.7f, 94,
                    "Written for the screen by novel author Gillian Flynn, this psychological thriller adaptation captures every dark twist and turn.",
                    isBookSource ? 1290 : 1410,
                    isBookSource ? 1410 : 1290,
                    Arrays.asList(
                            new ComparisonAspect(
                                    "Screenplay & Structure",
                                    isBookSource ? "Dual diary entries and alternating timelines creating intense suspense."
                                                 : "Masterfully scripted by Flynn herself to retain the book's sharpest dialogue and plot twists.",
                                    isBookSource ? "Masterfully scripted by Flynn herself to retain the book's sharpest dialogue and plot twists."
                                                 : "Dual diary entries and alternating timelines creating intense suspense.",
                                    96),
                            new ComparisonAspect(
                                    "Tone & Direction",
                                    isBookSource ? "Dark satirical dissection of modern marriage and media frenzy."
                                                 : "David Fincher's sleek, icy directorial style with Trent Reznor's haunting score.",
                                    isBookSource ? "David Fincher's sleek, icy directorial style with Trent Reznor's haunting score."
                                                 : "Dark satirical dissection of modern marriage and media frenzy.",
                                    92)
                    )
            );
        }

        // Dynamic fallback for any pair
        return getFallbackComparison(sourceId, targetId);
    }

    private static ComparisonModel getFallbackComparison(int sourceId, int targetId) {
        MediaItem source = SampleMediaData.getMediaById(sourceId);
        MediaItem target = SampleMediaData.getMediaById(targetId);

        String sourceName = source != null ? source.getTitle() : "Original Work";
        String targetName = target != null ? target.getTitle() : "Adaptation";
        String sourceCat = source != null ? source.getCategory().name() : "Original";
        String targetCat = target != null ? target.getCategory().name() : "Adaptation";

        boolean isLowerSource = sourceId < targetId;

        return new ComparisonModel(
                sourceId, targetId, 4.2f, 84,
                "A compelling adaptation that captures the core essence of " + sourceName + " while tailoring the story for " + targetCat + " format.",
                isLowerSource ? 850 : 720,
                isLowerSource ? 720 : 850,
                Arrays.asList(
                        new ComparisonAspect(
                                "Narrative & Core Plot",
                                "The " + sourceCat + " details background lore and character development.",
                                "The " + targetCat + " focuses on key dramatic arcs and visual pacing.",
                                85),
                        new ComparisonAspect(
                                "Tone & Presentation",
                                "Conveys mood through detailed medium-specific narrative storytelling.",
                                "Utilizes audio-visual direction to enhance thematic resonance.",
                                83)
                )
        );
    }
}
