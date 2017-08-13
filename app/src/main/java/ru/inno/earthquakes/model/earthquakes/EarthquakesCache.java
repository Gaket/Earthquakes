package ru.inno.earthquakes.model.earthquakes;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.Query;
import io.objectbox.rx.RxQuery;
import io.reactivex.Single;
import java.util.ArrayList;
import java.util.List;
import ru.inno.earthquakes.entities.Earthquake;
import ru.inno.earthquakes.model.mappers.EarthquakesMapper;
import ru.inno.earthquakes.model.models.db.EarthquakeDb;
import timber.log.Timber;

/**
 * Local cache to persist the data
 *
 * @author Artur Badretdinov (Gaket) 05.08.17
 */
public class EarthquakesCache {

  private final Box<EarthquakeDb> earthquakeBox;
  private final EarthquakesMapper earthquakesMapper;

  public EarthquakesCache(BoxStore boxStore, EarthquakesMapper earthquakesMapper) {
    earthquakeBox = boxStore.boxFor(EarthquakeDb.class);
    this.earthquakesMapper = earthquakesMapper;
  }

  /**
   * Remove all {@link EarthquakeDb from cache}
   */
  void clearCache() {
    earthquakeBox.removeAll();
  }

  /**
   * @param earthquakeEntities to store
   */
  void putEarthquakes(List<Earthquake> earthquakeEntities) {
    earthquakeBox.put(earthquakesMapper.entitiesToDb(earthquakeEntities));
  }

  /**
   * @return all {@link EarthquakeDb} from the cache
   */
  Single<List<EarthquakeDb>> getEarthquakes() {
    Query<EarthquakeDb> query = earthquakeBox.query().build();
    return RxQuery.observable(query)
        .first(new ArrayList<>())
        .doOnSuccess(
            earthquakeEntities -> Timber.d("%d entities are in cache", earthquakeEntities.size()));
  }
}
