package ru.inno.earthquakes.presentation.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;

import ru.inno.earthquakes.EartquakeApp;
import ru.inno.earthquakes.R;
import timber.log.Timber;

public class SettingsActivity extends MvpAppCompatActivity {

    private Router router;

    public static Intent getStartIntent(Context callingContext) {
        return new Intent(callingContext, SettingsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        setActionBar();
        ViewGroup container = (ViewGroup)findViewById(R.id.controller_container);

        router = Conductor.attachRouter(this, container, savedInstanceState);
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(new SettingsController()));
        }
    }


    @Override
    protected void onDestroy() {
        EartquakeApp.getComponentsManager().clearSettingsComponent();
        super.onDestroy();
    }

    private void setActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            Timber.e("Action bar is null in Settings Activity");
        }
    }
}
