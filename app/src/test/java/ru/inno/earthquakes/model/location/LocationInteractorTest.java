package ru.inno.earthquakes.model.location;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import ru.inno.earthquakes.entities.Location;
import ru.inno.earthquakes.model.permissions.PermissionsRepository;

/**
 * @author Artur Badretdinov (Gaket)
 *         07.08.17
 */
@RunWith(MockitoJUnitRunner.class)
public class LocationInteractorTest {

    private LocationInteractor locationInteractor;
    private Location.Coordinates defaultCoordinates;
    private RuntimeException testError;

    @Mock
    LocationRepository locationRepository;
    @Mock
    PermissionsRepository permissionsRepository;

    private Location.Coordinates testCoordinates = new Location.Coordinates(15, 22.5);

    @Before
    public void setUp() throws Exception {
        locationInteractor = new LocationInteractor(locationRepository, permissionsRepository);
        defaultCoordinates = new Location.Coordinates(55.755826, 37.6173);
        testError = new RuntimeException("Test error");
    }

    @Test
    public void nothingHappenedInitially() throws Exception {
        Mockito.verifyZeroInteractions(locationRepository);
        Mockito.verifyZeroInteractions(permissionsRepository);
    }

    @Test
    public void correctLocationReturnedIfPermissionIsGiven() throws Exception {
        Mockito.when(permissionsRepository.requestLocationPermissions())
                .thenReturn(Observable.just(true));
        Mockito.when(locationRepository.getCurrentCoordinates())
                .thenReturn(Single.just(testCoordinates));

        TestObserver<Location.Coordinates> testObserver = locationInteractor.getCurrentCoordinates().test();
        testObserver.awaitTerminalEvent();

        Mockito.verify(permissionsRepository).requestLocationPermissions();
        Mockito.verify(locationRepository).getCurrentCoordinates();
        testObserver.assertNoErrors();
        testObserver.assertValue(testCoordinates);
    }

    @Test
    public void defaultLocationReturnedIfPermissionIsNotGiven() throws Exception {
        Mockito.when(permissionsRepository.requestLocationPermissions())
                .thenReturn(Observable.just(false));

        TestObserver<Location.Coordinates> testObserver = locationInteractor.getCurrentCoordinates().test();
        testObserver.awaitTerminalEvent();

        Mockito.verify(permissionsRepository).requestLocationPermissions();
        Mockito.verifyZeroInteractions(locationRepository);
        testObserver.assertNoErrors();
        testObserver.assertValue(defaultCoordinates);
    }

    @Test
    public void defaultLocationReturnedIfThereAreNoPreviousLocationsInDevice() throws Exception {
        Mockito.when(permissionsRepository.requestLocationPermissions())
                .thenReturn(Observable.just(true));
        Mockito.when(locationRepository.getCurrentCoordinates())
                .thenReturn(Single.error(testError));

        TestObserver<Location.Coordinates> testObserver = locationInteractor.getCurrentCoordinates().test();
        testObserver.awaitTerminalEvent();

        Mockito.verify(permissionsRepository).requestLocationPermissions();
        Mockito.verify(locationRepository).getCurrentCoordinates();
        testObserver.assertNoErrors();
        testObserver.assertValue(defaultCoordinates);
    }
}