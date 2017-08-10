package ru.inno.earthquakes.presentation.settings;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

/**
 * @author Artur Badretdinov (Gaket)
 *         05.08.17
 */
@StateStrategyType(AddToEndSingleStrategy.class)
public interface SettingsView extends MvpView {

    void setMaxDistance(Double dist);

    void setMinMagnitude(Double mag);

    void setDefaultCity(String name);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showNotImplementedError();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToInfo();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void close();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showDistanceFormatError();

}
