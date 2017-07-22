package ru.inno.earthquakes.entity;

/**
 * @author Artur Badretdinov (Gaket)
 *         22.07.17.
 */
public class EarthquakeWithDist {

    private Earthquake earthquake;
    private double distance;
    private Location.Coordinates currentPosition;

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

    private void updateDistance() {
        distance = Location.distance(currentPosition, earthquake.getLocation());
    }
}
