package ru.inno.earthquakes.model.location;

import io.reactivex.Single;
import ru.inno.earthquakes.entities.Location;

/**
 * @author Artur Badretdinov (Gaket)
 *         20.07.17.
 */
public interface LocationRepository {

    Single<Location.Coordinates> getCurrentCoordinates();
}
