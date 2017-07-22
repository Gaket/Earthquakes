package ru.inno.earthquakes.model.earthquakes.data;

import java.util.List;

import io.reactivex.Observable;
import ru.inno.earthquakes.entity.Earthquake;
import ru.inno.earthquakes.model.earthquakes.business.EarthquakesRepository;
import ru.inno.earthquakes.model.earthquakes.data.rawmodels.EarthquakesResponse;

/**
 * @author Artur Badretdinov (Gaket)
 *         21.07.2017.
 */
public class EarthquakesRepositoryImpl implements EarthquakesRepository {

    private EarthquakesApiService apiService;
    private EarthquakesMapper earthquakesMapper;

    public EarthquakesRepositoryImpl(EarthquakesApiService apiService, EarthquakesMapper earthquakesMapper) {
        this.apiService = apiService;
        this.earthquakesMapper = earthquakesMapper;
    }

    @Override
    public Observable<List<Earthquake>> getTodaysEarthquakes() {
        return apiService.getEarthquakes()
                .map(EarthquakesResponse::getFeatures)
                .flattenAsObservable(items -> items)
                .map(earthquakesMapper::map)
                .toList()
                .toObservable();
    }
}
