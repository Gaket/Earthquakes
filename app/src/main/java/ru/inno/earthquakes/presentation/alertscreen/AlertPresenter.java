package ru.inno.earthquakes.presentation.alertscreen;

import com.arellomobile.mvp.InjectViewState;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import ru.inno.earthquakes.entities.EarthquakeWithDist;
import ru.inno.earthquakes.model.EntitiesWrapper;
import ru.inno.earthquakes.model.earthquakes.EarthquakesInteractor;
import ru.inno.earthquakes.model.location.LocationInteractor;
import ru.inno.earthquakes.model.settings.SettingsInteractor;
import ru.inno.earthquakes.presentation.common.BasePresenter;
import ru.inno.earthquakes.presentation.common.SchedulersProvider;
import timber.log.Timber;

/**
 * @author Artur Badretdinov (Gaket)
 *         22.07.17
 */
@InjectViewState
public class AlertPresenter extends BasePresenter<AlertView> {

    private final SettingsInteractor settingsInteractor;
    private EarthquakesInteractor earthquakesInteractor;
    private LocationInteractor locationInteractor;
    private SchedulersProvider schedulersProvider;

    AlertPresenter(EarthquakesInteractor earthquakesInteractor,
                   LocationInteractor locationInteractor,
                   SettingsInteractor settingsInteractor,
                   SchedulersProvider schedulersProvider) {

        super();
        this.earthquakesInteractor = earthquakesInteractor;
        this.locationInteractor = locationInteractor;
        this.schedulersProvider = schedulersProvider;
        this.settingsInteractor = settingsInteractor;
    }

    @Override
    protected void onFirstViewAttach() {
        updateCurrentState();

        // Subscribe to settings updates
        Disposable disposable = settingsInteractor.getSettingsChangeObservable()
                .subscribe(updated -> onRefreshAction(), Timber::e);
        unsubscribeOnDestroy(disposable);

        // Show a message for users if they don't have Google Api Services needed for program
        Disposable googleDisposable = locationInteractor.checkLocationServicesAvailability()
                .filter(available -> !available)
                .flatMap(available -> locationInteractor.getLocationServicesStatus().toMaybe())
                .observeOn(schedulersProvider.ui())
                .subscribe(status -> getViewState().showGoogleApiMessage(status), Timber::e);
        unsubscribeOnDestroy(googleDisposable);
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

    /**
     * Call to update current data
     */
    private void updateCurrentState() {
        Disposable disposable = getEarthquakeAlert()
                .observeOn(schedulersProvider.ui())
                .doOnSubscribe(disp -> getViewState().showLoading(true))
                .doAfterTerminate(() -> getViewState().showLoading(false))
                .subscribe(this::handleEartquakesAnswer, Timber::e);
        unsubscribeOnDestroy(disposable);
    }

    private void handleEartquakesAnswer(EntitiesWrapper<EarthquakeWithDist> earthquakeWithDists) {
        handleNetworkStateMessage(earthquakeWithDists);
        handleEarthquakeData(earthquakeWithDists);
    }

    private void handleEarthquakeData(EntitiesWrapper<EarthquakeWithDist> earthquakeWithDists) {
        if (earthquakeWithDists.getState() == EntitiesWrapper.State.EMPTY) {
            getViewState().showThereAreNoAlerts();
        } else if (earthquakeWithDists.getState() == EntitiesWrapper.State.SUCCESS) {
            getViewState().showEarthquakeAlert(earthquakeWithDists.getData());
        }
    }

    private void handleNetworkStateMessage(EntitiesWrapper<EarthquakeWithDist> earthquakeWithDists) {
        if (earthquakeWithDists.getState() == EntitiesWrapper.State.ERROR_NETWORK) {
            getViewState().showNetworkError(true);
            getViewState().showThereAreNoAlerts();
        } else {
            getViewState().showNetworkError(false);
        }
    }

    /**
     * Get earthquake alert and show user if there are any problems
     */
    private Single<EntitiesWrapper<EarthquakeWithDist>> getEarthquakeAlert() {
        return locationInteractor.getCurrentCoordinates()
                .doOnSuccess(locationAnswer -> {
                    switch (locationAnswer.getState()) {
                        case SUCCESS:
                            // do nothing
                            break;
                        case PERMISSION_DENIED:
                            getViewState().showPermissionDeniedAlert();
                            break;
                        case NO_DATA:
                            getViewState().showNoDataAlert();
                            break;
                    }
                })
                .flatMap(locationAnswer -> earthquakesInteractor.getEarthquakeAlert(locationAnswer.getCoordinates()));
    }
}
