package ru.inno.earthquakes;

import android.app.Application;

import ru.inno.earthquakes.di.ComponentsManager;
import timber.log.Timber;

/**
 * @author Artur Badretdinov (Gaket)
 *         21.07.17.
 */
public class EartquakeApp extends Application {

    private static ComponentsManager componentsManager;

    @Override
    public void onCreate() {
        super.onCreate();
        initComponentsTree();
        initAppComponent();
        initLogging();
    }

    private void initAppComponent() {
        componentsManager.getAppComponent();
    }

    private void initComponentsTree() {
        componentsManager = new ComponentsManager(this);
    }

    private void initLogging() {
        Timber.plant(new Timber.DebugTree());
    }

    public static ComponentsManager getComponentsManager() {
        return componentsManager;
    }
}
