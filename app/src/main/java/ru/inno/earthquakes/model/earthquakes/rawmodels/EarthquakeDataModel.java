package ru.inno.earthquakes.model.earthquakes.rawmodels;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import ru.inno.earthquakes.entities.Earthquake;


/**
 * @author Artur Badretdinov (Gaket)
 *         20.07.17.
 */
public class EarthquakeDataModel {

    @SerializedName("properties")
    public Property properties;
    @SerializedName("geometry")
    public Geometry geometry;

    public class Property {
        public String title;
        public double mag;
        public String place;
        public Date time;
        public String url;
        @SerializedName("alert")
        public Earthquake.AlertLevel alert;
    }

    public static class Geometry {

        @SerializedName("coordinates")
        public double[] coordinates;
    }
}
