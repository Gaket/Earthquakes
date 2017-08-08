package ru.inno.earthquakes.presentation.alertscreen;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Observable;
import io.reactivex.Single;
import ru.inno.earthquakes.entities.Location;
import ru.inno.earthquakes.model.EntitiesWrapper;
import ru.inno.earthquakes.model.earthquakes.EarthquakesInteractor;
import ru.inno.earthquakes.model.location.LocationInteractor;
import ru.inno.earthquakes.model.settings.SettingsInteractor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
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

    @Before
    public void setUp() throws Exception {
        when(settingsInteractor.getSettingsChangeObservable()).thenReturn(Observable.just(true));
        when(locationInteractor.getCurrentCoordinates()).thenReturn(Single.just(testCoords));
        when(earthquakesInteractor.getEarthquakeAlert(testCoords)).thenReturn(Single.just(new EntitiesWrapper<>()));
        alertPresenter = new AlertPresenter(earthquakesInteractor, locationInteractor, settingsInteractor);
        testCoords = new Location.Coordinates(0, 0);
    }

    @Test
    public void requestsEarthquakesAfterInit() throws Exception {
        alertPresenter.onFirstViewAttach();

        verify(earthquakesInteractor).getEarthquakeAlert(any());
    }
}