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

import java.util.Locale;

import javax.inject.Inject;

import ru.inno.earthquakes.EartquakeApp;
import ru.inno.earthquakes.R;
import ru.inno.earthquakes.entities.EarthquakeWithDist;
import ru.inno.earthquakes.model.earthquakes.EarthquakesInteractor;
import ru.inno.earthquakes.model.location.LocationInteractor;
import ru.inno.earthquakes.model.settings.SettingsInteractor;
import ru.inno.earthquakes.presentation.common.SchedulersProvider;
import ru.inno.earthquakes.presentation.common.Utils;
import ru.inno.earthquakes.presentation.earthquakeslist.EarthquakesListActivity;
import ru.inno.earthquakes.presentation.settings.SettingsActivity;

/**
 * @author Artur Badretdinov (Gaket)
 *         22.07.17
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

    @ProvidePresenter
    AlertPresenter providePresenter() {
        return new AlertPresenter(earthquakesInteractor, locationInteractor, settinsInteractor, schedulersProvider);
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
        EartquakeApp.getComponentsManager().getEarthquakesComponent().inject(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        messageView = (TextView) findViewById(R.id.alert_message);
        detailsView = (TextView) findViewById(R.id.alert_details);
        magnitudeView = (TextView) findViewById(R.id.alert_magnitude);
        distanceView = (TextView) findViewById(R.id.alert_distance);
        alertImageView = (ImageView) findViewById(R.id.alert_status);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.alert_swipe_refresh);
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
        detailsView.setVisibility(View.GONE);
        distanceView.setVisibility(View.GONE);
        magnitudeView.setVisibility(View.GONE);
    }

    @Override
    public void showEartquakeAlert(EarthquakeWithDist earthquake) {
        alertImageView.setImageResource(R.drawable.earth_alarm);
        messageView.setText(R.string.alert_msg_earhquake_nearby);
        detailsView.setText(earthquake.getEarthquake().getTitle());
        distanceView.setText(String.format("\u2248 %s km from you", Utils.formatDistanceString(earthquake.getDistance())));
        String magnitude = String.format(Locale.getDefault(), "%.2f", earthquake.getEarthquake().getMagnitude());
        magnitudeView.setText(magnitude);
        detailsView.setVisibility(View.VISIBLE);
        distanceView.setVisibility(View.VISIBLE);
        magnitudeView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNetworkError(boolean show) {
        if (show) {
            snackbar = Snackbar.make(swipeRefreshLayout, R.string.error_connection, BaseTransientBottomBar.LENGTH_INDEFINITE)
                    .setAction(R.string.action_ok, (d) -> snackbar.dismiss());
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
}