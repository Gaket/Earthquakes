package ru.inno.earthquakes.model.settings;

import android.content.SharedPreferences;

import ru.inno.earthquakes.entities.Location;

/**
 * @author Artur Badretdinov (Gaket)
 *         05.08.17
 */
public class SettingsRepository {

    private static final String KEY_MAX_DIST = "ru.inno.earthquakes.model.settings.max_dist";
    private static final String KEY_MIN_MAG = "ru.inno.earthquakes.model.settings.min_mag";

    private SharedPreferences sharedPreferences;

    // Return Moscow coordinates by default. Later we can create a feature where user enters it
    private final String DEFAULT_CITY = "Moscow";
    private final Location.Coordinates DEFAULT_COORDINATES = new Location.Coordinates(55.755826, 37.6173);

    public SettingsRepository(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    /**
     * @return maximal distance to which an Earthquake alert should be shown
     */
    public double getAlertMaxDistance() {
        return getDoubleFromLongFromPrefs(KEY_MAX_DIST);
    }

    /**
     * @return maximal magnitude from which an Earthquake alert should be shown
     */
    public double getAlertMinMagnitude() {
        return getDoubleFromLongFromPrefs(KEY_MIN_MAG);
    }

    /**
     * Save maximal distance for alert
     *
     * @param value in meters
     */
    public void putAlertMaxDistance(double value) {
        putDoubleAsLongToPrefs(KEY_MAX_DIST, value);
    }

    /**
     * Save minimal magnitude for alert
     *
     * @param value in Richter scale
     */
    public void putAlertMinMagnitude(double value) {
        putDoubleAsLongToPrefs(KEY_MIN_MAG, value);
    }

    /**
     * Get default location to use if there are problems getting user's actual location
     *Å
     * @return default location
     */
    public Location getDefaultLocation() {
        return new Location(DEFAULT_CITY, DEFAULT_COORDINATES);
    }

    /**
     * Convenient method to store double without losing precision while converting to float
     */
    private void putDoubleAsLongToPrefs(String key, double value) {
        sharedPreferences.edit()
                .putLong(key, Double.doubleToRawLongBits(value))
                .apply();
    }

    /**
     * Convenient method to read double without losing precision while converting to float
     */
    private double getDoubleFromLongFromPrefs(String key) {
        if ( !sharedPreferences.contains(key))
            return 0;
        return Double.longBitsToDouble(sharedPreferences.getLong(key, 0));
    }

}
