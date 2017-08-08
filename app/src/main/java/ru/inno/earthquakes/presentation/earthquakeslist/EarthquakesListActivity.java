package ru.inno.earthquakes.presentation.earthquakeslist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.inno.earthquakes.EartquakeApp;
import ru.inno.earthquakes.R;
import ru.inno.earthquakes.entities.EarthquakeWithDist;
import ru.inno.earthquakes.presentation.common.EmptyRecyclerView;
import ru.inno.earthquakes.presentation.common.SmartDividerItemDecoration;
import timber.log.Timber;

public class EarthquakesListActivity extends MvpAppCompatActivity
        implements EarthquakesListView {

    @InjectPresenter
    EarthquakesListPresenter presenter;

    @BindView(R.id.earthquakes_swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.earthquakes_recycler)
    EmptyRecyclerView earthquakesView;
    private EarthquakesListAdapter earthquakesListAdapter;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earth_quakes_list);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            Timber.e("Action bar is null in Earthquakes Activity");
        }
        swipeRefreshLayout.setOnRefreshListener(() -> presenter.onRefreshAction());
        initRecyclerView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initRecyclerView() {
        earthquakesView.setLayoutManager(new LinearLayoutManager(this));
        earthquakesView.setEmptyViewLayout(R.layout.empty_earthquakes);
        SmartDividerItemDecoration itemDecoration = new SmartDividerItemDecoration.Builder(this)
                .setMarginProvider((position, parent) -> 56)
                .build();
        earthquakesView.addItemDecoration(itemDecoration);
        earthquakesListAdapter = new EarthquakesListAdapter();
        earthquakesView.setAdapter(earthquakesListAdapter);
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
