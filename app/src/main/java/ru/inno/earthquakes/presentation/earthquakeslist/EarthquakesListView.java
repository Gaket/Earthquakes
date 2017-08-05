package ru.inno.earthquakes.presentation.earthquakeslist;

import com.arellomobile.mvp.MvpView;

import java.util.List;

import ru.inno.earthquakes.entities.EarthquakeWithDist;

/**
 * @author Artur Badretdinov (Gaket)
 *         01.08.17
 */
public interface EarthquakesListView extends MvpView {

    void showEarthquakes(List<EarthquakeWithDist> earthquakeWithDists);

    // This method better should be in a router. It is here for simplicity now.
    void navigateToEarthquakesList();

    void showNetworkError();

    void hideNetworkError();

    void showLoading();

    void hideLoading();
}
