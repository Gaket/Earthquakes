package ru.inno.earthquakes.presentation.alertscreen;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import ru.inno.earthquakes.di.earthquakes.EarthquakesComponent;
import ru.inno.earthquakes.entities.EarthquakeWithDist;
import ru.inno.earthquakes.model.EntitiesWrapper;
import ru.inno.earthquakes.model.earthquakes.EarthquakesInteractor;
import ru.inno.earthquakes.model.location.LocationInteractor;
import timber.log.Timber;

/**
 * @author Artur Badretdinov (Gaket)
 *         22.07.17
 */
@InjectViewState
public class AlertPresenter extends MvpPresenter<AlertView> {

    @Inject
    EarthquakesInteractor earthquakesInteractor;
    @Inject
    LocationInteractor locationInteractor;

    public AlertPresenter(EarthquakesComponent earthquakesComponent) {
        earthquakesComponent.inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        updateCurrentState();
    }

    public void onRefreshAction() {
        updateCurrentState();
    }

    private void updateCurrentState() {
        getSortedEartquakesObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showLoading(true))
                .doAfterTerminate(() -> getViewState().showLoading(false))
                .subscribe(earthquakeWithDists -> {
                    if (earthquakeWithDists.getState() == EntitiesWrapper.State.ERROR_NETWORK) {
                        getViewState().showNetworkError(true);
                    } else {
                        getViewState().showNetworkError(false);
                    }
                    if (earthquakeWithDists.getData().isEmpty()) {
                        getViewState().showThereAreNoAlerts();
                    } else {
                        getViewState().showEartquakeAlert(earthquakeWithDists.getData().get(0));
                    }
                }, Timber::e);
    }

    private Observable<EntitiesWrapper<List<EarthquakeWithDist>>> getSortedEartquakesObservable() {
        return locationInteractor.getCurrentCoordinates()
                .flatMap(coords -> earthquakesInteractor.getTodaysEartquakesSortedByLocation(coords));
    }

    public void onShowAllAction() {
        getViewState().navigateToEarthquakesList();
    }
}
