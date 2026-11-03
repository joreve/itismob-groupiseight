package com.itismob.s03.groupiseight.adapted.helpers;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputLayout;

/** Removes the error from a TextInputLayout as soon as the user edits the field again. */
public class ClearErrorTextWatcher implements TextWatcher {

    private final TextInputLayout layout;

    public ClearErrorTextWatcher(TextInputLayout layout) {
        this.layout = layout;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        layout.setError(null);
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
