package ru.inno.earthquakes.presentation.earthquakeslist;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import ru.inno.earthquakes.EartquakeApp;
import ru.inno.earthquakes.R;
import ru.inno.earthquakes.entities.EarthquakeWithDist;
import ru.inno.earthquakes.model.earthquakes.EarthquakesInteractor;
import ru.inno.earthquakes.model.location.LocationInteractor;
import ru.inno.earthquakes.presentation.common.EmptyRecyclerView;
import ru.inno.earthquakes.presentation.common.SchedulersProvider;
import ru.inno.earthquakes.presentation.common.SmartDividerItemDecoration;
import ru.inno.earthquakes.presentation.common.controller.BaseController;

public class EarthquakesListController extends BaseController
        implements EarthquakesListView {

    @InjectPresenter
    EarthquakesListPresenter presenter;
    @Inject
    EarthquakesInteractor earthquakesInteractor;
    @Inject
    LocationInteractor locationInteractor;
    @Inject
    SchedulersProvider schedulersProvider;

    @BindView(R.id.earthquakes_swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.earthquakes_recycler)
    EmptyRecyclerView earthquakesView;
    private EarthquakesListAdapter earthquakesListAdapter;
    private Snackbar snackbar;


    @ProvidePresenter
    EarthquakesListPresenter providePresenter() {
        EartquakeApp.getComponentsManager().getEarthquakesComponent().inject(this);
        return new EarthquakesListPresenter(earthquakesInteractor, locationInteractor, schedulersProvider);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getRouter().popCurrentController();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);
        swipeRefreshLayout.setOnRefreshListener(() -> presenter.onRefreshAction());
        initRecyclerView();
    }

    @NonNull
    @Override
    protected String getTitle() {
        return getResources().getString(R.string.title_earthquake_list);
    }

    private void initRecyclerView() {
        earthquakesView.setLayoutManager(new LinearLayoutManager(getActivity()));
        earthquakesView.setEmptyViewLayout(R.layout.empty_earthquakes);
        SmartDividerItemDecoration itemDecoration = new SmartDividerItemDecoration.Builder(getActivity())
                .setMarginProvider((position, parent) -> 56)
                .build();
        earthquakesView.addItemDecoration(itemDecoration);
        earthquakesListAdapter = new EarthquakesListAdapter(earthquakeWithDist -> presenter.onEarthquakeClick(earthquakeWithDist));
        earthquakesView.setAdapter(earthquakesListAdapter);
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
    public void navigateToEarthquakeDetails(EarthquakeWithDist earthquakeWithDist) {
        Uri webpage = Uri.parse(earthquakeWithDist.getDetailsUrl());
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (getActivity() != null
                && intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void showEarthquakes(List<EarthquakeWithDist> earthquakeWithDists) {
        earthquakesListAdapter.setItems(earthquakeWithDists);
    }

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.activity_earth_quakes_list, container, false);
    }
}
