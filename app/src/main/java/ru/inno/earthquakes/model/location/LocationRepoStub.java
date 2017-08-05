package ru.inno.earthquakes.model.location;

import io.reactivex.Single;
import ru.inno.earthquakes.entities.Location;

/**
 * @author Artur Badretdinov (Gaket)
 *         22.07.17.
 */
public class LocationRepoStub implements LocationRepository {

    @Override
    public Single<Location.Coordinates> getCurrentCoordinates() {
        return Single.just(new Location.Coordinates(0, 0));
    }
}
