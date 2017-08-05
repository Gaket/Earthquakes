package ru.inno.earthquakes;

import android.content.Context;

import ru.inno.earthquakes.di.application.AppComponent;
import ru.inno.earthquakes.di.application.AppModule;
import ru.inno.earthquakes.di.application.DaggerAppComponent;
import ru.inno.earthquakes.di.earthquakes.EarthquakesComponent;
import ru.inno.earthquakes.di.earthquakes.EarthquakesModule;

public class ComponentsManager {

    public static AppComponent appComponent;
    public static EarthquakesComponent earthquakesComponent;

    public void initAppComponent(Context context) {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(context))
                .build();
    }

    public EarthquakesComponent getEarthquakesComponent() {
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

    public void clearEarthquakesComponent() {
        earthquakesComponent = null;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public AppComponent getSettingsComponent() {
        return null;
    }
}