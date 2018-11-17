package ru.inno.earthquakes.presentation.alertscreen;

import android.Manifest.permission;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.tbruyelle.rxpermissions2.RxPermissions;
import dagger.Provides;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.Query;
import io.objectbox.rx.RxQuery;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.mapstruct.factory.Mappers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.inno.earthquakes.EartquakeApp;
import ru.inno.earthquakes.R;
import ru.inno.earthquakes.business.location.LocationInteractor.LocationAnswer;
import ru.inno.earthquakes.data.network.EarthquakesApiService;
import ru.inno.earthquakes.models.EntitiesWrapper;
import ru.inno.earthquakes.models.EntitiesWrapper.State;
import ru.inno.earthquakes.models.db.EarthquakeDb;
import ru.inno.earthquakes.models.db.MyObjectBox;
import ru.inno.earthquakes.models.entities.Earthquake;
import ru.inno.earthquakes.models.entities.EarthquakeWithDist;
import ru.inno.earthquakes.business.location.LocationInteractor;
import ru.inno.earthquakes.business.settings.SettingsInteractor;
import ru.inno.earthquakes.models.entities.Location;
import ru.inno.earthquakes.models.entities.Location.Coordinates;
import ru.inno.earthquakes.models.mappers.EarthquakesMapper;
import ru.inno.earthquakes.models.network.EarthquakesResponse;
import ru.inno.earthquakes.presentation.common.SchedulersProvider;
import ru.inno.earthquakes.presentation.common.Utils;
import ru.inno.earthquakes.presentation.earthquakeslist.EarthquakesListActivity;
import ru.inno.earthquakes.presentation.settings.SettingsActivity;
import ru.inno.earthquakes.repositories.earthquakes.EarthquakesCache;
import ru.inno.earthquakes.repositories.location.UnknownLocationException;
import timber.log.Timber;

/**
 * @author Artur Badretdinov (Gaket) 22.07.17
 */
public class AlertActivity extends MvpAppCompatActivity {

  public static final int TIMEOUT = 20;
  private static final String KEY_MAX_DIST = "ru.inno.earthquakes.business.settings.max_dist";
  private static final String KEY_MIN_MAG = "ru.inno.earthquakes.business.settings.min_mag";
  private static final String APP_PREFS = "AppPrefs";
  private static final String BASE_URL = "https://earthquake.usgs.gov/";
  // Return Moscow coordinates by default. Later we can create a feature where user enters it
  private final String DEFAULT_CITY = "Moscow";
  private final Location.Coordinates DEFAULT_COORDINATES = new Location.Coordinates(55.755826,
      37.6173);

  @Inject
  SettingsInteractor settinsInteractor;
  @Inject
  BoxStore boxStore;
  SchedulersProvider schedulersProvider;
  GoogleApiAvailability googleApiAvailability;
  EarthquakesApiService apiService;
  EarthquakesMapper earthquakesMapper;
  Comparator<EarthquakeWithDist> distanceComparator = (a, b) -> Double.compare(a.getDistance(), b.getDistance());
  private CompositeDisposable compositeDisposable;
  private SwipeRefreshLayout swipeRefreshLayout;
  private TextView messageView;
  private TextView detailsView;
  private TextView magnitudeView;
  private TextView distanceView;
  private ImageView alertImageView;
  private Snackbar snackbar;
  private RxPermissions rxPermissions;
  private SharedPreferences sharedPreferences;
  private FusedLocationProviderClient fusedLocationClient;
  private boolean isGoogleApiAvailable = true;
  private Box<EarthquakeDb> earthquakeBox;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    EartquakeApp.getComponentsManager().getEarthquakesComponent().inject(this);
    super.onCreate(savedInstanceState);
    compositeDisposable = new CompositeDisposable();

    setContentView(R.layout.activity_main);
    messageView = findViewById(R.id.alert_message);
    detailsView = findViewById(R.id.alert_details);
    magnitudeView = findViewById(R.id.alert_magnitude);
    distanceView = findViewById(R.id.alert_distance);
    alertImageView = findViewById(R.id.alert_status);
    swipeRefreshLayout = findViewById(R.id.alert_swipe_refresh);
    swipeRefreshLayout.setOnRefreshListener(() -> onRefreshAction());
    findViewById(R.id.alert_show_all).setOnClickListener(v -> onShowAll());

    sharedPreferences = getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    rxPermissions = RxPermissions.getInstance(this);
    earthquakeBox = boxStore.boxFor(EarthquakeDb.class);
    earthquakesMapper = Mappers.getMapper(EarthquakesMapper.class);
    schedulersProvider = new SchedulersProvider();
    googleApiAvailability = GoogleApiAvailability.getInstance();
    OkHttpClient client = provideOkHttpClient();
    Gson gson = provideGson();
    Retrofit retrofit = provideRetrofit(client, gson);
    apiService = provideApiService(retrofit);

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
    Disposable googleDisposable = checkLocationServicesAvailability()
        .filter(available -> !available)
        .flatMap(available -> getLocationServicesStatus().toMaybe())
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

  /**
   * Remove all {@link EarthquakeDb from cache}
   */
  public void clearCache() {
    earthquakeBox.removeAll();
  }

  /**
   * @param earthquakeEntities to store
   */
  public void putEarthquakes(List<Earthquake> earthquakeEntities) {
    earthquakeBox.put(earthquakesMapper.entitiesToDb(earthquakeEntities));
  }

  /**
   * @return all {@link EarthquakeDb} from the cache
   */
  public Single<List<EarthquakeDb>> getEarthquakes() {
    Query<EarthquakeDb> query = earthquakeBox.query().build();
    return RxQuery.observable(query)
        .first(new ArrayList<>())
        .doOnSuccess(
            earthquakeEntities -> Timber.d("%d entities are in cache", earthquakeEntities.size()));
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
    return getCurrentCoordinatesBus()
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
        .flatMap(locationAnswer -> getEarthquakeAlert(locationAnswer.getCoordinates()));
  }

  /**
   * Check for permission and get current coordinates. Use states to show if location found successfully, or permission
   * denied.
   *
   * @return state of request and current {@link Coordinates}. In case of problems, default coordinates are returned. In
   * this case, coordinates of Moscow, Russia
   */
  public Single<LocationAnswer> getCurrentCoordinatesBus() {
    return requestLocationPermissions()
        .first(false)
        .flatMap(permGiven -> {
          if (permGiven) {
            return getCurrentCoordinates()
                .map(coordinates -> new LocationAnswer(coordinates,
                    LocationInteractor.State.SUCCESS))
                .onErrorReturnItem(
                    new LocationAnswer(getDefaultLocation().getCoords(),
                        LocationInteractor.State.NO_DATA));
          } else {
            return Single.just(
                new LocationAnswer(getDefaultLocation().getCoords(),
                    LocationInteractor.State.PERMISSION_DENIED));
          }
        })
        .subscribeOn(schedulersProvider.io());
  }

  /**
   * Get the closest to the given position Earthquake that satisfies program settings (maximal distance and minimal
   * magnitude, details in {@link SettingsInteractor})
   *
   * @param coords of user
   * @return {@link EntitiesWrapper} with different states (see {@link State}) and the closest {@link
   * EarthquakeWithDist} if it was found
   */
  public Single<EntitiesWrapper<EarthquakeWithDist>> getEarthquakeAlertBusiness(
      Location.Coordinates coords) {
    return getApiDataSorted(coords, distanceComparator)
        .flattenAsObservable(earthquakeWithDists -> earthquakeWithDists)
        .filter(earthquakeWithDist -> earthquakeWithDist.getDistance() < getAlertMaxDistance())
        .filter(earthquakeWithDist -> earthquakeWithDist.getMagnitude() >= getAlertMinMagnitude())
        .toList()
        .map(earthquakeWithDists -> earthquakeWithDists.isEmpty() ?
            new EntitiesWrapper<EarthquakeWithDist>(EntitiesWrapper.State.EMPTY, null) :
            new EntitiesWrapper<EarthquakeWithDist>(EntitiesWrapper.State.EMPTY.SUCCESS,
                earthquakeWithDists.get(0)))
        .onErrorReturnItem(
            new EntitiesWrapper<EarthquakeWithDist>(EntitiesWrapper.State.ERROR_NETWORK, null))
        .subscribeOn(schedulersProvider.io());
  }

  protected void unsubscribeOnDestroy(Disposable disposable) {
    compositeDisposable.add(disposable);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    compositeDisposable.clear();
  }

  /**
   * Get the closest to the given position Earthquake that satisfies program settings (maximal distance and minimal
   * magnitude, details in {@link SettingsInteractor})
   *
   * @param coords of user
   * @return {@link EntitiesWrapper} with different states (see {@link State}) and the closest {@link
   * EarthquakeWithDist} if it was found
   */
  public Single<EntitiesWrapper<EarthquakeWithDist>> getEarthquakeAlert(
      Location.Coordinates coords) {
    return getApiDataSorted(coords, distanceComparator)
        .flattenAsObservable(earthquakeWithDists -> earthquakeWithDists)
        .filter(earthquakeWithDist -> earthquakeWithDist.getDistance() < getAlertMaxDistance())
        .filter(earthquakeWithDist -> earthquakeWithDist.getMagnitude() >= getAlertMinMagnitude())
        .toList()
        .map(earthquakeWithDists -> earthquakeWithDists.isEmpty() ?
            new EntitiesWrapper<EarthquakeWithDist>(EntitiesWrapper.State.EMPTY, null) :
            new EntitiesWrapper<EarthquakeWithDist>(EntitiesWrapper.State.SUCCESS,
                earthquakeWithDists.get(0)))
        .onErrorReturnItem(
            new EntitiesWrapper<EarthquakeWithDist>(EntitiesWrapper.State.ERROR_NETWORK, null))
        .subscribeOn(schedulersProvider.io());
  }

  /**
   * Download earthquakes list from server, calculate distance to the given location, and return list of {@link
   * EarthquakeWithDist} sorted by distance
   */
  private Single<List<EarthquakeWithDist>> getApiDataSorted(Location.Coordinates coords,
      Comparator<EarthquakeWithDist> distanceComparator) {
    return getTodaysEarthquakesFromApi()
        .flattenAsObservable(earthquakes -> earthquakes)
        .map(earthquakeEntity -> new EarthquakeWithDist(earthquakeEntity, coords))
        .toSortedList(distanceComparator)
        .subscribeOn(schedulersProvider.io());
  }

  /**
   * Get earthquakes from API, clear cache and put new ones there
   *
   * @return list of todays earthquakes
   */
  public Single<List<Earthquake>> getTodaysEarthquakesFromApi() {
    return apiService.getEarthquakes()
        .map(EarthquakesResponse::getFeatures)
        .flattenAsObservable(items -> items)
        .map(earthquakesMapper::earthquakeDataToEntity)
        .toList()
        .doOnSuccess(earthquakeEntities -> {
          clearCache();
          putEarthquakes(earthquakeEntities);
        })
        .doOnSuccess(earthquakeEntities -> Timber
            .d("%d entities came from server", earthquakeEntities.size()));
  }

  /**
   * @return maximal distance to which an Earthquake alert should be shown
   */
  public double getAlertMaxDistance() {
    return getDoubleFromLongFromPrefs(KEY_MAX_DIST);
  }

  /**
   * Convenient method to read double without losing precision while converting to float
   */
  private double getDoubleFromLongFromPrefs(String key) {
    if (!sharedPreferences.contains(key)) {
      return 0;
    }
    return Double.longBitsToDouble(sharedPreferences.getLong(key, 0));
  }

  /**
   * @return maximal magnitude from which an Earthquake alert should be shown
   */
  public double getAlertMinMagnitude() {
    return getDoubleFromLongFromPrefs(KEY_MIN_MAG);
  }

  /**
   * @return if location services, needed for the app are available
   */
  public Single<Boolean> checkLocationServicesAvailability() {
    return Single.just(checkPlayServicesAvailable())
        .subscribeOn(schedulersProvider.io());
  }

  /**
   * @return if location services, needed for the app are available
   */
  public boolean checkPlayServicesAvailable() {
    final int status = googleApiAvailability.isGooglePlayServicesAvailable(this);

    if (status != ConnectionResult.SUCCESS) {
      isGoogleApiAvailable = false;
    }
    return isGoogleApiAvailable;
  }

  /**
   * @return status code from the location services
   */
  public Single<Integer> getLocationServicesStatus() {
    return Single.just(getPlayServicesStatus())
        .subscribeOn(schedulersProvider.io());
  }

  /**
   * @return Current coordinates, taken from the Android system
   * @throws {@link UnknownLocationException} if the last position is unknown
   * @throws {@link SecurityException} if we try to get position without permissions
   */
  public Single<Location.Coordinates> getCurrentCoordinates() {
    return getLastLocation()
        .map(location -> new Location.Coordinates(location.getLongitude(), location.getLatitude()));
  }

  /**
   * Get last known location of the user. If there are no Google services on phone, we will have an error
   */
  private Single<android.location.Location> getLastLocation() {
    return Single.create(emitter -> {
      try {
        fusedLocationClient.getLastLocation()
            .addOnSuccessListener(location -> {
              if (emitter.isDisposed()) {
                return;
              }

              // GPS location can be null if GPS is switched off or we don't have play services
              if (location != null) {
                emitter.onSuccess(location);
              } else {
                // TODO: add here a call for a new location update
                emitter.onError(new UnknownLocationException("Last location is unknown"));
              }
            })
            .addOnFailureListener(emitter::onError);
      } catch (SecurityException ex) {
        emitter.onError(ex);
      }
    });
  }

  /**
   * @return status code from the location services
   */
  public int getPlayServicesStatus() {
    return googleApiAvailability.isGooglePlayServicesAvailable(this);
  }

  /**
   * Checks if the location permissions are given
   *
   * @return true if permission granted
   */
  public Observable<Boolean> requestLocationPermissions() {
    return rxPermissions.request(permission.ACCESS_FINE_LOCATION);
  }

  /**
   * Get default location to use if there are problems getting user's actual location Å
   *
   * @return default location
   */
  public Location getDefaultLocation() {
    return new Location(DEFAULT_CITY, DEFAULT_COORDINATES);
  }

  Retrofit provideRetrofit(OkHttpClient client, Gson gson) {
    return new Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .build();
  }

  OkHttpClient provideOkHttpClient() {
    OkHttpClient.Builder builder = new OkHttpClient.Builder();
    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(
        message -> Timber.v(message));
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

    builder.addInterceptor(loggingInterceptor)
        .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT, TimeUnit.SECONDS)
        .connectTimeout(TIMEOUT, TimeUnit.SECONDS);
    return builder.build();
  }

  Gson provideGson() {
    return new GsonBuilder()
        .registerTypeAdapter(Date.class,
            (JsonDeserializer<Date>) (json, typeOfT, context)
                -> new Date(json.getAsJsonPrimitive().getAsLong()))
        .create();
  }

  EarthquakesApiService provideApiService(Retrofit retrofit) {
    return retrofit.create(EarthquakesApiService.class);
  }

  public enum State {
    SUCCESS,
    PERMISSION_DENIED,
    NO_DATA
  }
}