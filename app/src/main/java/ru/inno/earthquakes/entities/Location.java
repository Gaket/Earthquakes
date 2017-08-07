package ru.inno.earthquakes.entities;

/**
 * @author Artur Badretdinov (Gaket)
 *         20.07.17.
 */
public class Location {

    private String name;
    private Coordinates coords;

    static double distance(Coordinates from, Coordinates to) {
        return distFrom(from.getLatitude(), from.getLongtitude(), to.getLatitude(), to.getLongtitude());
    }

    private static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6_371; //kilometers
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
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

    public void setCoords(double[] coordinates) {
        this.coords = new Coordinates(coordinates[0], coordinates[1]);
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Coordinates that = (Coordinates) o;

            return Double.compare(that.longtitude, longtitude) == 0
                    && Double.compare(that.latitude, latitude) == 0;
        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            temp = Double.doubleToLongBits(longtitude);
            result = (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(latitude);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            return result;
        }
    }
}
