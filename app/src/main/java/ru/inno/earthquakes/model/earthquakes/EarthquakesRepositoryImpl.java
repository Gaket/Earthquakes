package ru.inno.earthquakes.model.earthquakes;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import ru.inno.earthquakes.entities.Earthquake;
import ru.inno.earthquakes.model.mappers.EarthquakesMapper;
import ru.inno.earthquakes.model.models.network.EarthquakesResponse;
import timber.log.Timber;

/**
 * @author Artur Badretdinov (Gaket)
 *         21.07.2017.
 */
public class EarthquakesRepositoryImpl implements EarthquakesRepository {

    private EarthquakesApiService apiService;
    private EarthquakesMapper earthquakesMapper;
    private EarthquakesCache earthquakesCache;

    @Inject
    public EarthquakesRepositoryImpl(EarthquakesApiService apiService, EarthquakesMapper earthquakesMapper, EarthquakesCache earthquakesCache) {
        this.apiService = apiService;
        this.earthquakesMapper = earthquakesMapper;
        this.earthquakesCache = earthquakesCache;
    }

    @Override
    public Single<List<Earthquake>> getNetworkTodaysEarthquakes() {
        return apiService.getEarthquakes()
                .map(EarthquakesResponse::getFeatures)
                .flattenAsObservable(items -> items)
                .map(earthquakesMapper::earthquakeDataToEntity)
                .toList()
                .doOnSuccess(earthquakeEntities -> {
                    earthquakesCache.clearCache();
                    earthquakesCache.putEarthquakes(earthquakeEntities);
                })
                .doOnSuccess(earthquakeEntities -> Timber.d("%d entities came from server", earthquakeEntities.size()));
    }

    @Override
    public Single<List<Earthquake>> getCachedTodaysEarthquakes() {
        return earthquakesCache.getEarthquakes();
    }
}
