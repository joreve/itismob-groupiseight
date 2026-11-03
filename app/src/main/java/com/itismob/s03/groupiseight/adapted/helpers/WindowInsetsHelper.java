package com.itismob.s03.groupiseight.adapted.helpers;

import android.graphics.Color;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/** Shared edge-to-edge setup so every screen handles status bar, cutouts and keyboard the same way. */
public final class WindowInsetsHelper {

    private WindowInsetsHelper() {
    }

    /**
     * Draws the app behind the system bars. The light style (dark icons) is forced because the
     * temporary theme is white; this also keeps icons visible when the phone is in dark mode.
     */
    public static void enableEdgeToEdge(AppCompatActivity activity) {
        EdgeToEdge.enable(activity,
                SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
                SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT));
    }

    /**
     * Pads the root view so content does not sit under the status bar or display cutout.
     *
     * @param includeBottom true for screens without a bottom navigation bar: also pads the
     *                      bottom for the navigation bar and keyboard. Screens with a
     *                      BottomNavigationView pass false because that view handles it itself.
     */
    public static void applySystemBarPadding(View root, boolean includeBottom) {
        ViewCompat.setOnApplyWindowInsetsListener(root, (view, insets) -> {
            Insets bars = insets.getInsets(
                    WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
            Insets keyboard = insets.getInsets(WindowInsetsCompat.Type.ime());
            int bottom = includeBottom ? Math.max(bars.bottom, keyboard.bottom) : 0;
            view.setPadding(bars.left, bars.top, bars.right, bottom);
            return insets;
        });
    }
}
