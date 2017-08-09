package ru.inno.earthquakes.model.settings;

import android.content.SharedPreferences;

import javax.inject.Inject;

/**
 * @author Artur Badretdinov (Gaket)
 *         05.08.17
 */
public class SettingsRepository {

    private static final String KEY_MAX_DIST = "ru.inno.earthquakes.model.settings.max_dist";
    private static final String KEY_MIN_MAG = "ru.inno.earthquakes.model.settings.min_mag";

    private SharedPreferences sharedPreferences;

    @Inject
    public SettingsRepository(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    /**
     * @return maximal distance to which an Earthquake alert should be shown
     */
    public double getAlertMaxDistance() {
        return (double) sharedPreferences.getFloat(KEY_MAX_DIST, 0);
    }

    /**
     * @return maximal magnitude from which an Earthquake alert should be shown
     */
    public double getAlertMinMagnitude() {
        return (double) sharedPreferences.getFloat(KEY_MIN_MAG, 0);
    }

    /**
     * Save maximal distance for alert
     *
     * @param value in meters
     */
    void putAlertMaxDistance(double value) {
        sharedPreferences.edit()
                .putFloat(KEY_MAX_DIST, (float) value)
                .apply();
    }

    /**
     * Save minimal magnitude for alert
     *
     * @param value in Richter scale
     */
    void putAlertMinMagnitude(double value) {
        sharedPreferences.edit()
                .putFloat(KEY_MIN_MAG, (float) value)
                .apply();
    }
}
