package ru.inno.earthquakes.model.models.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Artur Badretdinov (Gaket)
 *         20.07.17.
 */
public class EarthquakesResponse {

    @SerializedName("features")
    private List<EarthquakeNetwork> features;

    public List<EarthquakeNetwork> getFeatures() {
        return features;
    }

    public void setFeatures(List<EarthquakeNetwork> features) {
        this.features = features;
    }
}
