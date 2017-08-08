package ru.inno.earthquakes.di.settings;

import dagger.Subcomponent;
import ru.inno.earthquakes.presentation.settings.SettingsActivity;

/**
 * @author Artur Badretdinov (Gaket)
 *         22.07.17.
 */
@Subcomponent()
@SettingsScope
public interface SettingsComponent {

    void inject(SettingsActivity settingsPresenter);
}
