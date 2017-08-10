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
import io.reactivex.schedulers.Schedulers;
import ru.inno.earthquakes.entities.Location;
import ru.inno.earthquakes.model.permissions.PermissionsRepository;
import ru.inno.earthquakes.model.settings.SettingsRepository;
import ru.inno.earthquakes.presentation.common.SchedulersProvider;

import static org.mockito.Mockito.when;

/**
 * @author Artur Badretdinov (Gaket)
 *         07.08.17
 */
@RunWith(MockitoJUnitRunner.class)
public class LocationInteractorTest {

    private LocationInteractor locationInteractor;
    private RuntimeException testError;

    @Mock
    LocationRepository locationRepository;
    @Mock
    PermissionsRepository permissionsRepository;
    @Mock
    SettingsRepository settingsRepository;
    @Mock
    SchedulersProvider schedulersProvider;

    private Location.Coordinates testCoordinates = new Location.Coordinates(15, 22.5);

    @Before
    public void setUp() throws Exception {
        locationInteractor = new LocationInteractor(locationRepository, permissionsRepository, settingsRepository, schedulersProvider);
        testError = new RuntimeException("Test error");
        when(settingsRepository.getDefaultLocation()).thenReturn(new Location());
        when(schedulersProvider.io()).thenReturn(Schedulers.trampoline());
    }

    @Test
    public void nothingHappenedInitially() throws Exception {
        Mockito.verifyZeroInteractions(locationRepository);
        Mockito.verifyZeroInteractions(permissionsRepository);
    }

    @Test
    public void correctLocationReturnedIfPermissionIsGiven() throws Exception {
        when(permissionsRepository.requestLocationPermissions())
                .thenReturn(Observable.just(true));
        when(locationRepository.getCurrentCoordinates())
                .thenReturn(Single.just(testCoordinates));

        TestObserver<LocationInteractor.LocationAnswer> testObserver = locationInteractor.getCurrentCoordinates().test();
        testObserver.awaitTerminalEvent();

        Mockito.verify(permissionsRepository).requestLocationPermissions();
        Mockito.verify(locationRepository).getCurrentCoordinates();
        testObserver.assertNoErrors();
    }

    @Test
    public void defaultLocationReturnedIfPermissionIsNotGiven() throws Exception {
        when(permissionsRepository.requestLocationPermissions())
                .thenReturn(Observable.just(false));

        TestObserver<LocationInteractor.LocationAnswer> testObserver = locationInteractor.getCurrentCoordinates().test();
        testObserver.awaitTerminalEvent();

        Mockito.verify(permissionsRepository).requestLocationPermissions();
        Mockito.verifyZeroInteractions(locationRepository);
        testObserver.assertNoErrors();
    }

    @Test
    public void defaultLocationReturnedIfThereAreNoPreviousLocationsInDevice() throws Exception {
        when(permissionsRepository.requestLocationPermissions())
                .thenReturn(Observable.just(true));
        when(locationRepository.getCurrentCoordinates())
                .thenReturn(Single.error(testError));

        TestObserver<LocationInteractor.LocationAnswer> testObserver = locationInteractor.getCurrentCoordinates().test();
        testObserver.awaitTerminalEvent();

        Mockito.verify(permissionsRepository).requestLocationPermissions();
        Mockito.verify(locationRepository).getCurrentCoordinates();
        testObserver.assertNoErrors();
    }
}