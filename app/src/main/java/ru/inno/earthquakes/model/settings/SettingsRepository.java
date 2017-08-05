package ru.inno.earthquakes.model.settings;

import android.content.SharedPreferences;

import javax.inject.Inject;

import io.reactivex.Single;

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

    public Single<Double> getAlertMaxDistance() {
        return Single.fromCallable(this::getMaxDist);
    }

    public Single<Double> getAlertMinMagnitude() {
        return Single.fromCallable(this::getMinMag);
    }

    public void putAlertMaxDistance(double value) {
        sharedPreferences.edit()
                .putFloat(KEY_MAX_DIST, (float)value)
                .apply();
    }

    public void putAlertMinMagnitude(double value) {
        sharedPreferences.edit()
                .putFloat(KEY_MIN_MAG, (float)value)
                .apply();
    }

    private Double getMaxDist() {
        return (double) sharedPreferences.getFloat(KEY_MAX_DIST, 0);
    }

    private Double getMinMag() {
        return (double) sharedPreferences.getFloat(KEY_MIN_MAG, 0);
    }
}
