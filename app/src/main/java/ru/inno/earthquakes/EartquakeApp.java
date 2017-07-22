package ru.inno.earthquakes;

import android.app.Application;

import ru.inno.earthquakes.di.AppComponent;
import ru.inno.earthquakes.di.AppModule;
import ru.inno.earthquakes.di.DaggerAppComponent;

/**
 * @author Artur Badretdinov (Gaket)
 *         21.07.17.
 */
public class EartquakeApp extends Application {

    private static AppComponent appComponent;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    private void initAppComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }
}
