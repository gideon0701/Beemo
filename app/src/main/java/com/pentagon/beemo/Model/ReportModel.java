
package com.pentagon.beemo.Model;


import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class ReportModel {

    @SerializedName("Hive_1")
    private Hive1 mHive1;
    @SerializedName("Hive_2")
    private Hive2 mHive2;
    @SerializedName("Hive_3")
    private Hive3 mHive3;
    @SerializedName("Outside")
    private Outside mOutside;
    @SerializedName("Timestamp")
    private String mTimestamp;

    public Hive1 getHive1() {
        return mHive1;
    }

    public void setHive1(Hive1 hive1) {
        mHive1 = hive1;
    }

    public Hive2 getHive2() {
        return mHive2;
    }

    public void setHive2(Hive2 hive2) {
        mHive2 = hive2;
    }

    public Hive3 getHive3() {
        return mHive3;
    }

    public void setHive3(Hive3 hive3) {
        mHive3 = hive3;
    }

    public Outside getOutside() {
        return mOutside;
    }

    public void setOutside(Outside outside) {
        mOutside = outside;
    }

    public String getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(String timestamp) {
        mTimestamp = timestamp;
    }

}
