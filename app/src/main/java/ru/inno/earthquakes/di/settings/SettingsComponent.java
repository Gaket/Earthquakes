package ru.inno.earthquakes.di.settings;

import dagger.Subcomponent;
import ru.inno.earthquakes.presentation.settings.SettingsPresenter;

/**
 * @author Artur Badretdinov (Gaket)
 *         22.07.17.
 */
@Subcomponent(modules = {SettingsModule.class})
@SettingsScope
public interface SettingsComponent {

    void inject(SettingsPresenter settingsPresenter);
}
