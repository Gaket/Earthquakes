package ru.inno.earthquakes.entity;

/**
 * @author Artur Badretdinov (Gaket)
 *         20.07.17.
 */
public class Location {

    private String name;
    private Coordinates coords;

    public static double distance(Coordinates from, Location to) {
        return distance(from, to.getCoords());
    }

    public static double distance(Coordinates from, Coordinates to) {
        return Math.hypot(
                from.getLatitude() - to.getLatitude(),
                from.getLongtitude() - to.getLongtitude());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoords() {
        return coords;
    }

    public void setCoords(Coordinates coords) {
        this.coords = coords;
    }

    public static class Coordinates {

        private double longtitude;
        private double latitude;

        public Coordinates(double longtitude, double latitude) {
            this.longtitude = longtitude;
            this.latitude = latitude;
        }

        public double getLongtitude() {
            return longtitude;
        }

        public void setLongtitude(double longtitude) {
            this.longtitude = longtitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }
    }
}
