package ru.inno.earthquakes.presentation.common;

import android.content.Context;
import android.util.DisplayMetrics;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * @author Artur Badretdinov (Gaket)
 *         06.08.17
 */
public final class Utils {

    private Utils(){};

    /**
     * Converts values from pixels to density independent pixels
     */
    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    /**
     * Formats doule to {@link String} like "1 220 000"
     */
    public static String formatDistanceString(Double distance) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        DecimalFormat formatter = new DecimalFormat("###,###", symbols);
        return formatter.format(distance);
    }

    /**
     * Returns whether the given CharSequence contains only digits.
     */
    public static boolean isDigitsOnly(CharSequence str) {
        final int len = str.length();
        for (int cp, i = 0; i < len; i += Character.charCount(cp)) {
            cp = Character.codePointAt(str, i);
            if (!Character.isDigit(cp)) {
                return false;
            }
        }
        return true;
    }
}
