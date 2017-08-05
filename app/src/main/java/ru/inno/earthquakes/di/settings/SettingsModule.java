package ru.inno.earthquakes.di.settings;

import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;
import ru.inno.earthquakes.model.settings.SettingsInteractor;
import ru.inno.earthquakes.model.settings.SettingsRepository;

/**
 * @author Artur Badretdinov (Gaket)
 *         22.07.17.
 */
@Module
@SettingsScope
public class SettingsModule {

    @Provides
    @SettingsScope
    SettingsInteractor provideInteractor(SettingsRepository repository) {
        return new SettingsInteractor(repository);
    }

    @Provides
    @SettingsScope
    SettingsRepository provideRepository(SharedPreferences sharedPreferences) {
        return new SettingsRepository(sharedPreferences);
    }
}
