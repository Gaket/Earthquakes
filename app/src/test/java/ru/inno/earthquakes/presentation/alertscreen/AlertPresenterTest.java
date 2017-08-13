package ru.inno.earthquakes.presentation.alertscreen;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import ru.inno.earthquakes.entities.EarthquakeWithDist;
import ru.inno.earthquakes.entities.Location;
import ru.inno.earthquakes.model.EntitiesWrapper;
import ru.inno.earthquakes.model.earthquakes.EarthquakesInteractor;
import ru.inno.earthquakes.model.location.LocationInteractor;
import ru.inno.earthquakes.model.settings.SettingsInteractor;
import ru.inno.earthquakes.presentation.common.SchedulersProvider;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Artur Badretdinov (Gaket)
 *         08.08.17
 */
@RunWith(MockitoJUnitRunner.class)
public class AlertPresenterTest {

    private Location.Coordinates testCoords;
    private AlertPresenter alertPresenter;

    @Mock
    EarthquakesInteractor earthquakesInteractor;
    @Mock
    LocationInteractor locationInteractor;
    @Mock
    SettingsInteractor settingsInteractor;
    @Mock
    SchedulersProvider schedulersProvider;
    @Mock
    AlertView alertView;

    @Before
    public void setUp() throws Exception {
        when(settingsInteractor.getSettingsChangeObservable()).thenReturn(Observable.empty());
        when(locationInteractor.getCurrentCoordinates()).thenReturn(
                Single.just(new LocationInteractor.LocationAnswer(testCoords, LocationInteractor.State.SUCCESS)));
        when(locationInteractor.checkLocationServicesAvailability()).thenReturn(Single.just(true));
        when(earthquakesInteractor.getEarthquakeAlert(testCoords)).thenReturn(Single.just(new EntitiesWrapper<>(EntitiesWrapper.State.SUCCESS, new EarthquakeWithDist())));
        when(schedulersProvider.ui()).thenReturn(Schedulers.trampoline());
        alertPresenter = new AlertPresenter(earthquakesInteractor, locationInteractor, settingsInteractor, schedulersProvider);
        testCoords = new Location.Coordinates(0, 0);
    }

    @Test
    public void nothingHappensInitially() throws Exception {
        verifyZeroInteractions(earthquakesInteractor);
        verifyZeroInteractions(locationInteractor);
        verifyZeroInteractions(alertView);
    }

    @Test
    public void requestsAndshowEarthquakeAfterInit() throws Exception {
        alertPresenter.attachView(alertView);
        verify(earthquakesInteractor).getEarthquakeAlert(any());
        verify(alertView).showLoading(true);
        verify(alertView).showLoading(false);
        verify(alertView).showNetworkError(false);
        verify(alertView).showEarthquakeAlert(any());
    }

    @Test
    public void requestsAndShowNoEarthquakesAfterInit() throws Exception {
        when(earthquakesInteractor.getEarthquakeAlert(any()))
                .thenReturn(Single.just(new EntitiesWrapper<>(EntitiesWrapper.State.EMPTY, null)));

        alertPresenter.attachView(alertView);
        verify(earthquakesInteractor).getEarthquakeAlert(any());
        verify(alertView).showLoading(true);
        verify(alertView).showLoading(false);
        verify(alertView).showNetworkError(false);
        verify(alertView).showThereAreNoAlerts();
    }

    @Test
    public void requestsAndShowsNetworkErrorAfterInit() throws Exception {
        when(earthquakesInteractor.getEarthquakeAlert(any()))
                .thenReturn(Single.just(new EntitiesWrapper<>(EntitiesWrapper.State.ERROR_NETWORK, null)));

        alertPresenter.attachView(alertView);

        verify(earthquakesInteractor).getEarthquakeAlert(any());
        verify(alertView).showLoading(true);
        verify(alertView).showLoading(false);

        verify(alertView).showThereAreNoAlerts();
        verify(alertView).showNetworkError(true);
    }
}