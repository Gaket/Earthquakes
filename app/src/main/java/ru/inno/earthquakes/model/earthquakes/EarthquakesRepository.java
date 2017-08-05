package ru.inno.earthquakes.model.earthquakes;

import java.util.List;

import io.reactivex.Single;
import ru.inno.earthquakes.entities.Earthquake;

/**
 * @author Artur Badretdinov (Gaket)
 *         20.07.17.
 */
public interface EarthquakesRepository {

    Single<List<Earthquake>> getTodaysEarthquakes();

    Single<List<Earthquake>> getCachedTodaysEarthquakes();
}
