package ru.inno.earthquakes.entity;

import java.util.Date;

/**
 * @author Artur Badretdinov (Gaket)
 *         20.07.17.
 */
public class Earthquake {

    private double magnitude;
    private Date time;
    private AlertLevel alertLevel;
    private Location location;
    private String url;

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public AlertLevel getAlertLevel() {
        return alertLevel;
    }

    public void setAlertLevel(AlertLevel alertLevel) {
        this.alertLevel = alertLevel;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public enum AlertLevel {
        GREEN,
        YELLOW,
        ORANGE,
        RED,
        UNDEFINED
    }
}
