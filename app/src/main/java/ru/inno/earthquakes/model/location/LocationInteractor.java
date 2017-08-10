package ru.inno.earthquakes.model.location;

import io.reactivex.Single;
import ru.inno.earthquakes.entities.Location.Coordinates;
import ru.inno.earthquakes.model.permissions.PermissionsRepository;
import ru.inno.earthquakes.model.settings.SettingsRepository;
import ru.inno.earthquakes.presentation.common.SchedulersProvider;

/**
 * @author Artur Badretdinov (Gaket)
 *         22.07.17.
 */
public class LocationInteractor {

    private final LocationRepository repository;
    private final PermissionsRepository permissionsRepository;
    private final SettingsRepository settingsRepository;
    private final SchedulersProvider schedulersProvider;

    public LocationInteractor(LocationRepository repository,
                              PermissionsRepository permissionsRepository,
                              SettingsRepository settingsRepository,
                              SchedulersProvider schedulersProvider) {
        this.repository = repository;
        this.permissionsRepository = permissionsRepository;
        this.settingsRepository = settingsRepository;
        this.schedulersProvider = schedulersProvider;
    }

    /**
     * Check for permission and get current coordinates.
     * Use states to show if location found successfully, or permission denied.
     *
     * @return state of request and current {@link Coordinates}.
     * In case of problems, default coordinates are returned. In this case, coordinates of Moscow, Russia
     */
    public Single<LocationAnswer> getCurrentCoordinates() {
        return permissionsRepository
                .requestLocationPermissions()
                .first(false)
                .flatMap(permGiven -> {
                    if (permGiven) {
                        return repository.getCurrentCoordinates()
                                .map(coordinates -> new LocationAnswer(coordinates, State.SUCCESS))
                                .onErrorReturnItem(new LocationAnswer(settingsRepository.getDefaultLocation().getCoords(), State.NO_DATA));
                    } else {
                        return Single.just(new LocationAnswer(settingsRepository.getDefaultLocation().getCoords(), State.PERMISSION_DENIED));
                    }
                })
                .subscribeOn(schedulersProvider.io());
    }

    /**
     * @return if location services, needed for the app are available
     */
    public Single<Boolean> checkLocationServicesAvailability() {
        return Single.just(repository.checkPlayServicesAvailable())
                .subscribeOn(schedulersProvider.io());
    }

    /**
     * @return status code from the location services
     */
    public Single<Integer> getLocationServicesStatus() {
        return Single.just(repository.getPlayServicesStatus())
                .subscribeOn(schedulersProvider.io());
    }

    /**
     * Class containing coordinates and their state
     */
    public static class LocationAnswer {
        private Coordinates coordinates;
        private State state;

        public LocationAnswer(Coordinates coordinates, State state) {
            this.coordinates = coordinates;
            this.state = state;
        }

        public Coordinates getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(Coordinates coordinates) {
            this.coordinates = coordinates;
        }

        public State getState() {
            return state;
        }

        public void setState(State state) {
            this.state = state;
        }
    }

    public enum State {
        SUCCESS,
        PERMISSION_DENIED,
        NO_DATA
    }
}
