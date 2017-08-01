package ru.inno.earthquakes.model.earthquakes.business;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import ru.inno.earthquakes.entities.Location;
import ru.inno.earthquakes.model.earthquakes.EarthquakesInteractor;
import ru.inno.earthquakes.model.earthquakes.EarthquakesRepository;

/**
 * @author Artur Badretdinov (Gaket)
 *         26.07.17.
 */
@RunWith(MockitoJUnitRunner.class)
public class EarthquakesInteractorTest {

    EarthquakesInteractor interactor;

    @Mock
    EarthquakesRepository repository;
    private RuntimeException testError;

    @Before
    public void setUp() throws Exception {
        interactor = new EarthquakesInteractor(repository);
        testError = new RuntimeException("Test error");
    }

    @Test
    public void getTodaysEartquakes() throws Exception {
        Mockito.when(repository.getTodaysEarthquakes()).thenReturn(Observable.empty());

        TestObserver testObserver = interactor.getTodaysEartquakes().test();
        testObserver.awaitTerminalEvent();

        testObserver.assertNoErrors();
        Mockito.verify(repository).getTodaysEarthquakes();
    }

    @Test
    public void getTodaysEartquakes_error() throws Exception {
        Mockito.when(repository.getTodaysEarthquakes()).thenReturn(Observable.error(testError));

        TestObserver testObserver = interactor.getTodaysEartquakes().test();
        testObserver.awaitTerminalEvent();

        testObserver.assertError(testError);
        Mockito.verify(repository).getTodaysEarthquakes();
    }

    @Test
    public void getTodaysEartquakesSortedByLocation() throws Exception {
        Mockito.when(repository.getTodaysEarthquakes()).thenReturn(Observable.empty());
        Location.Coordinates testCoords = new Location.Coordinates(0, 0);

        TestObserver testObserver = interactor.getTodaysEartquakesSortedByLocation(testCoords).test();
        testObserver.awaitTerminalEvent();

        testObserver.assertNoErrors();
        Mockito.verify(repository).getTodaysEarthquakes();
    }

    @Test
    public void getTodaysEartquakesSortedByLocation_error() throws Exception {
        Mockito.when(repository.getTodaysEarthquakes()).thenReturn(Observable.empty());
        Location.Coordinates testCoords = new Location.Coordinates(0, 0);

        TestObserver testObserver = interactor.getTodaysEartquakesSortedByLocation(testCoords).test();
        testObserver.awaitTerminalEvent();

        testObserver.assertNoErrors();
        Mockito.verify(repository).getTodaysEarthquakes();
    }
}