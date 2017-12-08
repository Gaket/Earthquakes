package ru.inno.earthquakes.data.network;

import io.reactivex.Single;
import retrofit2.http.GET;
import ru.inno.earthquakes.models.network.EarthquakesResponse;

/**
 * @author Artur Badretdinov (Gaket) 20.07.17.
 */
public interface EarthquakesApiService {

  /**
   * @return all earthquakes that happened in this day
   */
  @GET("earthquakes/feed/v1.0/summary/all_day.geojson")
  Single<EarthquakesResponse> getEarthquakes();
}
