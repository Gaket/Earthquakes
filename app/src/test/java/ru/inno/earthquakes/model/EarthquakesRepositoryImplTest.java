package ru.inno.earthquakes.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;
import java.util.List;

import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.inno.earthquakes.entity.Earthquake;
import ru.inno.earthquakes.model.earthquakes.data.EarthquakesApiService;
import ru.inno.earthquakes.model.earthquakes.data.EarthquakesMapper;
import ru.inno.earthquakes.model.earthquakes.data.EarthquakesRepositoryImpl;

/**
 * @author Artur Badretdinov (Gaket)
 *         20.07.17.
 */
@RunWith(MockitoJUnitRunner.class)
public class EarthquakesRepositoryImplTest {

    static EarthquakesRepositoryImpl repository;

    @BeforeClass
    public static void initRepo() {

        // TODO: Dagger does not create components in tests, check it later
        // DataTestComponent component = DaggerDataTestComponent.

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class,
                        (JsonDeserializer<Date>) (json, typeOfT, context)
                                -> new Date(json.getAsJsonPrimitive().getAsLong()))
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://earthquake.usgs.gov/") // implicit dependency
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();
        EarthquakesApiService service = retrofit.create(EarthquakesApiService.class);
        repository = new EarthquakesRepositoryImpl(service, new EarthquakesMapper());
    }

    @Test
    public void checkNoErrorsInDownloading() throws Exception {
        TestObserver<List<Earthquake>> observer = repository.getTodaysEarthquakes().test();
        observer.awaitTerminalEvent();
        observer.assertNoErrors();
        List<Object> events = observer.getEvents().get(0);
        Assert.assertEquals(1, events.size());
        List<Earthquake> answer = (List<Earthquake>) events.get(0);
        Assert.assertFalse(answer.isEmpty());

    }
}