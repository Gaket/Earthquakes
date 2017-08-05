package ru.inno.earthquakes.presentation.alertscreen;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.inno.earthquakes.entities.EarthquakeWithDist;

/**
 * @author Artur Badretdinov (Gaket)
 *         22.07.17
 */
@StateStrategyType(AddToEndSingleStrategy.class)
public interface AlertView extends MvpView{

    void showThereAreNoAlerts();

    void showEartquakeAlert(EarthquakeWithDist earthquake);

    // This method better should be in a router. It is here for simplicity now.
    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToEarthquakesList();

    void showNetworkError(boolean show);

    void showLoading(boolean show);
}
