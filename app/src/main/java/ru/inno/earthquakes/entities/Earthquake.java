package ru.inno.earthquakes.entities;

import java.util.Date;

/**
 * @author Artur Badretdinov (Gaket)
 *         20.07.17.
 */
public class Earthquake {

    private String title;
    private Double magnitude;
    private Date time;
    private AlertLevel alertLevel;
    private Location location;
    private String detailsUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(Double magnitude) {
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

    public String getDetailsUrl() {
        return detailsUrl;
    }

    public void setDetailsUrl(String url) {
        this.detailsUrl = url;
    }

    public enum AlertLevel {
        GREEN,
        YELLOW,
        ORANGE,
        RED,
        UNDEFINED
    }
}
