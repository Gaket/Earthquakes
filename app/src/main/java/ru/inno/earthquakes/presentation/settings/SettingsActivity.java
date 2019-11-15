package ru.inno.earthquakes.presentation.settings;

import static android.view.inputmethod.EditorInfo.IME_ACTION_DONE;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import java.util.Locale;
import javax.inject.Inject;
import ru.inno.earthquakes.EartquakeApp;
import ru.inno.earthquakes.R;
import ru.inno.earthquakes.business.settings.SettingsInteractor;
import ru.inno.earthquakes.presentation.info.InfoActivity;

public class SettingsActivity extends MvpAppCompatActivity implements SettingsView, ResourceFactory {

  @InjectPresenter
  SettingsPresenter presenter;
  @Inject
  SettingsInteractor settingsInteractor;

  private EditText distanceView;
  private SeekBar magnitudeView;
  private TextView magnitudeValueView;
  private TextView defaultCityView;

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
    defaultCityView = (TextView) findViewById(R.id.settings_default_city);
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
    String dist = distanceView.getText().toString();
    double magnitude = magnitudeView.getProgress() / 10.0;
    presenter.onSave(dist, magnitude);
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
  public void showError(String errorMsg) {
    Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
  }

  @Override
  public void showError(SettingsMessage distanceError) {
    Toast.makeText(this, distanceError.getResId(), Toast.LENGTH_LONG).show();
  }

  @Override
  public void showError(int resId) {
    Toast.makeText(this, resId, Toast.LENGTH_LONG).show();
  }

  @Override
  public void setDefaultCity(String name) {
    defaultCityView.setText(name);
  }

  @Override
  public void showNotImplementedError() {
    Toast.makeText(this, R.string.error_not_implemented, Toast.LENGTH_SHORT).show();
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
