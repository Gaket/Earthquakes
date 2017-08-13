package ru.inno.earthquakes.model.earthquakes;

import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import ru.inno.earthquakes.entities.EarthquakeWithDist;
import ru.inno.earthquakes.entities.Location;
import ru.inno.earthquakes.model.EntitiesWrapper;
import ru.inno.earthquakes.model.EntitiesWrapper.State;
import ru.inno.earthquakes.model.settings.SettingsInteractor;
import ru.inno.earthquakes.model.settings.SettingsRepository;
import ru.inno.earthquakes.presentation.common.SchedulersProvider;

/**
 * @author Artur Badretdinov (Gaket)
 *         22.07.17.
 */
public class EarthquakesInteractor {

    private EarthquakesRepository earthquakesRepository;
    private SettingsRepository settingsRepository;
    private Comparator<EarthquakeWithDist> distanceComparator;
    private SchedulersProvider schedulersProvider;

    public EarthquakesInteractor(EarthquakesRepository earthquakesRepository, 
                                 SettingsRepository settingsRepository, 
                                 SchedulersProvider schedulersProvider) {
        this.earthquakesRepository = earthquakesRepository;
        this.settingsRepository = settingsRepository;
        this.schedulersProvider = schedulersProvider;
        distanceComparator = (a, b) -> Double.compare(a.getDistance(), b.getDistance());
    }

    /**
     * Get the closest to the given position Earthquake that satisfies program settings
     * (maximal distance and minimal magnitude, details in {@link SettingsInteractor})
     *
     * @param coords of user
     * @return {@link EntitiesWrapper} with different states (see {@link State})
     * and the closest {@link EarthquakeWithDist} if it was found
     */
    public Single<EntitiesWrapper<EarthquakeWithDist>> getEarthquakeAlert(Location.Coordinates coords) {
        return getApiDataSorted(coords, distanceComparator)
                .flattenAsObservable(earthquakeWithDists -> earthquakeWithDists)
                .filter(earthquakeWithDist -> earthquakeWithDist.getDistance() < settingsRepository.getAlertMaxDistance())
                .filter(earthquakeWithDist -> earthquakeWithDist.getMagnitude() >= settingsRepository.getAlertMinMagnitude())
                .toList()
                .map(earthquakeWithDists -> earthquakeWithDists.isEmpty() ?
                        new EntitiesWrapper<EarthquakeWithDist>(State.EMPTY, null) :
                        new EntitiesWrapper<EarthquakeWithDist>(State.SUCCESS, earthquakeWithDists.get(0)))
                .onErrorReturnItem(new EntitiesWrapper<EarthquakeWithDist>(State.ERROR_NETWORK, null))
                .subscribeOn(schedulersProvider.io());
    }

    /**
     * Get all earthquakes sorted by location
     *
     * @param coords of user
     * @return {@link Observable} that first emits cached data, then - actual data
     * if it is successfully downloaded or an {@link EntitiesWrapper} with an error state otherwise.
     */
    public Observable<EntitiesWrapper<List<EarthquakeWithDist>>> getTodaysEartquakesSortedByLocation(Location.Coordinates coords) {
        return getCachedDataSorted(coords, distanceComparator)
                .map(earthquakeWithDists -> new EntitiesWrapper<>(State.LOADING, earthquakeWithDists))
                .concatWith(getApiDataSorted(coords, distanceComparator)
                        .map(earthquakeWithDists -> new EntitiesWrapper<>(State.SUCCESS, earthquakeWithDists))
                        .onErrorReturnItem(new EntitiesWrapper<>(State.ERROR_NETWORK, null)))
                .toObservable()
                .subscribeOn(schedulersProvider.io());
    }

    /**
     * Download earthquakes list from server, calculate distance to the given location,
     * and return list of {@link EarthquakeWithDist} sorted by distance
     *
     * @param coords
     * @param distanceComparator
     * @return
     */
    private Single<List<EarthquakeWithDist>> getApiDataSorted(Location.Coordinates coords, Comparator<EarthquakeWithDist> distanceComparator) {
        return earthquakesRepository.getTodaysEarthquakesFromApi()
                .flattenAsObservable(earthquakes -> earthquakes)
                .map(earthquakeEntity -> new EarthquakeWithDist(earthquakeEntity, coords))
                .toSortedList(distanceComparator)
                .subscribeOn(schedulersProvider.io());
    }

    /**
     * Read earthquakes from cache, calculate distance to the given location,
     * and return list of {@link EarthquakeWithDist} sorted by distance
     *
     * @param coords
     * @param distanceComparator
     * @return
     */
    private Single<List<EarthquakeWithDist>> getCachedDataSorted(Location.Coordinates coords, Comparator<EarthquakeWithDist> distanceComparator) {
        return earthquakesRepository.getCachedTodaysEarthquakes()
                .flattenAsObservable(earthquakes -> earthquakes)
                .map(earthquakeEntity -> new EarthquakeWithDist(earthquakeEntity, coords))
                .toSortedList(distanceComparator)
                .subscribeOn(schedulersProvider.io());
    }
}
