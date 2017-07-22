package ru.inno.earthquakes.model.earthquakes.business;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.inno.earthquakes.entity.Earthquake;

/**
 * @author Artur Badretdinov (Gaket)
 *         22.07.17.
 */
public class EarthquakesInteractor {

    private EarthquakesRepository earthquakesRepository;

    @Inject
    public EarthquakesInteractor(EarthquakesRepository earthquakesRepository) {
        this.earthquakesRepository = earthquakesRepository;
    }

    Observable<List<Earthquake>> getTodaysEartquakes() {
        return earthquakesRepository.getTodaysEarthquakes();
    }
}
