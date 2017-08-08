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

    public AppComponent getAppComponent() {
        AppComponent component = appComponent;
        if (component == null) {
            synchronized (AppComponent.class) {
                component = appComponent;
                if (component == null) {
                    appComponent = component = DaggerAppComponent.builder()
                            .appModule(new AppModule(context))
                            .build();
                }
            }
        }
        return component;
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

    public SettingsComponent getSettingsComponent() {
        SettingsComponent component = settingsComponent;
        if (component == null) {
            synchronized (SettingsComponent.class) {
                component = settingsComponent;
                if (component == null) {
                    settingsComponent = component = appComponent.plusSettingsComponent();
                }
            }
        }
        return component;
    }

    public void clearSettingsComponent() {
        settingsComponent = null;
    }
}