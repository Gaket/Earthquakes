package ru.inno.earthquakes.model.location;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.inno.earthquakes.entities.Location;

/**
 * @author Artur Badretdinov (Gaket)
 *         22.07.17.
 */
public class LocationInteractor {

    private LocationRepository repository;

    @Inject
    public LocationInteractor(LocationRepository repository) {
        this.repository = repository;
    }

    public Observable<Location.Coordinates> getCurrentCoordinates() {
        return repository.getCurrentCoordinates();
    }
}
