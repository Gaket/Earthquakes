package ru.inno.earthquakes.di.application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import java.util.Date;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.inno.earthquakes.model.earthquakes.EarthquakesApiService;
import timber.log.Timber;

/**
 * @author Artur Badretdinov (Gaket)
 *         21.07.17.
 */
@Module
@Singleton
public class RetrofitModule {

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient client, Gson gson) {
        return new Retrofit.Builder()
                .baseUrl("https://earthquake.usgs.gov/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> Timber.v(message));
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        builder.addInterceptor(loggingInterceptor);
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
