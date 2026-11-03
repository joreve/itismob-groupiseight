package com.itismob.s03.groupiseight.adapted.models;

import com.itismob.s03.groupiseight.adapted.R;

/** The four kinds of media Adapted tracks. Each one knows the string resource used to display it. */
public enum MediaCategory {
    BOOK(R.string.category_book),
    MOVIE(R.string.category_movie),
    TV_SERIES(R.string.category_tv_series),
    MUSIC(R.string.category_music);

    private final int labelResId;

    MediaCategory(int labelResId) {
        this.labelResId = labelResId;
    }

    /** Singular display name, e.g. "Book" or "TV Series". */
    public int getLabelResId() {
        return labelResId;
    }
}
