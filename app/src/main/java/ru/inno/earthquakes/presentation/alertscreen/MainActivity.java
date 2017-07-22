package ru.inno.earthquakes.presentation.alertscreen;

import android.os.Bundle;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import ru.inno.earthquakes.EartquakeApp;
import ru.inno.earthquakes.R;
import ru.inno.earthquakes.entity.EarthquakeWithDist;

public class MainActivity extends MvpAppCompatActivity
        implements AlertView {


    @InjectPresenter
    AlertPresenter presenter;

    @ProvidePresenter
    AlertPresenter providePresenter() {
        return new AlertPresenter(EartquakeApp.getEarthquakesComponent());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void showThereAreNoAlerts() {
        Toast.makeText(this, "no alerts", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEartqhakeAlert(EarthquakeWithDist earthquake) {
        Toast.makeText(this, earthquake.toString(), Toast.LENGTH_SHORT).show();
    }
}
