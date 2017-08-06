package ru.inno.earthquakes.model.permissions;

import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Observable;

/**
 * @author Artur Badretdinov (Gaket)
 *         06.08.17
 */
public class PermissionsRepository {

    private RxPermissions rxPermissions;

    public PermissionsRepository(RxPermissions rxPermissions) {
        this.rxPermissions = rxPermissions;
    }

    public Observable<Boolean> requestLocationPermissions() {
        return rxPermissions.request(android.Manifest.permission.ACCESS_COARSE_LOCATION);
    }
}
