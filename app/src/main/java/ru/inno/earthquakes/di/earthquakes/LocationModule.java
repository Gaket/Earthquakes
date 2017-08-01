package ru.inno.earthquakes.di.earthquakes;

import dagger.Module;
import dagger.Provides;
import ru.inno.earthquakes.model.location.LocationRepository;
import ru.inno.earthquakes.model.location.LocationInteractor;
import ru.inno.earthquakes.model.location.LocationRepoStub;

/**
 * @author Artur Badretdinov (Gaket)
 *         22.07.17.
 */
@Module
@EarthquakesScope
public class LocationModule {

    @Provides
    @EarthquakesScope
    LocationInteractor provideInteractor(LocationRepository repository) {
        return new LocationInteractor(repository);
    }


    @Provides
    @EarthquakesScope
    LocationRepository provideRepository() {
        return new LocationRepoStub();
    }
}
