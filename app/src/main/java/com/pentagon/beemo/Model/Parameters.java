
package com.pentagon.beemo.Model;

import com.google.gson.annotations.SerializedName;

public class Parameters {

    @SerializedName("Harvest_Weight")
    private Double mHarvestWeight;
    @SerializedName("Max_Temp")
    private Double mMaxTemp;
    @SerializedName("Min_Temp")
    private Double mMinTemp;
    @SerializedName("Max_Temp2")
    private Double mMaxTemp2;
    @SerializedName("Min_Temp2")
    private Double mMinTemp2;
    @SerializedName("Notif_Status")
    private Boolean mNotifStatus;
    @SerializedName("Time_Span")
    private Integer mTimeSpan;
    @SerializedName("Max_Humid")
    private Double mMaxHumid;
    @SerializedName("Min_Humid")
    private Double mMinHumid;
    @SerializedName("Max_Humid2")
    private Double mMaxHumid2;
    @SerializedName("Min_Humid2")
    private Double mMinHumid2;

    public Double getHarvestWeight() {
        return mHarvestWeight;
    }

    public void setHarvestWeight(Double harvestWeight) {
        mHarvestWeight = harvestWeight;
    }

    public Double getMaxTemp() {
        return mMaxTemp;
    }

    public void setMaxTemp(Double maxTemp) {
        mMaxTemp = maxTemp;
    }

    public Double getMinTemp() {
        return mMinTemp;
    }

    public void setMinTemp(Double minTemp) {
        mMinTemp = minTemp;
    }

    public Boolean getNotifStatus() {
        return mNotifStatus;
    }

    public void setNotifStatus(Boolean notifStatus) {
        mNotifStatus = notifStatus;
    }

    public Integer getTimeSpan() {
        return mTimeSpan;
    }

    public void setTimeSpan(Integer timeSpan) {
        mTimeSpan = timeSpan;
    }

    public Double getMaxHumid() {
        return mMaxHumid;
    }

    public void setMaxHumid(Double maxHumid) {
        this.mMaxHumid = maxHumid;
    }

    public Double getMinHumid() {
        return mMinHumid;
    }

    public void setMinHumid(Double minHumid) {
        this.mMinHumid = minHumid;
    }

    public Double getMaxTemp2() {
        return mMaxTemp2;
    }

    public void setMaxTemp2(Double maxTemp2) {
        this.mMaxTemp2 = maxTemp2;
    }

    public Double getMinTemp2() {
        return mMinTemp2;
    }

    public void setMinTemp2(Double minTemp2) {
        this.mMinTemp2 = minTemp2;
    }

    public Double getMaxHumid2() {
        return mMaxHumid2;
    }

    public void setMaxHumid2(Double maxHumid2) {
        this.mMaxHumid2 = maxHumid2;
    }

    public Double getMinHumid2() {
        return mMinHumid2;
    }

    public void setMinHumid2(Double minHumid2) {
        this.mMinHumid2 = minHumid2;
    }
}
