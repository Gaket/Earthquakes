package ru.inno.earthquakes.presentation;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * @author Artur Badretdinov (Gaket)
 *         06.08.17
 */
class ImageUtils {

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
}
