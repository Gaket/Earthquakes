package ru.inno.earthquakes.model.settings;

/**
 * @author Artur Badretdinov (Gaket)
 *         06.08.17
 */
interface SettingsRepositoryInt {

    double getAlertMaxDistance();

    double getAlertMinMagnitude();

    void putAlertMaxDistance(double value);

    void putAlertMinMagnitude(double value);
}
