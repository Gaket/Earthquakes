package ru.inno.earthquakes.di.application;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.common.GoogleApiAvailability;
import com.tbruyelle.rxpermissions2.RxPermissions;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.inno.earthquakes.model.permissions.PermissionsRepository;
import ru.inno.earthquakes.presentation.common.SchedulersProvider;

/**
 * @author Artur Badretdinov (Gaket)
 *         21.07.17.
 */
@Module
@Singleton
public class AppModule {

    private static final String APP_PREFS = "AppPrefs";
    private Context context;

    public AppModule(Context aContext) {
        context = aContext.getApplicationContext();
    }

    @Provides
    @Singleton
    Context provideAppContext() {
        return context;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences() {
        return context.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    PermissionsRepository providePermissionsManager(RxPermissions rxPermissions) {
        return new PermissionsRepository(rxPermissions);
    }

    @Provides
    @Singleton
    RxPermissions provideRxPermissions(Context context) {
        return RxPermissions.getInstance(context);
    }

    @Provides
    @Singleton
    SchedulersProvider provideSchedulersProvider() {
        return new SchedulersProvider();
    }

    @Provides
    @Singleton
    GoogleApiAvailability provideGoogleApiAvailability() {
        return GoogleApiAvailability.getInstance();
    }
}
