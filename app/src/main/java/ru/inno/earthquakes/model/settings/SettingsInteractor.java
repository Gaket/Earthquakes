package ru.inno.earthquakes.model.settings;


import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * @author Artur Badretdinov (Gaket)
 *         05.08.17
 */
public class SettingsInteractor {

    private SettingsRepositoryInt repository;
    private PublishSubject<Boolean> dataUpdated;

    @Inject
    public SettingsInteractor(SettingsRepositoryInt repository) {
        this.repository = repository;
        dataUpdated = PublishSubject.create();
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
        dataUpdated.onNext(true);
    }

    public Observable<Boolean> getSettingsChangeObservable() {
        return dataUpdated;
    }
}
