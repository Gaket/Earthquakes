package ru.inno.earthquakes.di.earthquakes;

import android.content.Context;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import dagger.Module;
import dagger.Provides;
import ru.inno.earthquakes.model.permissions.PermissionsRepository;
import ru.inno.earthquakes.model.location.LocationInteractor;
import ru.inno.earthquakes.model.location.LocationRepository;

/**
 * @author Artur Badretdinov (Gaket)
 *         22.07.17.
 */
@Module
@EarthquakesScope
public class LocationModule {

    @Provides
    @EarthquakesScope
    LocationInteractor provideInteractor(LocationRepository repository, PermissionsRepository permissionsRepository) {
        return new LocationInteractor(repository, permissionsRepository);
    }

    @Provides
    @EarthquakesScope
    LocationRepository provideRepository(FusedLocationProviderClient locationProviderClient) {
        return new LocationRepository(locationProviderClient);
    }

    @Provides
    @EarthquakesScope
    FusedLocationProviderClient provideLocationClient(Context context) {
        return LocationServices.getFusedLocationProviderClient(context);
    }
}
