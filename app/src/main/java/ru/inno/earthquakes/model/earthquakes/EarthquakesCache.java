package ru.inno.earthquakes.model.earthquakes;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.Query;
import io.objectbox.rx.RxQuery;
import io.reactivex.Single;
import ru.inno.earthquakes.entities.Earthquake;
import ru.inno.earthquakes.model.models.db.EarthquakeDb;
import ru.inno.earthquakes.model.mappers.EarthquakesMapper;
import timber.log.Timber;

/**
 * @author Artur Badretdinov (Gaket)
 *         05.08.17
 */
public class EarthquakesCache {

    private final Box<EarthquakeDb> earthquakeBox;
    private final EarthquakesMapper earthquakesMapper;

    @Inject
    public EarthquakesCache(BoxStore boxStore, EarthquakesMapper earthquakesMapper) {
        earthquakeBox = boxStore.boxFor(EarthquakeDb.class);
        this.earthquakesMapper = earthquakesMapper;
    }

    void clearCache() {
        earthquakeBox.removeAll();
    }

    void putEarthquakes(List<Earthquake> earthquakeEntities) {
        earthquakeBox.put(earthquakesMapper.entitiesToDb(earthquakeEntities));
    }

    Single<List<EarthquakeDb>> getEarthquakes() {
        Query<EarthquakeDb> query = earthquakeBox.query().build();
        return RxQuery.observable(query)
                .first(new ArrayList<>())
                .doOnSuccess(earthquakeEntities -> Timber.d("%d entities are in cache", earthquakeEntities.size()));
    }
}
