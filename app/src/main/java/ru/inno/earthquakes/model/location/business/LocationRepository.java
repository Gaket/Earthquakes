package ru.inno.earthquakes.model.location.business;

import io.reactivex.Observable;
import ru.inno.earthquakes.entity.Location;

/**
 * @author Artur Badretdinov (Gaket)
 *         20.07.17.
 */
public interface LocationRepository {

    Observable<Location.Coordinates> getCurrentCoordinates();
}
