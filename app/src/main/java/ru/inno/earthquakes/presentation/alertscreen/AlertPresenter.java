package ru.inno.earthquakes.presentation.alertscreen;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import ru.inno.earthquakes.entities.EarthquakeWithDist;
import ru.inno.earthquakes.model.EntitiesWrapper;
import ru.inno.earthquakes.model.earthquakes.EarthquakesInteractor;
import ru.inno.earthquakes.model.location.LocationInteractor;
import ru.inno.earthquakes.model.settings.SettingsInteractor;
import timber.log.Timber;

/**
 * @author Artur Badretdinov (Gaket)
 *         22.07.17
 */
@InjectViewState
public class AlertPresenter extends MvpPresenter<AlertView> {

    private EarthquakesInteractor earthquakesInteractor;
    private LocationInteractor locationInteractor;
    private CompositeDisposable compositeDisposable;

    AlertPresenter(EarthquakesInteractor earthquakesInteractor,
                   LocationInteractor locationInteractor,
                   SettingsInteractor settingsInteractor) {

        compositeDisposable = new CompositeDisposable();
        this.earthquakesInteractor = earthquakesInteractor;
        this.locationInteractor = locationInteractor;
        Disposable disposable = settingsInteractor.getSettingsChangeObservable()
                .subscribe(updated -> onRefreshAction(), Timber::e);
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onFirstViewAttach() {
        updateCurrentState();
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    void onRefreshAction() {
        updateCurrentState();
    }

    void onShowAll() {
        getViewState().navigateToEarthquakesList();
    }

    void onOpenSettings() {
        getViewState().navigateToSettings();
    }

    private void updateCurrentState() {
        Disposable disposable = getEarthquakeAlert()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disp -> getViewState().showLoading(true))
                .doAfterTerminate(() -> getViewState().showLoading(false))
                .subscribe(this::handleEartquakesAnswer, Timber::e);
        compositeDisposable.add(disposable);
    }

    private void handleEartquakesAnswer(EntitiesWrapper<EarthquakeWithDist> earthquakeWithDists) {
        handleNetworkStateMessage(earthquakeWithDists);
        handleEarthquakeData(earthquakeWithDists);
    }

    private void handleEarthquakeData(EntitiesWrapper<EarthquakeWithDist> earthquakeWithDists) {
        if (earthquakeWithDists.getState() == EntitiesWrapper.State.EMPTY) {
            getViewState().showThereAreNoAlerts();
        } else if (earthquakeWithDists.getState() == EntitiesWrapper.State.SUCCESS) {
            getViewState().showEartquakeAlert(earthquakeWithDists.getData());
        }
    }

    private void handleNetworkStateMessage(EntitiesWrapper<EarthquakeWithDist> earthquakeWithDists) {
        if (earthquakeWithDists.getState() == EntitiesWrapper.State.ERROR_NETWORK) {
            getViewState().showNetworkError(true);
        } else {
            getViewState().showNetworkError(false);
        }
    }

    private Single<EntitiesWrapper<EarthquakeWithDist>> getEarthquakeAlert() {
        return locationInteractor.getCurrentCoordinates()
                .flatMap(coords -> earthquakesInteractor.getEarthquakeAlert(coords));
    }
}
