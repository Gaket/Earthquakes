package ru.inno.earthquakes.entities;

import java.util.Date;

/**
 * {@link Earthquake} object that has knowledge about distance from user's position
 *
 * @author Artur Badretdinov (Gaket) 22.07.17
 */
public class EarthquakeWithDist {

  private Earthquake earthquake;
  private double distance;
  private Location.Coordinates currentPosition;

  public EarthquakeWithDist() {
  }

  public EarthquakeWithDist(Earthquake earthquake, Location.Coordinates currentPosition) {
    this.earthquake = earthquake;
    this.currentPosition = currentPosition;
    updateDistance();
  }

  public Earthquake getEarthquake() {
    return earthquake;
  }

  public void setEarthquake(Earthquake earthquake) {
    this.earthquake = earthquake;
    updateDistance();
  }

  public double getDistance() {
    return distance;
  }

  public Location.Coordinates getCurrentPosition() {
    return currentPosition;
  }

  public void setCurrentPosition(Location.Coordinates currentPosition) {
    this.currentPosition = currentPosition;
    updateDistance();
  }

  public String getTitle() {
    return earthquake.getTitle();
  }

  public void setTitle(String title) {
    earthquake.setTitle(title);
  }

  public Double getMagnitude() {
    return earthquake.getMagnitude();
  }

  public void setMagnitude(Double magnitude) {
    earthquake.setMagnitude(magnitude);
  }

  public Date getTime() {
    return earthquake.getTime();
  }

  public void setTime(Date time) {
    earthquake.setTime(time);
  }

  public Location getLocation() {
    return earthquake.getLocation();
  }

  public void setLocation(Location location) {
    earthquake.setLocation(location);
  }

  public String getDetailsUrl() {
    return earthquake.getDetailsUrl();
  }

  public void setDetailsUrl(String url) {
    earthquake.setDetailsUrl(url);
  }

  private void updateDistance() {
    distance = Location.distance(currentPosition, earthquake.getLocation().getCoords());
  }
}
