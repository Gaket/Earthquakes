package ru.inno.earthquakes.presentation.settings;

import android.content.Context;
import androidx.annotation.StringRes;

/**
 * Created by Artur (gaket) on 2019-11-15.
 */
public interface ResourceFactory {

  String getString(@StringRes int resId);
}
