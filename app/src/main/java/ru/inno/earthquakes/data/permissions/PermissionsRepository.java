package ru.inno.earthquakes.data.permissions;

import com.tbruyelle.rxpermissions2.RxPermissions;
import io.reactivex.Observable;

/**
 * Class helping to work with Android permissions in Rx style
 *
 * @author Artur Badretdinov (Gaket) 06.08.17
 */
public class PermissionsRepository {

  private RxPermissions rxPermissions;

  public PermissionsRepository(RxPermissions rxPermissions) {
    this.rxPermissions = rxPermissions;
  }

  /**
   * Checks if the location permissions are given
   *
   * @return true if permission granted
   */
  public Observable<Boolean> requestLocationPermissions() {
    return rxPermissions.request(android.Manifest.permission.ACCESS_COARSE_LOCATION);
  }
}
