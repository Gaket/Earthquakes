package ru.inno.earthquakes.di;

import dagger.Component;

/**
 * @author Artur Badretdinov (Gaket)
 *         21.07.17.
 */
@Component(modules = {AppModule.class, RetrofitModule.class})
public interface AppComponent {
}
