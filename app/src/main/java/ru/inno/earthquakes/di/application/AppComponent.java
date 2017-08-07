package ru.inno.earthquakes.di.application;

import javax.inject.Singleton;

import dagger.Component;
import ru.inno.earthquakes.di.earthquakes.EarthquakesComponent;
import ru.inno.earthquakes.di.earthquakes.EarthquakesModule;
import ru.inno.earthquakes.di.settings.SettingsComponent;
import ru.inno.earthquakes.di.settings.SettingsModule;

/**
 * @author Artur Badretdinov (Gaket)
 *         21.07.17.
 */
@Component(modules = {AppModule.class, RetrofitModule.class, SettingsModule.class})
@Singleton
public interface AppComponent {

    EarthquakesComponent plusEarthquakesComponent(EarthquakesModule module);

    SettingsComponent plusSettingsComponent();

}
