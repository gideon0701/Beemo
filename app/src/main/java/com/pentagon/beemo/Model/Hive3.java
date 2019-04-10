
package com.pentagon.beemo.Model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Hive3 {

    @SerializedName("Humidities")
    private List<Double> mHumidities;
    @SerializedName("Temperatures")
    private List<Double> mTemperatures;
    @SerializedName("Weight")
    private Double mWeight;

    public List<Double> getHumidities() {
        return mHumidities;
    }

    public void setHumidities(List<Double> humidities) {
        mHumidities = humidities;
    }

    public List<Double> getTemperatures() {
        return mTemperatures;
    }

    public void setTemperatures(List<Double> temperatures) {
        mTemperatures = temperatures;
    }

    public Double getWeight() {
        return mWeight;
    }

    public void setWeight(Double weight) {
        mWeight = weight;
    }

}
