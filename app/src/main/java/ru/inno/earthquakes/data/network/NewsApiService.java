package ru.inno.earthquakes.data.network;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.inno.earthquakes.models.network.EarthquakesResponse;
import ru.inno.earthquakes.models.network.NewsResponse;

/**
 * Created by Artur (gaket) on 2019-11-04.
 */
public interface NewsApiService {

  /**
   * @return all earthquakes that happened in this day
   */
  @GET("https://newsapi.org/v2/everything?q=earthquake")
  Single<NewsResponse> getEarthquakes(@Query("apiKey") String apiKey);


}
