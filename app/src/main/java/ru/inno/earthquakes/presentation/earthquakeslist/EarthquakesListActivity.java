package ru.inno.earthquakes.presentation.earthquakeslist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.util.List;

import ru.inno.earthquakes.EartquakeApp;
import ru.inno.earthquakes.R;
import ru.inno.earthquakes.entities.EarthquakeWithDist;

public class EarthquakesListActivity extends MvpAppCompatActivity
        implements EarthquakesListView {

    @InjectPresenter
    EarthquakesListPresenter presenter;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private EarthquakesListAdapter earthquakesListAdapter;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earth_quakes_list);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.earthquakes_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> presenter.onRefreshAction());
        recyclerView = (RecyclerView) findViewById(R.id.earthquakes_recycler);
        earthquakesListAdapter = new EarthquakesListAdapter();
        recyclerView.setAdapter(earthquakesListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @ProvidePresenter
    EarthquakesListPresenter providePresenter() {
        return new EarthquakesListPresenter(EartquakeApp.getComponentsManager().getEarthquakesComponent());
    }

    public static Intent getStartIntent(Context callingContext) {
        return new Intent(callingContext, EarthquakesListActivity.class);
    }

    @Override
    public void navigateToEarthquakesList() {
        throw new UnsupportedOperationException("Will be ready soon");
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
    public void showEarthquakes(List<EarthquakeWithDist> earthquakeWithDists) {
        earthquakesListAdapter.setItems(earthquakeWithDists);
    }

}
