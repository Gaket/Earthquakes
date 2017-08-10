package ru.inno.earthquakes.model.earthquakes;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;
import ru.inno.earthquakes.entities.Earthquake;
import ru.inno.earthquakes.entities.EarthquakeWithDist;
import ru.inno.earthquakes.entities.Location;
import ru.inno.earthquakes.model.EntitiesWrapper;
import ru.inno.earthquakes.model.settings.SettingsRepository;
import ru.inno.earthquakes.presentation.common.SchedulersProvider;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Artur Badretdinov (Gaket)
 *         07.08.17
 */
@RunWith(MockitoJUnitRunner.class)
public class EarthquakesInteractorTest {

    public static final double HUGE_DISTANCE = 100_000.0;
    public static final double TINY_DISTANCE = 1.0;
    public static final double ZERO_MAGNITUDE = 0.0;
    public static final double HIGH_MAGNITUDE = 9.0;

    private EarthquakesInteractor interactor;
    private Location.Coordinates testCoords;
    private RuntimeException testError;
    private Earthquake testEarthquake;

    @Mock
    EarthquakesRepository earthquakesRepository;
    @Mock
    SettingsRepository settingsRepository;
    @Mock
    SchedulersProvider schedulersProvider;

    @Before
    public void setUp() throws Exception {
        interactor = new EarthquakesInteractor(earthquakesRepository, settingsRepository, schedulersProvider);
        when(schedulersProvider.io()).thenReturn(Schedulers.trampoline());
        testCoords = new Location.Coordinates(0, 0);
        testError = new RuntimeException("Test error");
        testEarthquake = new Earthquake();
        testEarthquake.setMagnitude(5.0);
        Location location = new Location();
        location.setCoords(new Location.Coordinates(10, 20));
        testEarthquake.setLocation(location);
    }

    @Test
    public void nothingHappenedInitially() throws Exception {
        Mockito.verifyZeroInteractions(earthquakesRepository);
        Mockito.verifyZeroInteractions(settingsRepository);
    }

    @Test
    public void firstCachedThenActualValuesReturned() throws Exception {
        when(earthquakesRepository.getCachedTodaysEarthquakes()).thenReturn(Single.just(new ArrayList<>()));
        when(earthquakesRepository.getTodaysEarthquakesFromApi()).thenReturn(Single.just(new ArrayList<>()));

        TestObserver testObserver = interactor.getTodaysEartquakesSortedByLocation(testCoords).test();
        testObserver.awaitTerminalEvent();

        testObserver.assertNoErrors();
        testObserver.assertValueCount(2);
        verify(earthquakesRepository).getCachedTodaysEarthquakes();
        verify(earthquakesRepository).getTodaysEarthquakesFromApi();
    }

    @Test
    public void cachedValuesReturnedIfNetworkError() throws Exception {
        when(earthquakesRepository.getCachedTodaysEarthquakes()).thenReturn(Single.just(new ArrayList<>()));
        when(earthquakesRepository.getTodaysEarthquakesFromApi()).thenReturn(Single.error(testError));

        TestObserver testObserver = interactor.getTodaysEartquakesSortedByLocation(testCoords).test();
        testObserver.awaitTerminalEvent();

        testObserver.assertNoErrors();
        testObserver.assertValueCount(2);
        verify(earthquakesRepository).getCachedTodaysEarthquakes();
        verify(earthquakesRepository).getTodaysEarthquakesFromApi();
    }

    @Test
    public void earthquakeReturnedIfItPassesSettings() throws Exception {
        when(earthquakesRepository.getTodaysEarthquakesFromApi()).thenReturn(Single.just(Collections.singletonList(testEarthquake)));
        when(settingsRepository.getAlertMaxDistance()).thenReturn(HUGE_DISTANCE);
        when(settingsRepository.getAlertMinMagnitude()).thenReturn(0.0);

        TestObserver testObserver = interactor.getEarthquakeAlert(testCoords).test();
        testObserver.awaitTerminalEvent();

        testObserver.assertNoErrors();
        testObserver.assertValueCount(1);
        verify(earthquakesRepository).getTodaysEarthquakesFromApi();
        testObserver.assertValue(o -> ((EntitiesWrapper<EarthquakeWithDist>) o).getState().equals(EntitiesWrapper.State.SUCCESS));
    }

    @Test
    public void earthquakeNotReturnedIfItDoesNotPassMaxDistanceSettings() throws Exception {
        when(earthquakesRepository.getTodaysEarthquakesFromApi()).thenReturn(Single.just(Collections.singletonList(testEarthquake)));
        when(settingsRepository.getAlertMaxDistance()).thenReturn(TINY_DISTANCE);

        TestObserver testObserver = interactor.getEarthquakeAlert(testCoords).test();
        testObserver.awaitTerminalEvent();

        testObserver.assertNoErrors();
        testObserver.assertValueCount(1);
        verify(earthquakesRepository).getTodaysEarthquakesFromApi();
        testObserver.assertValue(o -> ((EntitiesWrapper<EarthquakeWithDist>) o).getState().equals(EntitiesWrapper.State.EMPTY));
    }

    @Test
    public void earthquakeNotReturnedIfItDoesNotPassMinMagnitudeSettings() throws Exception {
        when(earthquakesRepository.getTodaysEarthquakesFromApi()).thenReturn(Single.just(Collections.singletonList(testEarthquake)));
        when(settingsRepository.getAlertMaxDistance()).thenReturn(HUGE_DISTANCE);
        when(settingsRepository.getAlertMinMagnitude()).thenReturn(HIGH_MAGNITUDE);

        TestObserver testObserver = interactor.getEarthquakeAlert(testCoords).test();
        testObserver.awaitTerminalEvent();

        testObserver.assertNoErrors();
        testObserver.assertValueCount(1);
        verify(earthquakesRepository).getTodaysEarthquakesFromApi();
        testObserver.assertValue(o -> ((EntitiesWrapper<EarthquakeWithDist>) o).getState().equals(EntitiesWrapper.State.EMPTY));
    }
}