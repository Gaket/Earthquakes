package ru.inno.earthquakes.presentation.settings;

import android.content.Context;
import com.arellomobile.mvp.InjectViewState;
import ru.inno.earthquakes.R;
import ru.inno.earthquakes.business.settings.SettingsInteractor;
import ru.inno.earthquakes.presentation.common.BasePresenter;
import ru.inno.earthquakes.presentation.common.Utils;

/**
 * @author Artur Badretdinov (Gaket) 05.08.17
 */
@InjectViewState
public class SettingsPresenter extends BasePresenter<SettingsView> {

  private SettingsInteractor interactor;

  SettingsPresenter(SettingsInteractor settingsInteractor) {
    this.interactor = settingsInteractor;
  }

  @Override
  protected void onFirstViewAttach() {
    super.onFirstViewAttach();
    getViewState().setMaxDistance(interactor.getAlertMaxDistance());
    getViewState().setMinMagnitude(interactor.getAlertMinMagnitude());
    getViewState().setDefaultCity(interactor.getDefaultLocation().getName());
  }

  /**
   * Save user input
   */
  void onSave(String km, double magnitude) {
    if (km.isEmpty() || !Utils.isDigitsOnly(km)) {
      getViewState().showError("Enter integer number");
      return;
    }

    interactor.saveAlertSettings(Integer.parseInt(km), magnitude);
    getViewState().close();
  }

  void onInfoAction() {
    getViewState().navigateToInfo();
  }

  public void onChangeDefaultCity() {
    getViewState().showNotImplementedError();
  }
}
