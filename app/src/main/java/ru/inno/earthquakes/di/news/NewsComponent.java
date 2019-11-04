package ru.inno.earthquakes.di.news;

import dagger.Subcomponent;
import ru.inno.earthquakes.presentation.alertscreen.AlertActivity;
import ru.inno.earthquakes.presentation.earthquakeslist.EarthquakesListActivity;
import ru.inno.earthquakes.presentation.newsscreen.NewsActivity;

/**
 * @author Artur Badretdinov (Gaket) 22.07.17.
 */
@Subcomponent(modules = {NewsModule.class})
@NewsScope
public interface NewsComponent {

  void inject(NewsActivity alertPresenter);

}
