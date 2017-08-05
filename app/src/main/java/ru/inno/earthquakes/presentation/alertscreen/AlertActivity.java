package ru.inno.earthquakes.presentation.alertscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.util.Locale;

import ru.inno.earthquakes.EartquakeApp;
import ru.inno.earthquakes.R;
import ru.inno.earthquakes.entities.EarthquakeWithDist;
import ru.inno.earthquakes.presentation.earthquakeslist.EarthquakesListActivity;

/**
 * @author Artur Badretdinov (Gaket)
 *         22.07.17
 */
public class AlertActivity extends MvpAppCompatActivity implements AlertView, SwipeRefreshLayout.OnRefreshListener {

    @InjectPresenter
    AlertPresenter presenter;

    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView messageView;
    private TextView detailsView;
    private TextView magnitudeView;
    private Snackbar snackbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messageView = (TextView) findViewById(R.id.alert_tv_message);
        detailsView = (TextView) findViewById(R.id.alert_tv_details);
        magnitudeView = (TextView) findViewById(R.id.alert_tv_magnitude);
        findViewById(R.id.alert_btn_show_all).setOnClickListener(v -> presenter.onShowAllAction());
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.alert_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @ProvidePresenter
    AlertPresenter providePresenter() {
        return new AlertPresenter(EartquakeApp.getComponentsManager().getEarthquakesComponent());
    }

    @Override
    public void showThereAreNoAlerts() {
        messageView.setText(R.string.alert_msg_everything_is_ok);
    }

    @Override
    public void showEartquakeAlert(EarthquakeWithDist earthquake) {
        messageView.setText(R.string.alert_msg_earhquake_nearby);
        detailsView.setText(earthquake.getEarthquakeEntity().getTitle());
        String magnitude = String.format(Locale.GERMANY, "%.2f", earthquake.getEarthquakeEntity().getMagnitude());
        magnitudeView.setText(magnitude);
    }

    @Override
    public void onRefresh() {
        presenter.onRefreshAction();
    }

    @Override
    public void navigateToEarthquakesList() {
        Intent intent = EarthquakesListActivity.getStartIntent(this);
        startActivity(intent);
    }

    @Override
    public void showNetworkError() {
        snackbar = Snackbar.make(swipeRefreshLayout, R.string.error_connection, BaseTransientBottomBar.LENGTH_INDEFINITE)
                .setAction(R.string.action_ok, (d) -> snackbar.dismiss());
        snackbar.show();
    }

    @Override
    public void hideNetworkError() {
        if (snackbar != null) {
            snackbar.dismiss();
        }
    }

    @Override
    public void showLoading() {
        if (!swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void hideLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }
}