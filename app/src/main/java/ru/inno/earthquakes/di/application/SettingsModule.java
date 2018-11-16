package ru.inno.earthquakes.di.application;

import android.content.SharedPreferences;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import ru.inno.earthquakes.business.settings.SettingsInteractor;
import ru.inno.earthquakes.repositories.settings.SettingsRepository;

/**
 * @author Artur Badretdinov (Gaket) 22.07.17.
 */
@Module
class SettingsModule {

  @Provides
  @Singleton
  SettingsInteractor provideInteractor(SettingsRepository repository) {
    return new SettingsInteractor(repository);
  }

  @Provides
  @Singleton
  SettingsRepository provideSettingsRepository(SharedPreferences sharedPreferences) {
    return new SettingsRepository(sharedPreferences);
  }
}
