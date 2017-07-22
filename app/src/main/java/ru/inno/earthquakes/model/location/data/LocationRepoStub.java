package ru.inno.earthquakes.model.location.data;

import io.reactivex.Observable;
import ru.inno.earthquakes.entity.Location;
import ru.inno.earthquakes.model.location.business.LocationRepository;

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
