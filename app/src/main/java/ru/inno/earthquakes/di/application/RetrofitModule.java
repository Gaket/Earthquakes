package ru.inno.earthquakes.di.application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.inno.earthquakes.model.earthquakes.EarthquakesApiService;
import timber.log.Timber;

/**
 * @author Artur Badretdinov (Gaket) 21.07.17.
 */
@Module
@Singleton
class RetrofitModule {

  private static final String BASE_URL = "https://earthquake.usgs.gov/";
  public static final int TIMEOUT = 20;

  @Provides
  @Singleton
  Retrofit provideRetrofit(OkHttpClient client, Gson gson) {
    return new Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .build();
  }

  @Provides
  @Singleton
  OkHttpClient provideOkHttpClient() {
    OkHttpClient.Builder builder = new OkHttpClient.Builder();
    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(
        message -> Timber.v(message));
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

    builder.addInterceptor(loggingInterceptor)
        .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT, TimeUnit.SECONDS)
        .connectTimeout(TIMEOUT, TimeUnit.SECONDS);
    return builder.build();
  }

  @Provides
  @Singleton
  Gson provideGson() {
    return new GsonBuilder()
        .registerTypeAdapter(Date.class,
            (JsonDeserializer<Date>) (json, typeOfT, context)
                -> new Date(json.getAsJsonPrimitive().getAsLong()))
        .create();
  }

  @Provides
  @Singleton
  EarthquakesApiService provideApiService(Retrofit retrofit) {
    return retrofit.create(EarthquakesApiService.class);
  }
}
