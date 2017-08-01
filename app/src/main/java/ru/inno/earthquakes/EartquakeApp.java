package ru.inno.earthquakes;

import android.app.Application;

import ru.inno.earthquakes.di.application.AppComponent;
import ru.inno.earthquakes.di.application.AppModule;
import ru.inno.earthquakes.di.application.DaggerAppComponent;
import ru.inno.earthquakes.di.earthquakes.EarthquakesComponent;
import ru.inno.earthquakes.di.earthquakes.EarthquakesModule;

/**
 * @author Artur Badretdinov (Gaket)
 *         21.07.17.
 */
public class EartquakeApp extends Application {

    private static AppComponent appComponent;
    private static EarthquakesComponent earthquakesComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        initAppComponent();
    }

    private void initAppComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public static EarthquakesComponent getEarthquakesComponent() {
        EarthquakesComponent component = earthquakesComponent;
        if (component == null) {
            synchronized (EarthquakesComponent.class) {
                component = earthquakesComponent;
                if (component == null) {
                    earthquakesComponent = component = appComponent.plusEarthquakesComponent(new EarthquakesModule());
                }
            }
        }
        return component;
    }

    public static void clearEarthquakesComponent() {
        earthquakesComponent = null;
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }
}
