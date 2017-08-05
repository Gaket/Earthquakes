package ru.inno.earthquakes.model.earthquakes;

import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import ru.inno.earthquakes.entities.EarthquakeWithDist;
import ru.inno.earthquakes.entities.Location;
import ru.inno.earthquakes.model.EntitiesWrapper;

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

    public Observable<EntitiesWrapper<List<EarthquakeWithDist>>> getTodaysEartquakesSortedByLocation(Location.Coordinates coords) {
        Comparator<EarthquakeWithDist> distanceComparator = (a, b) -> Double.compare(a.getDistance(), b.getDistance());
        return getCachedData(EntitiesWrapper.State.LOADING, coords, distanceComparator)
                .mergeWith(getApiData(coords, distanceComparator)
                        .onErrorResumeNext((t) -> getCachedData(EntitiesWrapper.State.ERROR_NETWORK, coords, distanceComparator)))
                .toObservable();
    }

    private Single<EntitiesWrapper<List<EarthquakeWithDist>>> getApiData(Location.Coordinates coords, Comparator<EarthquakeWithDist> distanceComparator) {
        return repository.getTodaysEarthquakes()
                .flattenAsObservable(earthquakes -> earthquakes)
                .map(earthquakeEntity -> new EarthquakeWithDist(earthquakeEntity, coords))
                .toSortedList(distanceComparator)
                .map(earthquakeWithDists -> new EntitiesWrapper<>(EntitiesWrapper.State.SUCCESS, earthquakeWithDists));
    }

    private Single<EntitiesWrapper<List<EarthquakeWithDist>>> getCachedData(EntitiesWrapper.State state, Location.Coordinates coords, Comparator<EarthquakeWithDist> distanceComparator) {
        return repository.getCachedTodaysEarthquakes()
                .flattenAsObservable(earthquakes -> earthquakes)
                .map(earthquakeEntity -> new EarthquakeWithDist(earthquakeEntity, coords))
                .toSortedList(distanceComparator)
                .map(earthquakeWithDists -> new EntitiesWrapper<>(state, earthquakeWithDists));
    }


}
