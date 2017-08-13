package ru.inno.earthquakes.presentation.alertscreen;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import ru.inno.earthquakes.entities.EarthquakeWithDist;

/**
 * @author Artur Badretdinov (Gaket) 22.07.17
 */
@StateStrategyType(AddToEndSingleStrategy.class)
public interface AlertView extends MvpView {

  void showThereAreNoAlerts();

  void showEarthquakeAlert(EarthquakeWithDist earthquake);

  void showLoading(boolean show);

  @StateStrategyType(OneExecutionStateStrategy.class)
  void showNetworkError(boolean show);

  @StateStrategyType(OneExecutionStateStrategy.class)
  void showPermissionDeniedAlert();

  @StateStrategyType(OneExecutionStateStrategy.class)
  void showNoDataAlert();

  @StateStrategyType(OneExecutionStateStrategy.class)
  void navigateToEarthquakesList();

  @StateStrategyType(OneExecutionStateStrategy.class)
  void navigateToSettings();

  @StateStrategyType(OneExecutionStateStrategy.class)
  void showGoogleApiMessage(int status);
}
