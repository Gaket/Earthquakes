package ru.inno.earthquakes.model.location;

import javax.inject.Inject;

import io.reactivex.Single;
import ru.inno.earthquakes.entities.Location;
import ru.inno.earthquakes.model.PermissionsManager;

/**
 * @author Artur Badretdinov (Gaket)
 *         22.07.17.
 */
public class LocationInteractor {

    private LocationRepository repository;
    private PermissionsManager permissionsManager;

    @Inject
    public LocationInteractor(LocationRepository repository, PermissionsManager permissionsManager) {
        this.repository = repository;
        this.permissionsManager = permissionsManager;
    }

    public Single<Location.Coordinates> getCurrentCoordinates() {
        return permissionsManager
                .requestLocationPermissions()
                .first(false)
                .flatMap(permGiven -> {
                    if (permGiven) {
                        return repository.getCurrentCoordinates()
                                // Return Moscow coordinates by default
                                .onErrorReturnItem(new Location.Coordinates(55.755826, 37.6173));
                    } else {
                        // Return Moscow coordinates by default
                        return Single.just(new Location.Coordinates(55.755826, 37.6173));
                    }
                });
    }
}
