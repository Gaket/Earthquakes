package ru.inno.earthquakes.di.application;

import javax.inject.Singleton;

import dagger.Component;
import ru.inno.earthquakes.di.earthquakes.EarthquakesComponent;
import ru.inno.earthquakes.di.earthquakes.EarthquakesModule;
import ru.inno.earthquakes.presentation.settings.SettingsActivity;

/**
 * @author Artur Badretdinov (Gaket)
 *         21.07.17.
 */
@Component(modules = {AppModule.class, RetrofitModule.class, SettingsModule.class})
@Singleton
public interface AppComponent {

    EarthquakesComponent plusEarthquakesComponent(EarthquakesModule module);

    void inject(SettingsActivity settingsActivity);
}
