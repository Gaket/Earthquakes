package ru.inno.earthquakes.model.earthquakes;

import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import ru.inno.earthquakes.entities.EarthquakeWithDist;
import ru.inno.earthquakes.entities.Location;
import ru.inno.earthquakes.model.EntitiesWrapper;
import ru.inno.earthquakes.model.settings.SettingsRepository;

/**
 * @author Artur Badretdinov (Gaket)
 *         22.07.17.
 */
public class EarthquakesInteractor {

    private EarthquakesRepository repository;
    private SettingsRepository settingsRepository;
    private Comparator<EarthquakeWithDist> distanceComparator;

    @Inject
    public EarthquakesInteractor(EarthquakesRepository repository, SettingsRepository settingsRepository) {
        this.repository = repository;
        this.settingsRepository = settingsRepository;
        distanceComparator = (a, b) -> Double.compare(a.getDistance(), b.getDistance());
    }

    public Single<EntitiesWrapper<EarthquakeWithDist>> getEarthquakeAlert(Location.Coordinates coords) {
        return getApiData(coords, distanceComparator)
                .map(EntitiesWrapper::getData)
//                .flattenAsObservable(earthquakeWithDists -> earthquakeWithDists)
//                .filter(earthquakeWithDist -> earthquakeWithDist.getDistance() < )
                .map(earthquakeWithDists -> {
                    if (earthquakeWithDists.isEmpty()) {
                        return new EntitiesWrapper<EarthquakeWithDist>(EntitiesWrapper.State.EMPTY, null);
                    } else {
                        return new EntitiesWrapper<EarthquakeWithDist>(EntitiesWrapper.State.SUCCESS, earthquakeWithDists.get(0));
                    }
                })
                .onErrorReturnItem(new EntitiesWrapper<EarthquakeWithDist>(EntitiesWrapper.State.ERROR_NETWORK, null));
    }

    public Observable<EntitiesWrapper<List<EarthquakeWithDist>>> getTodaysEartquakesSortedByLocation(Location.Coordinates coords) {
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
