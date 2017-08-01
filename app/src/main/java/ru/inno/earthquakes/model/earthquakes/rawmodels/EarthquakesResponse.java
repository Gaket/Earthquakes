package ru.inno.earthquakes.model.earthquakes.rawmodels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Artur Badretdinov (Gaket)
 *         20.07.17.
 */
public class EarthquakesResponse {

    @SerializedName("features")
    private List<EarthquakeDataModel> features;

    public List<EarthquakeDataModel> getFeatures() {
        return features;
    }

    public void setFeatures(List<EarthquakeDataModel> features) {
        this.features = features;
    }
}
