package ru.inno.earthquakes.presentation.newsscreen;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import java.util.List;
import ru.inno.earthquakes.models.entities.EarthquakeWithDist;

/**
 * @author Artur Badretdinov (Gaket) 01.08.17
 */
@StateStrategyType(AddToEndSingleStrategy.class)
interface NewsView extends MvpView {

}
