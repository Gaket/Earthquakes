package ru.inno.earthquakes.entities;

import java.util.Date;

/**
 * @author Artur Badretdinov (Gaket)
 *         22.07.17
 */
public class EarthquakeWithDist {

    private EarthquakeEntity earthquakeEntity;
    private double distance;
    private Location.Coordinates currentPosition;

    public EarthquakeWithDist(EarthquakeEntity earthquakeEntity, Location.Coordinates currentPosition) {
        this.earthquakeEntity = earthquakeEntity;
        this.currentPosition = currentPosition;
        updateDistance();
    }

    public EarthquakeEntity getEarthquakeEntity() {
        return earthquakeEntity;
    }

    public void setEarthquakeEntity(EarthquakeEntity earthquakeEntity) {
        this.earthquakeEntity = earthquakeEntity;
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
        return earthquakeEntity.getTitle();
    }

    public void setTitle(String title) {
        earthquakeEntity.setTitle(title);
    }

    public Double getMagnitude() {
        return earthquakeEntity.getMagnitude();
    }

    public void setMagnitude(Double magnitude) {
        earthquakeEntity.setMagnitude(magnitude);
    }

    public Date getTime() {
        return earthquakeEntity.getTime();
    }

    public void setTime(Date time) {
        earthquakeEntity.setTime(time);
    }

    public Location getLocation() {
        return earthquakeEntity.getLocation();
    }

    public void setLocation(Location location) {
        earthquakeEntity.setLocation(location);
    }

    public String getDetailsUrl() {
        return earthquakeEntity.getDetailsUrl();
    }

    public void setDetailsUrl(String url) {
        earthquakeEntity.setDetailsUrl(url);
    }

    private void updateDistance() {
        distance = Location.distance(currentPosition, earthquakeEntity.getLocation().getCoords());
    }
}
