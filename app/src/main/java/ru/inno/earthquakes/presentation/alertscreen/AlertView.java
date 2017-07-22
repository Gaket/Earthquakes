package ru.inno.earthquakes.presentation.alertscreen;

import com.arellomobile.mvp.MvpView;

import ru.inno.earthquakes.entity.EarthquakeWithDist;

/**
 * @author Artur Badretdinov (Gaket)
 *         22.07.17.
 */
public interface AlertView extends MvpView{

    void showThereAreNoAlerts();

    void showEartqhakeAlert(EarthquakeWithDist earthquake);
}
