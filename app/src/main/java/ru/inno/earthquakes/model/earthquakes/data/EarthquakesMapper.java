package ru.inno.earthquakes.model.earthquakes.data;

import ru.inno.earthquakes.entity.Earthquake;
import ru.inno.earthquakes.entity.Location;
import ru.inno.earthquakes.model.earthquakes.data.rawmodels.EarthquakeDataModel;

/**
 * @author Artur Badretdinov (Gaket)
 *         20.07.17.
 */
public class EarthquakesMapper {

    public Earthquake map(EarthquakeDataModel dataModel) {
        Earthquake earthquake = new Earthquake();
        EarthquakeDataModel.Property properties = dataModel.getProperties();

        earthquake.setTime(properties.getTime());
        earthquake.setMagnitude(properties.getMag());
        Earthquake.AlertLevel alert = properties.getAlert() == null
                ? Earthquake.AlertLevel.UNDEFINED : properties.getAlert();
        earthquake.setAlertLevel(alert);
        earthquake.setUrl(properties.getUrl());

        Location location = new Location();
        location.setName(properties.getPlace());
        double[] coordinates = dataModel.getGeometry().getCoordinates();
        location.setCoords(new Location.Coordinates(coordinates[0] ,coordinates[1]));
        location.setName(properties.getPlace());
        earthquake.setLocation(location);

        return earthquake;
    }
}
