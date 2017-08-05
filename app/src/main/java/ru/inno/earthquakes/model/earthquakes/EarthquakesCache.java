package ru.inno.earthquakes.model.earthquakes;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.Query;
import io.objectbox.rx.RxQuery;
import io.reactivex.Single;
import ru.inno.earthquakes.entities.EarthquakeEntity;
import ru.inno.earthquakes.model.dbobjects.Earthquake;
import ru.inno.earthquakes.model.mappers.EarthquakesMapper;
import timber.log.Timber;

/**
 * @author Artur Badretdinov (Gaket)
 *         05.08.17
 */
public class EarthquakesCache {

    private final Box<Earthquake> earthquakeBox;
    private final EarthquakesMapper earthquakesMapper;

    @Inject
    public EarthquakesCache(BoxStore boxStore, EarthquakesMapper earthquakesMapper) {
        earthquakeBox = boxStore.boxFor(Earthquake.class);
        this.earthquakesMapper = earthquakesMapper;
    }

    public void clearCache() {
        earthquakeBox.removeAll();
    }

    public void putEarthquakes(List<EarthquakeEntity> earthquakeEntities) {
        earthquakeBox.put(earthquakesMapper.entitiesToDb(earthquakeEntities));
    }

    public Single<List<EarthquakeEntity>> getEarthquakes() {
        Query<Earthquake> query = earthquakeBox.query().build();
        return RxQuery.observable(query)
                .map(earthquakesMapper::earthquakesToEntities)
                .first(new ArrayList<>())
                .doOnSuccess(earthquakeEntities -> Timber.d("%d entities are in cache", earthquakeEntities.size()));
    }
}
