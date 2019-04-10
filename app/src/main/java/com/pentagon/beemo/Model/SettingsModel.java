
package com.pentagon.beemo.Model;

import com.google.gson.annotations.SerializedName;

public class SettingsModel {

    @SerializedName("Parameters")
    private Parameters mParameters;

    public Parameters getParameters() {
        return mParameters;
    }

    public void setParameters(Parameters parameters) {
        mParameters = parameters;
    }

}
