package ru.inno.earthquakes.di.earthquakes;

import android.content.Context;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import dagger.Module;
import dagger.Provides;
import ru.inno.earthquakes.model.location.LocationInteractor;
import ru.inno.earthquakes.model.location.LocationRepository;
import ru.inno.earthquakes.model.permissions.PermissionsRepository;
import ru.inno.earthquakes.model.settings.SettingsRepository;
import ru.inno.earthquakes.presentation.common.SchedulersProvider;

/**
 * @author Artur Badretdinov (Gaket) 22.07.17.
 */
@Module
@EarthquakesScope
public class LocationModule {

  @Provides
  @EarthquakesScope
  LocationInteractor provideInteractor(LocationRepository repository,
      PermissionsRepository permissionsRepository, SettingsRepository settingsRepository,
      SchedulersProvider schedulersProvider) {
    return new LocationInteractor(repository, permissionsRepository, settingsRepository,
        schedulersProvider);
  }

  @Provides
  @EarthquakesScope
  LocationRepository provideRepository(FusedLocationProviderClient locationProviderClient,
      GoogleApiAvailability googleApiAvailability, Context context) {
    return new LocationRepository(locationProviderClient, googleApiAvailability, context);
  }

  @Provides
  @EarthquakesScope
  FusedLocationProviderClient provideLocationClient(Context context) {
    return LocationServices.getFusedLocationProviderClient(context);
  }
}
