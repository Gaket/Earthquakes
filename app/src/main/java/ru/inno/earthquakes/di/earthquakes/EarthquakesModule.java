package ru.inno.earthquakes.di.earthquakes;

import android.content.Context;
import dagger.Module;
import dagger.Provides;
import io.objectbox.BoxStore;
import org.mapstruct.factory.Mappers;
import ru.inno.earthquakes.data.network.EarthquakesApiService;
import ru.inno.earthquakes.repositories.earthquakes.EarthquakesCache;
import ru.inno.earthquakes.business.earthquakes.EarthquakesInteractor;
import ru.inno.earthquakes.models.mappers.EarthquakesMapper;
import ru.inno.earthquakes.models.db.MyObjectBox;
import ru.inno.earthquakes.presentation.common.SchedulersProvider;
import ru.inno.earthquakes.repositories.earthquakes.EarthquakesRepository;
import ru.inno.earthquakes.repositories.settings.SettingsRepository;

/**
 * @author Artur Badretdinov (Gaket) 22.07.17.
 */
@Module
public class EarthquakesModule {

  @Provides
  @EarthquakesScope
  EarthquakesInteractor provideInteractor(EarthquakesRepository repository,
      SettingsRepository settingsRepository, SchedulersProvider schedulersProvider) {
    return new EarthquakesInteractor(repository, settingsRepository, schedulersProvider);
  }

  @Provides
  @EarthquakesScope
  EarthquakesRepository provideRepository(EarthquakesApiService apiService,
      EarthquakesMapper mapper, EarthquakesCache earthquakesCache) {
    return new EarthquakesRepository(apiService, mapper, earthquakesCache);
  }

  @Provides
  @EarthquakesScope
  EarthquakesCache provideCache(BoxStore boxStore, EarthquakesMapper mapper) {
    return new EarthquakesCache(boxStore, mapper);
  }

  @Provides
  @EarthquakesScope
  BoxStore provideBoxStore(Context context) {
    return MyObjectBox.builder()
        .androidContext(context)
        .buildDefault();
  }

  @Provides
  @EarthquakesScope
  EarthquakesMapper provideMapper() {
    return Mappers.getMapper(EarthquakesMapper.class);
  }
}
