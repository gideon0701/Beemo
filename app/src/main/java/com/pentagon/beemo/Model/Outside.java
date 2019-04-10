
package com.pentagon.beemo.Model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Outside {

    @SerializedName("Humidity")
    private Double mHumidity;
    @SerializedName("Temperature")
    private Double mTemperature;

    public Double getHumidity() {
        return mHumidity;
    }

    public void setHumidity(Double humidity) {
        mHumidity = humidity;
    }

    public Double getTemperature() {
        return mTemperature;
    }

    public void setTemperature(Double temperature) {
        mTemperature = temperature;
    }

}
