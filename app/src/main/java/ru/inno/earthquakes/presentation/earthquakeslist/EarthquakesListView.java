package ru.inno.earthquakes.presentation.earthquakeslist;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import java.util.List;
import ru.inno.earthquakes.entities.EarthquakeWithDist;

/**
 * @author Artur Badretdinov (Gaket) 01.08.17
 */
@StateStrategyType(AddToEndSingleStrategy.class)
interface EarthquakesListView extends MvpView {

  void showEarthquakes(List<EarthquakeWithDist> earthquakeWithDists);

  void showNetworkError(boolean show);

  void showLoading(boolean show);

  @StateStrategyType(OneExecutionStateStrategy.class)
  void navigateToEarthquakeDetails(EarthquakeWithDist earthquakeWithDist);
}
