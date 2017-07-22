package ru.inno.earthquakes.presentation.alertscreen;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import ru.inno.earthquakes.di.earthquakes.EarthquakesComponent;
import ru.inno.earthquakes.model.earthquakes.business.EarthquakesInteractor;
import ru.inno.earthquakes.model.location.business.LocationInteractor;
import timber.log.Timber;

/**
 * @author Artur Badretdinov (Gaket)
 *         22.07.17.
 */
@InjectViewState
public class AlertPresenter extends MvpPresenter<AlertView> {

    @Inject
    EarthquakesInteractor earthquakesInteractor;
    @Inject
    LocationInteractor locationInteractor;

    public AlertPresenter(EarthquakesComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        locationInteractor.getCurrentCoordinates()
                .flatMap(coords -> earthquakesInteractor.getTodaysEartquakesSortedByLocation(coords))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(earthquakeWithDists -> {
                    if (earthquakeWithDists.isEmpty()) {
                        getViewState().showThereAreNoAlerts();
                    } else {
                        getViewState().showEartqhakeAlert(earthquakeWithDists.get(0));
                    }
                }, Timber::e);
    }
}
