package ru.inno.earthquakes.di;

import android.content.Context;

import ru.inno.earthquakes.di.application.AppComponent;
import ru.inno.earthquakes.di.application.AppModule;
import ru.inno.earthquakes.di.application.DaggerAppComponent;
import ru.inno.earthquakes.di.earthquakes.EarthquakesComponent;
import ru.inno.earthquakes.di.earthquakes.EarthquakesModule;
import ru.inno.earthquakes.di.settings.SettingsComponent;

public class ComponentsManager {

    private Context context;

    private AppComponent appComponent;
    private EarthquakesComponent earthquakesComponent;
    private SettingsComponent settingsComponent;

    public ComponentsManager(Context context) {
        this.context = context.getApplicationContext();
    }

    // We don't have to think about multithreading here, because we get our components
    // only from Activity/Component classes on main thread
    public AppComponent getAppComponent() {
        if (appComponent == null) {
            appComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(context))
                    .build();
        }
        return appComponent;
    }

    public EarthquakesComponent getEarthquakesComponent() {
        if (earthquakesComponent == null) {
            earthquakesComponent = appComponent.plusEarthquakesComponent(new EarthquakesModule());
        }
        return earthquakesComponent;
    }

    public SettingsComponent getSettingsComponent() {
        if (settingsComponent == null) {
            settingsComponent = appComponent.plusSettingsComponent();
        }
        return settingsComponent;
    }

    public void clearEarthquakesComponent() {
        earthquakesComponent = null;
    }

    public void clearSettingsComponent() {
        settingsComponent = null;
    }
}