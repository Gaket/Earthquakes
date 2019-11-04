package ru.inno.earthquakes.di.news;

import android.content.Context;
import dagger.Module;
import dagger.Provides;
import io.objectbox.BoxStore;
import org.mapstruct.factory.Mappers;
import ru.inno.earthquakes.business.earthquakes.EarthquakesInteractor;
import ru.inno.earthquakes.data.network.EarthquakesApiService;
import ru.inno.earthquakes.models.db.MyObjectBox;
import ru.inno.earthquakes.models.mappers.EarthquakesMapper;
import ru.inno.earthquakes.presentation.common.SchedulersProvider;
import ru.inno.earthquakes.repositories.earthquakes.EarthquakesCache;
import ru.inno.earthquakes.repositories.earthquakes.EarthquakesRepository;
import ru.inno.earthquakes.repositories.settings.SettingsRepository;

/**
 * @author Artur Badretdinov (Gaket) 22.07.17.
 */
@Module
public class NewsModule {

  @Provides
  @NewsScope
  BoxStore provideBoxStore(Context context) {
    return MyObjectBox.builder()
        .androidContext(context)
        .buildDefault();
  }
}
