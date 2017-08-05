package ru.inno.earthquakes.model.settings;

import javax.inject.Inject;

import io.reactivex.Single;

/**
 * @author Artur Badretdinov (Gaket)
 *         05.08.17
 */
public class SettingsInteractor {

    private SettingsRepository repository;

    @Inject
    public SettingsInteractor(SettingsRepository repository) {
        this.repository = repository;
    }

    public Single<Double> getAlertMaxDistance() {
        return repository.getAlertMaxDistance();
    }

    public Single<Double> getAlertMinMagnitude() {
        return repository.getAlertMinMagnitude();
    }

    public void saveAlertSettings(double maxDistance, double minMagnitude) {
        repository.putAlertMaxDistance(maxDistance);
        repository.putAlertMinMagnitude(minMagnitude);
    }
}
