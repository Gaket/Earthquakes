package ru.inno.earthquakes.model.earthquakes;

import java.util.List;

import io.reactivex.Single;
import ru.inno.earthquakes.entities.EarthquakeEntity;

/**
 * @author Artur Badretdinov (Gaket)
 *         20.07.17.
 */
public interface EarthquakesRepository {

    Single<List<EarthquakeEntity>> getTodaysEarthquakes();

    Single<List<EarthquakeEntity>> getCachedTodaysEarthquakes();
}
