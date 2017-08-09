package ru.inno.earthquakes.presentation.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.util.Locale;

import javax.inject.Inject;

import ru.inno.earthquakes.EartquakeApp;
import ru.inno.earthquakes.R;
import ru.inno.earthquakes.model.settings.SettingsInteractor;
import ru.inno.earthquakes.presentation.info.InfoActivity;

import static android.view.inputmethod.EditorInfo.IME_ACTION_DONE;

public class SettingsActivity extends MvpAppCompatActivity implements SettingsView {

    @InjectPresenter
    SettingsPresenter presenter;
    @Inject
    SettingsInteractor settingsInteractor;

    private EditText distanceView;
    private SeekBar magnitudeView;
    private TextView magnitudeValueView;

    @ProvidePresenter
    SettingsPresenter providePresenter() {
        EartquakeApp.getComponentsManager().getAppComponent().inject(this);
        return new SettingsPresenter(settingsInteractor);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // According to YAGNI, we have only some hardcoded views here.
        // If application becomes more complicated, here should be a RecyclerView with options.
        distanceView = (EditText) findViewById(R.id.settings_distance);
        distanceView.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == IME_ACTION_DONE) {
                saveSettings();
                return true;
            } else {
                return false;
            }
        });

        magnitudeValueView = (TextView) findViewById(R.id.settings_magnitude_value);
        magnitudeView = (SeekBar) findViewById(R.id.settings_magnitude);
        magnitudeView.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                magnitudeValueView.setText(String.format(Locale.getDefault(), "%.1f", progress / 10.0));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // do nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // do nothing
            }
        });
        findViewById(R.id.settings_bt_save).setOnClickListener(v -> {
            saveSettings();
        });
    }

    private void saveSettings() {
        double magnitude = magnitudeView.getProgress() / 10.0;
        presenter.onSave(distanceView.getText().toString(), magnitude);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_info:
                presenter.onInfoAction();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setMaxDistance(Double dist) {
        distanceView.setText(String.format(Locale.getDefault(), "%.0f", dist));
    }

    @Override
    public void setMinMagnitude(Double mag) {
        magnitudeView.setProgress((int) (mag * 10));
    }

    @Override
    public void showDistanceFormatError() {
        distanceView.setError(getResources().getString(R.string.error_settings_distance));
    }

    @Override
    public void close() {
        onBackPressed();
    }

    @Override
    public void navigateToInfo() {
        Intent intent = InfoActivity.getStartInfo(this);
        startActivity(intent);
    }

    public static Intent getStartIntent(Context callingContext) {
        return new Intent(callingContext, SettingsActivity.class);
    }
}
