package ru.inno.earthquakes.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Artur Badretdinov (Gaket)
 *         21.07.17.
 */
@Module
@Singleton
public class AppModule {

    private Context mContext;

    public AppModule(Context aContext) {
        mContext = aContext.getApplicationContext();
    }

    @Provides
    @Singleton
    Context provideAppContext() {
        return mContext;
    }
}
