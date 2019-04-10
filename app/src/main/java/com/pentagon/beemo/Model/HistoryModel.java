package com.pentagon.beemo.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HistoryModel {
    @SerializedName("Reports")
    private List<ReportModel> mReports;

    public List<ReportModel> getReports() {
        return mReports;
    }

    public void setReports(List<ReportModel> reports) {
        mReports = reports;
    }
}
