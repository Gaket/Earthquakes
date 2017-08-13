package ru.inno.earthquakes.model.models.network;

import com.google.gson.annotations.SerializedName;
import java.util.Date;


/**
 * @author Artur Badretdinov (Gaket) 20.07.17.
 */
public class EarthquakeNetwork {

  @SerializedName("properties")
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

  public static class Property {

    private String title;

    public String getTitle() {
      return title;
    }

    private double mag;
    private String place;
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

    private void setTitle(String title) {
      this.title = title;
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
