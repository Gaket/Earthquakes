package ru.inno.earthquakes.di.earthquakes;

import dagger.Subcomponent;
import ru.inno.earthquakes.presentation.alertscreen.AlertController;
import ru.inno.earthquakes.presentation.earthquakeslist.EarthquakesListController;
import ru.inno.earthquakes.presentation.settings.SettingsController;

/**
 * @author Artur Badretdinov (Gaket)
 *         22.07.17.
 */
@Subcomponent(modules = {EarthquakesModule.class, LocationModule.class})
@EarthquakesScope
public interface EarthquakesComponent {

    void inject(AlertController alertController);

    void inject(EarthquakesListController earthquakesListController);
}
