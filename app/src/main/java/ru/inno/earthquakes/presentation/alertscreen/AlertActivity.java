package ru.inno.earthquakes.presentation.alertscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.google.android.gms.common.GoogleApiAvailability;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import java.util.Locale;
import javax.inject.Inject;
import ru.inno.earthquakes.EartquakeApp;
import ru.inno.earthquakes.R;
import ru.inno.earthquakes.models.EntitiesWrapper;
import ru.inno.earthquakes.models.entities.EarthquakeWithDist;
import ru.inno.earthquakes.business.earthquakes.EarthquakesInteractor;
import ru.inno.earthquakes.business.location.LocationInteractor;
import ru.inno.earthquakes.business.settings.SettingsInteractor;
import ru.inno.earthquakes.presentation.common.SchedulersProvider;
import ru.inno.earthquakes.presentation.common.Utils;
import ru.inno.earthquakes.presentation.earthquakeslist.EarthquakesListActivity;
import ru.inno.earthquakes.presentation.settings.SettingsActivity;
import timber.log.Timber;

/**
 * @author Artur Badretdinov (Gaket) 22.07.17
 */
public class AlertActivity extends MvpAppCompatActivity {

  // Here, and in other controllers, controller works as a root for some model components
  // dependency tree. As a result, we inject them here and deeper all injections are made
  // through the constructors.
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

  private CompositeDisposable compositeDisposable;

  private SwipeRefreshLayout swipeRefreshLayout;
  private TextView messageView;
  private TextView detailsView;
  private TextView magnitudeView;
  private TextView distanceView;
  private ImageView alertImageView;
  private Snackbar snackbar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    EartquakeApp.getComponentsManager().getEarthquakesComponent().inject(this);
    super.onCreate(savedInstanceState);
    compositeDisposable = new CompositeDisposable();

    setContentView(R.layout.activity_main);
    messageView = (TextView) findViewById(R.id.alert_message);
    detailsView = (TextView) findViewById(R.id.alert_details);
    magnitudeView = (TextView) findViewById(R.id.alert_magnitude);
    distanceView = (TextView) findViewById(R.id.alert_distance);
    alertImageView = (ImageView) findViewById(R.id.alert_status);
    swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.alert_swipe_refresh);
    swipeRefreshLayout.setOnRefreshListener(() -> onRefreshAction());
    findViewById(R.id.alert_show_all).setOnClickListener(v -> onShowAll());

    onFirstViewAttach();
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
        onOpenSettings();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  public void showThereAreNoAlerts() {
    messageView.setText(R.string.alert_msg_everything_is_ok);
    alertImageView.setImageResource(R.drawable.earth_normal);
    distanceView.setVisibility(View.GONE);
    magnitudeView.setVisibility(View.GONE);
    detailsView.setText(R.string.alert_details_no_earthquakes);
  }

  public void showEarthquakeAlert(EarthquakeWithDist earthquake) {
    alertImageView.setImageResource(R.drawable.earth_alarm);
    messageView.setText(R.string.alert_msg_earthquake_nearby);
    detailsView.setText(earthquake.getEarthquake().getTitle());
    distanceView.setText(getResources().getString(R.string.alert_distance_from_place,
        Utils.formatDistanceString(earthquake.getDistance())));
    String magnitude = String
        .format(Locale.getDefault(), "%.2f", earthquake.getEarthquake().getMagnitude());
    magnitudeView.setText(magnitude);
    distanceView.setVisibility(View.VISIBLE);
    magnitudeView.setVisibility(View.VISIBLE);
  }

  public void showNetworkError(boolean show) {
    if (show) {
      snackbar = Snackbar.make(swipeRefreshLayout, R.string.error_connection,
          BaseTransientBottomBar.LENGTH_INDEFINITE)
          .setAction(R.string.action_ok, (d) -> snackbar.dismiss());
      snackbar.show();
    } else if (snackbar != null) {
      snackbar.dismiss();
    }
  }

  public void showLoading(boolean show) {
    swipeRefreshLayout.setRefreshing(show);
  }

  public void navigateToEarthquakesList() {
    Intent intent = EarthquakesListActivity.getStartIntent(this);
    startActivity(intent);
  }

  public void navigateToSettings() {
    Intent intent = SettingsActivity.getStartIntent(this);
    startActivity(intent);
  }

  public void showPermissionDeniedAlert() {
    Toast.makeText(this, R.string.error_prohibited_location_access, Toast.LENGTH_LONG).show();
  }

  public void showNoDataAlert() {
    Toast.makeText(this, R.string.error_no_data, Toast.LENGTH_LONG).show();
  }

  public void showGoogleApiMessage(int status) {
    if (googleApiAvailability.isUserResolvableError(status)) {
      googleApiAvailability.getErrorDialog(this, status, 1).show();
    } else {
      Snackbar.make(swipeRefreshLayout, R.string.error_google_api_unavailable,
          Snackbar.LENGTH_INDEFINITE).show();
    }
  }

  protected void onFirstViewAttach() {
    updateCurrentState();

    // Subscribe to settings updates
    Disposable disposable = settinsInteractor.getSettingsChangeObservable()
        .subscribe(updated -> onRefreshAction(), Timber::e);
    unsubscribeOnDestroy(disposable);

    // Show a message for users if they don't have Google Api Services needed for program
    Disposable googleDisposable = locationInteractor.checkLocationServicesAvailability()
        .filter(available -> !available)
        .flatMap(available -> locationInteractor.getLocationServicesStatus().toMaybe())
        .observeOn(schedulersProvider.ui())
        .subscribe(status -> showGoogleApiMessage(status), Timber::e);
    unsubscribeOnDestroy(googleDisposable);
  }

  void onRefreshAction() {
    updateCurrentState();
  }

  void onShowAll() {
    navigateToEarthquakesList();
  }

  void onOpenSettings() {
    navigateToSettings();
  }

  /**
   * Call to update current data
   */
  private void updateCurrentState() {
    Disposable disposable = getEarthquakeAlert()
        .observeOn(schedulersProvider.ui())
        .doOnSubscribe(disp -> showLoading(true))
        .doAfterTerminate(() -> showLoading(false))
        .subscribe(this::handleEartquakesAnswer, Timber::e);
    unsubscribeOnDestroy(disposable);
  }

  private void handleEartquakesAnswer(EntitiesWrapper<EarthquakeWithDist> earthquakeWithDists) {
    handleNetworkStateMessage(earthquakeWithDists);
    handleEarthquakeData(earthquakeWithDists);
  }

  private void handleEarthquakeData(EntitiesWrapper<EarthquakeWithDist> earthquakeWithDists) {
    if (earthquakeWithDists.getState() == EntitiesWrapper.State.EMPTY) {
      showThereAreNoAlerts();
    } else if (earthquakeWithDists.getState() == EntitiesWrapper.State.SUCCESS) {
      showEarthquakeAlert(earthquakeWithDists.getData());
    }
  }

  private void handleNetworkStateMessage(EntitiesWrapper<EarthquakeWithDist> earthquakeWithDists) {
    if (earthquakeWithDists.getState() == EntitiesWrapper.State.ERROR_NETWORK) {
      showNetworkError(true);
      showThereAreNoAlerts();
    } else {
      showNetworkError(false);
    }
  }

  /**
   * Get earthquake alert and show user if there are any problems
   */
  private Single<EntitiesWrapper<EarthquakeWithDist>> getEarthquakeAlert() {
    return locationInteractor.getCurrentCoordinates()
        .doOnSuccess(locationAnswer -> {
          switch (locationAnswer.getState()) {
            case SUCCESS:
              // do nothing
              break;
            case PERMISSION_DENIED:
              showPermissionDeniedAlert();
              break;
            case NO_DATA:
              showNoDataAlert();
              break;
          }
        })
        .flatMap(locationAnswer -> earthquakesInteractor
            .getEarthquakeAlert(locationAnswer.getCoordinates()));
  }

  protected void unsubscribeOnDestroy(Disposable disposable) {
    compositeDisposable.add(disposable);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    compositeDisposable.clear();
  }
}