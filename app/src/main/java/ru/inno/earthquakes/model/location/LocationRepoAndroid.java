package ru.inno.earthquakes.model.location;

import com.google.android.gms.location.FusedLocationProviderClient;

import io.reactivex.Single;
import ru.inno.earthquakes.entities.Location;

/**
 * @author Artur Badretdinov (Gaket)
 *         22.07.17.
 */
public class LocationRepoAndroid implements LocationRepository {

    private FusedLocationProviderClient fusedLocationClient;

    public LocationRepoAndroid(FusedLocationProviderClient fusedLocationClient) {
        this.fusedLocationClient = fusedLocationClient;
    }

    @Override
    public Single<Location.Coordinates> getCurrentCoordinates() {
        return getLastLocation()
                .map(location -> new Location.Coordinates(location.getLongitude(), location.getLatitude()));
    }

    private Single<android.location.Location> getLastLocation() {
        return Single.create(emitter -> {
            try {
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(location -> {
                            if (emitter.isDisposed()) {
                                return;
                            }

                            // GPS location can be null if GPS is switched off
                            if (location != null) {
                                emitter.onSuccess(location);
                            } else {
                                // TODO: add here call for a new location update
                                emitter.onError(new UnknownLocationException("Last location is unknown"));
                            }
                        })
                        .addOnFailureListener(emitter::onError);
            } catch (SecurityException ex) {
                emitter.onError(ex);
            }
        });
    }
}
