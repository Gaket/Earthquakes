package ru.inno.earthquakes;

import javax.inject.Singleton;

import dagger.Component;
import ru.inno.earthquakes.di.AppComponent;
import ru.inno.earthquakes.di.RetrofitModule;
import ru.inno.earthquakes.model.EarthquakesRepositoryImplTest;

/**
 * @author Artur Badretdinov (Gaket)
 *         22.07.17.
 */
@Singleton
@Component(modules = {RetrofitModule.class})
public interface DataTestComponent extends AppComponent{
    void inject(EarthquakesRepositoryImplTest test);
}
