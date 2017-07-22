package ru.inno.earthquakes.model.earthquakes.data.rawmodels;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import ru.inno.earthquakes.entity.Earthquake;


/**
 * @author Artur Badretdinov (Gaket)
 *         20.07.17.
 */
public class EarthquakeDataModel {

    private Property properties;

    @SerializedName("geometry")
    private Geometry geometry;

    public Property getProperties() {
        return properties;
    }

    public void setProperties(Property properties) {
        this.properties = properties;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public class Property {
        private double mag;
        private String place;
        @SerializedName("alert")
        private Earthquake.AlertLevel alert;
        private Date time;
        private String url;

        public double getMag() {
            return mag;
        }

        public void setMag(double mag) {
            this.mag = mag;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public Earthquake.AlertLevel getAlert() {
            return alert;
        }

        public void setAlert(Earthquake.AlertLevel alert) {
            this.alert = alert;
        }

        public Date getTime() {
            return time;
        }

        public void setTime(Date time) {
            this.time = time;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class Geometry {

        @SerializedName("coordinates")
        private double[] coordinates;

        public double[] getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(double[] coordinates) {
            this.coordinates = coordinates;
        }
    }
}
