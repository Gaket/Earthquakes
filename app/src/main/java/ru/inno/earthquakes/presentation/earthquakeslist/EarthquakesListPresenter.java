package ru.inno.earthquakes.presentation.earthquakeslist;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import ru.inno.earthquakes.entities.EarthquakeWithDist;
import ru.inno.earthquakes.model.EntitiesWrapper;
import ru.inno.earthquakes.model.earthquakes.EarthquakesInteractor;
import ru.inno.earthquakes.model.location.LocationInteractor;
import ru.inno.earthquakes.presentation.common.SchedulersProvider;
import timber.log.Timber;

/**
 * @author Artur Badretdinov (Gaket)
 *         01.08.17
 */
@InjectViewState
public class EarthquakesListPresenter extends MvpPresenter<EarthquakesListView> {

    private EarthquakesInteractor earthquakesInteractor;
    private LocationInteractor locationInteractor;
    private SchedulersProvider schedulersProvider;
    private CompositeDisposable compositeDisposable;

    EarthquakesListPresenter(EarthquakesInteractor earthquakesInteractor, LocationInteractor locationInteractor, SchedulersProvider schedulersProvider) {
        this.earthquakesInteractor = earthquakesInteractor;
        this.locationInteractor = locationInteractor;
        this.schedulersProvider = schedulersProvider;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getEarthquakesList();
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    private void getEarthquakesList() {
        Disposable disposable = getSortedEartquakesObservable()
                .observeOn(schedulersProvider.ui())
                .doOnSubscribe((s) -> getViewState().showLoading(true))
                .doAfterTerminate(() -> getViewState().showLoading(false))
                .subscribe(earthquakeWithDists -> {
                    if (earthquakeWithDists.getState() == EntitiesWrapper.State.ERROR_NETWORK) {
                        getViewState().showNetworkError(true);
                    } else {
                        getViewState().showNetworkError(false);
                        getViewState().showEarthquakes(earthquakeWithDists.getData());
                    }
                }, Timber::e);
        compositeDisposable.add(disposable);
    }

    private Observable<EntitiesWrapper<List<EarthquakeWithDist>>> getSortedEartquakesObservable() {
        return locationInteractor.getCurrentCoordinates()
                .flatMapObservable(locationAnswer -> earthquakesInteractor.getTodaysEartquakesSortedByLocation(locationAnswer.getCoordinates()));
    }

    void onRefreshAction() {
        getEarthquakesList();
    }
}
