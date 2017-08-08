package ru.inno.earthquakes.model.location;

import javax.inject.Inject;

import io.reactivex.Single;
import ru.inno.earthquakes.entities.Location;
import ru.inno.earthquakes.model.permissions.PermissionsRepository;

/**
 * @author Artur Badretdinov (Gaket)
 *         22.07.17.
 */
public class LocationInteractor {

    // Return Moscow coordinates by default
    private final Location.Coordinates DEFAULT_COORDINATES = new Location.Coordinates(55.755826, 37.6173);
    private LocationRepository repository;
    private PermissionsRepository permissionsRepository;

    @Inject
    public LocationInteractor(LocationRepository repository, PermissionsRepository permissionsRepository) {
        this.repository = repository;
        this.permissionsRepository = permissionsRepository;
    }

    public Single<LocationAnswer> getCurrentCoordinates() {
        return permissionsRepository
                .requestLocationPermissions()
                .first(false)
                .flatMap(permGiven -> {
                    if (permGiven) {
                        return repository.getCurrentCoordinates()
                                .map(coordinates -> new LocationAnswer(coordinates, State.SUCCESS))
                                .onErrorReturnItem(new LocationAnswer(DEFAULT_COORDINATES, State.NO_DATA));
                    } else {
                        return Single.just(new LocationAnswer(DEFAULT_COORDINATES, State.PERMISSION_DENIED));
                    }
                });
    }

    public static class LocationAnswer {
        private Location.Coordinates coordinates;
        private State state;

        public LocationAnswer(Location.Coordinates coordinates, State state) {
            this.coordinates = coordinates;
            this.state = state;
        }

        public Location.Coordinates getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(Location.Coordinates coordinates) {
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
