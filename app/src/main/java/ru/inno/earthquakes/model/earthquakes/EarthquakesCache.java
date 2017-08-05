package ru.inno.earthquakes.model.earthquakes;

import java.util.List;

import io.reactivex.Single;
import ru.inno.earthquakes.entities.Earthquake;

/**
 * @author Artur Badretdinov (Gaket)
 *         05.08.17
 */
interface EarthquakesCache {
    void clearCache();

    void putEarthquakes(List<Earthquake> earthquakeEntities);

    Single<List<Earthquake>> getEarthquakes();
}
