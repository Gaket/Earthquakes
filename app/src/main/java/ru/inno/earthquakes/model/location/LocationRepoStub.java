package ru.inno.earthquakes.model.location;

import io.reactivex.Observable;
import ru.inno.earthquakes.entities.Location;
import ru.inno.earthquakes.model.location.LocationRepository;

/**
 * @author Artur Badretdinov (Gaket)
 *         22.07.17.
 */
public class LocationRepoStub implements LocationRepository {


    @Override
    public Observable<Location.Coordinates> getCurrentCoordinates() {
        return Observable.just(new Location.Coordinates(0, 0));
    }
}
