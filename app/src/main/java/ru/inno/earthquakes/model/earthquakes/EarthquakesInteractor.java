package ru.inno.earthquakes.model.earthquakes;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.inno.earthquakes.entities.Earthquake;
import ru.inno.earthquakes.entities.EarthquakeWithDist;
import ru.inno.earthquakes.entities.Location;

/**
 * @author Artur Badretdinov (Gaket)
 *         22.07.17.
 */
public class EarthquakesInteractor {

    private EarthquakesRepository repository;

    @Inject
    public EarthquakesInteractor(EarthquakesRepository repository) {
        this.repository = repository;
    }

    public Observable<List<Earthquake>> getTodaysEartquakes() {
        return repository.getTodaysEarthquakes();
    }

    public Observable<List<EarthquakeWithDist>> getTodaysEartquakesSortedByLocation(Location.Coordinates coords) {
        return repository.getTodaysEarthquakes()
                .flatMapIterable(earthquakes -> earthquakes)
                .map(earthquake -> new EarthquakeWithDist(earthquake, coords))
                .sorted((a, b) -> Double.compare(a.getDistance(), b.getDistance()))
                .toList()
                .toObservable();
    }

}