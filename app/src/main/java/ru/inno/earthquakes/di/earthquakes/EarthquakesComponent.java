package ru.inno.earthquakes.di.earthquakes;

import dagger.Subcomponent;
import ru.inno.earthquakes.presentation.alertscreen.AlertActivity;
import ru.inno.earthquakes.presentation.earthquakeslist.EarthquakesListActivity;

/**
 * @author Artur Badretdinov (Gaket) 22.07.17.
 */
@Subcomponent(modules = {EarthquakesModule.class, LocationModule.class})
@EarthquakesScope
public interface EarthquakesComponent {

  void inject(AlertActivity alertPresenter);

  void inject(EarthquakesListActivity earthquakesListActivity);
}
