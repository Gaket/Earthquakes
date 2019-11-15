package ru.inno.earthquakes.presentation.settings;

import androidx.annotation.StringRes;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

/**
 * @author Artur Badretdinov (Gaket) 05.08.17
 */
@StateStrategyType(AddToEndSingleStrategy.class)
public interface SettingsView extends MvpView {

  void setMaxDistance(Double dist);

  void setMinMagnitude(Double mag);

  void setDefaultCity(String name);

  void showNotImplementedError();

  void navigateToInfo();

  void close();

  void showDistanceFormatError();

  // ===
  // Methods for today's lesson
  // ===

  void showError(String errorMsg);

  void showError(SettingsMessage distanceError);

  void showError(@StringRes int resId);
}
