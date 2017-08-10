package ru.inno.earthquakes.presentation;

import android.os.Bundle;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;

import ru.inno.earthquakes.R;
import ru.inno.earthquakes.presentation.alertscreen.AlertController;
import ru.inno.earthquakes.presentation.common.ActionBarProvider;

public class MainActivity extends MvpAppCompatActivity implements ActionBarProvider {

    private Router router;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        ViewGroup container = (ViewGroup)findViewById(R.id.controller_container);

        router = Conductor.attachRouter(this, container, savedInstanceState);
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(new AlertController()));
        }
    }

    @Override
    public void onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed();
        }
    }
}
