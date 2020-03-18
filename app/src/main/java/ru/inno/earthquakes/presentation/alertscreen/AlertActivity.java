package ru.inno.earthquakes.presentation.alertscreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

import javax.inject.Inject;

import ru.inno.earthquakes.EarthquakesApp;
import ru.inno.earthquakes.R;
import ru.inno.earthquakes.business.earthquakes.EarthquakesInteractor;
import ru.inno.earthquakes.business.location.LocationInteractor;
import ru.inno.earthquakes.business.settings.SettingsInteractor;
import ru.inno.earthquakes.models.entities.EarthquakeWithDist;
import ru.inno.earthquakes.presentation.common.SchedulersProvider;
import ru.inno.earthquakes.presentation.common.Utils;
import ru.inno.earthquakes.presentation.earthquakeslist.EarthquakesListActivity;
import ru.inno.earthquakes.presentation.settings.SettingsActivity;

/**
 * @author Artur Badretdinov (Gaket) 22.07.17
 */
public class AlertActivity extends MvpAppCompatActivity implements AlertView {

  // Here, and in other controllers, controller works as a root for some model components
  // dependency tree. As a result, we inject them here and deeper all injections are made
  // through the constructors.
  @InjectPresenter
  AlertPresenter presenter;
  @Inject
  EarthquakesInteractor earthquakesInteractor;
  @Inject
  LocationInteractor locationInteractor;
  @Inject
  SettingsInteractor settinsInteractor;
  @Inject
  SchedulersProvider schedulersProvider;
  @Inject
  GoogleApiAvailability googleApiAvailability;

  @ProvidePresenter
  AlertPresenter providePresenter() {
    return new AlertPresenter(earthquakesInteractor, locationInteractor, settinsInteractor,
        schedulersProvider);
  }

  private SwipeRefreshLayout swipeRefreshLayout;
  private TextView messageView;
  private TextView detailsView;
  private TextView magnitudeView;
  private TextView distanceView;
  private ImageView alertImageView;
  private Snackbar snackbar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    EarthquakesApp.getComponentsManager().getEarthquakesComponent().inject(this);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    messageView = findViewById(R.id.alert_message);
    detailsView = findViewById(R.id.alert_details);
    magnitudeView = findViewById(R.id.alert_magnitude);
    distanceView = findViewById(R.id.alert_distance);
    alertImageView = findViewById(R.id.alert_status);
    swipeRefreshLayout = findViewById(R.id.alert_swipe_refresh);
    swipeRefreshLayout.setOnRefreshListener(() -> presenter.onRefreshAction());
    findViewById(R.id.alert_show_all).setOnClickListener(v -> presenter.onShowAll());
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_alert, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_settings:
        presenter.onOpenSettings();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  public void showThereAreNoAlerts() {
    messageView.setText(R.string.alert_msg_everything_is_ok);
    alertImageView.setImageResource(R.drawable.earth_normal);
    distanceView.setVisibility(View.GONE);
    magnitudeView.setVisibility(View.GONE);
    detailsView.setText(R.string.alert_details_no_earthquakes);
  }

  @Override
  public void showEarthquakeAlert(EarthquakeWithDist earthquake) {
    alertImageView.setImageResource(R.drawable.earth_alarm);
    messageView.setText(R.string.alert_msg_earthquake_nearby);
    detailsView.setText(earthquake.getEarthquake().getTitle());
    distanceView.setText(getResources().getString(R.string.alert_distance_from_place,
        Utils.formatDistanceString(earthquake.getDistance())));
    String magnitude = String.format(Locale.getDefault(), "%.2f", earthquake.getEarthquake().getMagnitude());
    magnitudeView.setText(magnitude);
    distanceView.setVisibility(View.VISIBLE);
    magnitudeView.setVisibility(View.VISIBLE);
  }

  @Override
  public void showNetworkError(boolean show) {
    if (show) {
      snackbar = Snackbar.make(swipeRefreshLayout, R.string.error_connection, Snackbar.LENGTH_INDEFINITE)
          .setAction(R.string.action_ok, d -> snackbar.dismiss());
      snackbar.show();
    } else if (snackbar != null) {
      snackbar.dismiss();
    }
  }

  @Override
  public void showLoading(boolean show) {
    swipeRefreshLayout.setRefreshing(show);
  }

  @Override
  public void navigateToEarthquakesList() {
    Intent intent = EarthquakesListActivity.getStartIntent(this);
    startActivity(intent);
  }

  @Override
  public void navigateToSettings() {
    Intent intent = SettingsActivity.getStartIntent(this);
    startActivity(intent);
  }

  @Override
  public void showPermissionDeniedAlert() {
    Toast.makeText(this, R.string.error_prohibited_location_access, Toast.LENGTH_LONG).show();
  }

  @Override
  public void showNoDataAlert() {
    Toast.makeText(this, R.string.error_no_data, Toast.LENGTH_LONG).show();
  }

  @Override
  public void showGoogleApiMessage(int status) {
    if (googleApiAvailability.isUserResolvableError(status)) {
      googleApiAvailability.getErrorDialog(this, status, 1).show();
    } else {
      Snackbar.make(swipeRefreshLayout, R.string.error_google_api_unavailable, Snackbar.LENGTH_INDEFINITE)
          .show();
    }
  }
}