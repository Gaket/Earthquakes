package ru.inno.earthquakes;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import ru.inno.earthquakes.di.ComponentsManager;
import timber.log.Timber;

/**
 * @author Artur Badretdinov (Gaket)
 *         21.07.17.
 */
public class EartquakeApp extends Application {

    private static ComponentsManager componentsManager;
    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        initComponentsTree();
        initAppComponent();
        initLogging();
        initLeakCanary();
    }

    public static ComponentsManager getComponentsManager() {
        return componentsManager;
    }

    public static RefWatcher getRefWatcher(Context context) {
        EartquakeApp application = (EartquakeApp) context.getApplicationContext();
        return application.refWatcher;
    }

    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        refWatcher = LeakCanary.install(this);
    }

    private void initAppComponent() {
        componentsManager.getAppComponent();
    }

    private void initComponentsTree() {
        componentsManager = new ComponentsManager(this);
    }

    private void initLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

}
