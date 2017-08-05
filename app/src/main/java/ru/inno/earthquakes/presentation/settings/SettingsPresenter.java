package ru.inno.earthquakes.presentation.settings;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import ru.inno.earthquakes.di.application.AppComponent;

/**
 * @author Artur Badretdinov (Gaket)
 *         05.08.17
 */
@InjectViewState
public class SettingsPresenter extends MvpPresenter<SettingsView> {
    public SettingsPresenter(AppComponent appComponent) {
        appComponent.inject(this);
    }
}
