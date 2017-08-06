package ru.inno.earthquakes.di.earthquakes;

import android.content.Context;

import org.mapstruct.factory.Mappers;

import dagger.Module;
import dagger.Provides;
import io.objectbox.BoxStore;
import ru.inno.earthquakes.model.models.db.MyObjectBox;
import ru.inno.earthquakes.model.earthquakes.EarthquakesApiService;
import ru.inno.earthquakes.model.earthquakes.EarthquakesCacheBox;
import ru.inno.earthquakes.model.earthquakes.EarthquakesInteractor;
import ru.inno.earthquakes.model.earthquakes.EarthquakesRepository;
import ru.inno.earthquakes.model.earthquakes.EarthquakesRepositoryImpl;
import ru.inno.earthquakes.model.mappers.EarthquakesMapper;
import ru.inno.earthquakes.model.settings.SettingsRepository;

/**
 * @author Artur Badretdinov (Gaket)
 *         22.07.17.
 */
@Module
@EarthquakesScope
public class EarthquakesModule {

    @Provides
    @EarthquakesScope
    EarthquakesInteractor provideInteractor(EarthquakesRepository repository, SettingsRepository settingsRepository){
        return new EarthquakesInteractor(repository, settingsRepository);
    }

    @Provides
    @EarthquakesScope
    EarthquakesRepository provideRepository(EarthquakesApiService apiService, EarthquakesMapper mapper, EarthquakesCacheBox earthquakesCache) {
        return new EarthquakesRepositoryImpl(apiService, mapper, earthquakesCache);
    }

    @Provides
    @EarthquakesScope
    EarthquakesCacheBox provideCache(BoxStore boxStore, EarthquakesMapper mapper) {
        return new EarthquakesCacheBox(boxStore, mapper);
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
