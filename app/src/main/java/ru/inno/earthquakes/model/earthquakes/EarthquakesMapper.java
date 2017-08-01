package ru.inno.earthquakes.model.earthquakes;

import ru.inno.earthquakes.entities.Earthquake;
import ru.inno.earthquakes.entities.Location;
import ru.inno.earthquakes.model.earthquakes.rawmodels.EarthquakeDataModel;

/**
 * @author Artur Badretdinov (Gaket)
 *         20.07.17.
 */
public class EarthquakesMapper {

    public Earthquake map(EarthquakeDataModel dataModel) {
        Earthquake earthquake = new Earthquake();
        EarthquakeDataModel.Property properties = dataModel.properties;

        earthquake.setTitle(dataModel.properties.title);
        earthquake.setTime(properties.time);
        earthquake.setMagnitude(properties.mag);
        Earthquake.AlertLevel alert =
                properties.alert == null ? Earthquake.AlertLevel.UNDEFINED : properties.alert;
        earthquake.setAlertLevel(alert);
        earthquake.setDetailsUrl(properties.url);

        Location location = new Location();
        location.setName(properties.place);
        double[] coordinates = dataModel.geometry.coordinates;
        location.setCoords(new Location.Coordinates(coordinates[0], coordinates[1]));
        location.setName(properties.place);
        earthquake.setLocation(location);

        return earthquake;
    }
}
