package ru.inno.earthquakes.model.settings;

import javax.inject.Inject;

/**
 * @author Artur Badretdinov (Gaket)
 *         05.08.17
 */
public class SettingsInteractor {

    private SettingsRepositoryInt repository;

    @Inject
    public SettingsInteractor(SettingsRepositoryInt repository) {
        this.repository = repository;
    }

    public double getAlertMaxDistance() {
        return repository.getAlertMaxDistance();
    }

    public double getAlertMinMagnitude() {
        return repository.getAlertMinMagnitude();
    }

    public void saveAlertSettings(double maxDistance, double minMagnitude) {
        repository.putAlertMaxDistance(maxDistance);
        repository.putAlertMinMagnitude(minMagnitude);
    }
}
