package ru.inno.earthquakes.model.settings;


import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import ru.inno.earthquakes.entities.Location;

/**
 * @author Artur Badretdinov (Gaket)
 *         05.08.17
 */
public class SettingsInteractor {

    private SettingsRepository repository;
    private PublishSubject<Boolean> dataUpdated;

    public SettingsInteractor(SettingsRepository repository) {
        this.repository = repository;
        dataUpdated = PublishSubject.create();
    }

    /**
     * @return maximal distance to which an Earthquake alert should be shown
     */
    public double getAlertMaxDistance() {
        return repository.getAlertMaxDistance();
    }

    /**
     * @return maximal magnitude from which an Earthquake alert should be shown
     */
    public double getAlertMinMagnitude() {
        return repository.getAlertMinMagnitude();
    }

    /**
     * Save settings for the alert
     *
     * @param maxDistance  in meters
     * @param minMagnitude in Richter scale
     */
    public void saveAlertSettings(double maxDistance, double minMagnitude) {
        repository.putAlertMaxDistance(maxDistance);
        repository.putAlertMinMagnitude(minMagnitude);
        dataUpdated.onNext(true);
    }

    /**
     * @return stream which notifies observers when settings are changed
     */
    public Observable<Boolean> getSettingsChangeObservable() {
        return dataUpdated;
    }

    /**
     * Get default location to use if there are problems getting user's actual location
     *
     * @return default location
     */
    public Location getDefaultLocation() {
        return repository.getDefaultLocation();
    }
}
