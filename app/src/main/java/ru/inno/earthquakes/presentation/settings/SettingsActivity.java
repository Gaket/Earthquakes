package ru.inno.earthquakes.presentation.settings;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import ru.inno.earthquakes.EartquakeApp;
import ru.inno.earthquakes.R;

public class SettingsActivity extends MvpAppCompatActivity implements SettingsView {

    @InjectPresenter
    SettingsPresenter presenter;

    @ProvidePresenter
    SettingsPresenter providePresenter() {
        return new SettingsPresenter(EartquakeApp.getComponentsManager().getSettingsComponent());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initToolbar();

        // According to YAGNI, we have only some hardcoded views here.
        // If application becomes more complicated, here should be a RecyclerView with options.
        findViewById(R.id.settings_tv_distance).setOnClickListener((v) -> openDistanceDialog());
        findViewById(R.id.settings_tv_magnitude).setOnClickListener((v) -> presenter.onChangeMagnitude());
    }

    private void openDistanceDialog() {

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void setMaxDistance(Double dist) {

    }

    @Override
    public void setMinMagnitude(Double mag) {

    }
}
