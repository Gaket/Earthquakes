package ru.inno.earthquakes.presentation.alertscreen;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
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

    EarthquakesInteractor earthquakesInteractor;
    LocationInteractor locationInteractor;

    public AlertPresenter(EarthquakesInteractor earthquakesInteractor,
                          LocationInteractor locationInteractor) {
        this.earthquakesInteractor = earthquakesInteractor;
        this.locationInteractor = locationInteractor;
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
                .subscribe(this::handleEartquakesAnswer, Timber::e);
    }

    private void handleEartquakesAnswer(EntitiesWrapper<EarthquakeWithDist> earthquakeWithDists) {
        if (earthquakeWithDists.getState() == EntitiesWrapper.State.ERROR_NETWORK) {
            getViewState().showNetworkError(true);
        } else {
            getViewState().showNetworkError(false);
        }
        if (earthquakeWithDists.getState() == EntitiesWrapper.State.EMPTY) {
            getViewState().showThereAreNoAlerts();
        } else if (earthquakeWithDists.getState() == EntitiesWrapper.State.SUCCESS) {
            getViewState().showEartquakeAlert(earthquakeWithDists.getData());
        }
    }

    private Single<EntitiesWrapper<EarthquakeWithDist>> getSortedEartquakesObservable() {
        return locationInteractor.getCurrentCoordinates()
                .flatMap(coords -> earthquakesInteractor.getEarthquakeAlert(coords));
    }

    public void onShowAll() {
        getViewState().navigateToEarthquakesList();
    }

    public void onOpenSettings() {
        getViewState().navigateToSettings();
    }
}
