package ru.inno.earthquakes.presentation.settings;

import ru.inno.earthquakes.R;

/**
 * Created by Artur (gaket) on 2019-11-15.
 */
public enum SettingsMessage {

  DISTANCE_ERROR(R.string.error_settings_distance),
  NOT_READY_ERROR(R.string.error_not_implemented);

  int resId;

  SettingsMessage(int resId) {
    this.resId = resId;
  }

  public int getResId() {
    return resId;
  }
}
