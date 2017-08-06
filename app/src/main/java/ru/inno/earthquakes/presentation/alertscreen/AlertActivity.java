package ru.inno.earthquakes.presentation.alertscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.util.Locale;

import ru.inno.earthquakes.EartquakeApp;
import ru.inno.earthquakes.R;
import ru.inno.earthquakes.entities.EarthquakeWithDist;
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

    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView messageView;
    private TextView detailsView;
    private TextView magnitudeView;
    private TextView distanceView;
    private ImageView alertImageView;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messageView = (TextView) findViewById(R.id.alert_tv_main);
        detailsView = (TextView) findViewById(R.id.alert_tv_details);
        magnitudeView = (TextView) findViewById(R.id.alert_tv_magnitude);
        distanceView = (TextView) findViewById(R.id.alert_tv_distance);
        alertImageView = (ImageView) findViewById(R.id.alert_iv_status);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.alert_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> presenter.onRefreshAction());
        findViewById(R.id.alert_btn_show_all).setOnClickListener(v -> presenter.onShowAll());
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

    @ProvidePresenter
    AlertPresenter providePresenter() {
        return new AlertPresenter(EartquakeApp.getComponentsManager().getEarthquakesComponent());
    }

    @Override
    public void showThereAreNoAlerts() {
        messageView.setText(R.string.alert_msg_everything_is_ok);
        alertImageView.setImageResource(R.drawable.earth_normal);
    }

    @Override
    public void showEartquakeAlert(EarthquakeWithDist earthquake) {
        alertImageView.setImageResource(R.drawable.earth_alarm);
        messageView.setText(R.string.alert_msg_earhquake_nearby);
        detailsView.setText(earthquake.getEarthquake().getTitle());
        distanceView.setText(String.format("\u2248 %s km from you", Utils.formatDistanceString(earthquake.getDistance())));
        String magnitude = String.format(Locale.GERMANY, "%.2f", earthquake.getEarthquake().getMagnitude());
        magnitudeView.setText(magnitude);
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