package ru.inno.earthquakes.presentation.alertscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.inno.earthquakes.EartquakeApp;
import ru.inno.earthquakes.R;
import ru.inno.earthquakes.entities.EarthquakeWithDist;
import ru.inno.earthquakes.model.earthquakes.EarthquakesInteractor;
import ru.inno.earthquakes.model.location.LocationInteractor;
import ru.inno.earthquakes.model.settings.SettingsInteractor;
import ru.inno.earthquakes.presentation.common.Utils;
import ru.inno.earthquakes.presentation.earthquakeslist.EarthquakesListActivity;
import ru.inno.earthquakes.presentation.settings.SettingsActivity;

/**
 * @author Artur Badretdinov (Gaket)
 *         22.07.17
 */
public class AlertActivity extends MvpAppCompatActivity implements AlertView {

    @InjectPresenter
    AlertPresenter presenter;
    @Inject
    EarthquakesInteractor earthquakesInteractor;
    @Inject
    LocationInteractor locationInteractor;
    @Inject
    SettingsInteractor settinsInteractor;

    @BindView(R.id.alert_swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.alert_message)
    TextView messageView;
    @BindView(R.id.alert_details)
    TextView detailsView;
    @BindView(R.id.alert_magnitude)
    TextView magnitudeView;
    @BindView(R.id.alert_distance)
    TextView distanceView;
    @BindView(R.id.alert_status)
    ImageView alertImageView;
    private Snackbar snackbar;

    @ProvidePresenter
    AlertPresenter providePresenter() {
        return new AlertPresenter(earthquakesInteractor, locationInteractor, settinsInteractor);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EartquakeApp.getComponentsManager().getEarthquakesComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        swipeRefreshLayout.setOnRefreshListener(() -> presenter.onRefreshAction());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_alert, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                presenter.onOpenSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.alert_show_all)
    void onShowAllAction() {
        presenter.onShowAll();
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
        String magnitude = String.format(Locale.GERMANY, "%.2f", earthquake.getEarthquake().getMagnitude());
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
}