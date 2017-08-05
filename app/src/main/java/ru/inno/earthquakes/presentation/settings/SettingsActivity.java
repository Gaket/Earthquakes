package ru.inno.earthquakes.presentation.settings;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import ru.inno.earthquakes.EartquakeApp;
import ru.inno.earthquakes.R;
import ru.inno.earthquakes.presentation.earthquakeslist.EarthquakesListPresenter;

public class SettingsActivity extends MvpAppCompatActivity {

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
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.settings_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
