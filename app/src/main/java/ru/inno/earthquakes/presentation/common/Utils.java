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

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public static String formatDistanceString(Double distance) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        DecimalFormat formatter = new DecimalFormat("###,###", symbols);
        return formatter.format(distance);
    }
}
