package ru.inno.earthquakes.presentation.settings;

import com.arellomobile.mvp.InjectViewState;
<<<<<<< HEAD
import com.arellomobile.mvp.MvpPresenter;
=======
>>>>>>> 141c0f1... Code cleaning

import ru.inno.earthquakes.model.settings.SettingsInteractor;
import ru.inno.earthquakes.presentation.common.BasePresenter;
import ru.inno.earthquakes.presentation.common.Utils;

/**
 * @author Artur Badretdinov (Gaket)
 *         05.08.17
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
    }

    /**
     * Save user input
     *
     * @param km
     * @param magnitude
     */
    void onSave(String km, double magnitude) {
        if (km.isEmpty() || !Utils.isDigitsOnly(km)) {
            getViewState().showDistanceFormatError();
            return;
        }
        interactor.saveAlertSettings(Integer.parseInt(km), magnitude);
        getViewState().close();
    }

    void onInfoAction() {
        getViewState().navigateToInfo();
    }
}
