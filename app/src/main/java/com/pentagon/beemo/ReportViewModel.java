package com.pentagon.beemo;

import android.arch.lifecycle.ViewModel;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.pentagon.beemo.Model.ContactNumberModel;
import com.pentagon.beemo.Model.ReportModel;
import com.pentagon.beemo.Model.SettingsModel;

import java.util.ArrayList;

public class ReportViewModel extends ViewModel {
    public ReportModel reportModel;
    public SettingsModel settingsModel;
    public ContactNumberModel contactNumberModel;

    private LineGraphSeries seriesWeight1 = new LineGraphSeries();
    private LineGraphSeries seriesWeight2 = new LineGraphSeries();
    private LineGraphSeries seriesWeight3 = new LineGraphSeries();
    private double[] weights = new double[3];

    private LineGraphSeries<DataPoint> seriesHumidHive1 = new LineGraphSeries();
    private LineGraphSeries<DataPoint> seriesHumidHive2 = new LineGraphSeries();
    private LineGraphSeries<DataPoint> seriesHumidHive3 = new LineGraphSeries();
    private double[] humids = new double[4];
    private LineGraphSeries<DataPoint> seriesHumid2Hive1 = new LineGraphSeries();
    private LineGraphSeries<DataPoint> seriesHumid2Hive2 = new LineGraphSeries();
    private LineGraphSeries<DataPoint> seriesHumid2Hive3 = new LineGraphSeries();
    private double[] humids2 = new double[4];
    private LineGraphSeries<DataPoint> seriesHumidOutside = new LineGraphSeries();
    private double humidOutside;

    private LineGraphSeries<DataPoint> seriesTempHive1 = new LineGraphSeries();
    private LineGraphSeries<DataPoint> seriesTempHive2 = new LineGraphSeries();
    private LineGraphSeries<DataPoint> seriesTempHive3 = new LineGraphSeries();
    private double[] temps = new double[4];
    private LineGraphSeries<DataPoint> seriesTemp2Hive1 = new LineGraphSeries();
    private LineGraphSeries<DataPoint> seriesTemp2Hive2 = new LineGraphSeries();
    private LineGraphSeries<DataPoint> seriesTemp2Hive3 = new LineGraphSeries();
    private double[] temps2 = new double[4];
    private LineGraphSeries<DataPoint> seriesTempOutside = new LineGraphSeries();
    private double tempOutside;

    private double graphWeightLastXValue = 1d;

    public void setWeightGraph(){
        double weight = reportModel.getHive1().getWeight();
        weights[0] = weight;
        seriesWeight1.appendData(new DataPoint(graphWeightLastXValue, weight), true, 30);

        weight = reportModel.getHive2().getWeight();
        weights[1] = weight;
        seriesWeight2.appendData(new DataPoint(graphWeightLastXValue, weight), true, 30);

        weight = reportModel.getHive3().getWeight();
        weights[2] = weight;
        seriesWeight3.appendData(new DataPoint(graphWeightLastXValue, weight), true, 30);

    }

    public void setHumidityGraph() {
        double humid = reportModel.getHive1().getHumidities().get(0);
        double humid2 =reportModel.getHive1().getHumidities().get(1);
        humidOutside = reportModel.getOutside().getHumidity();
        humids[0] = humid;
        humids2[0] = humid2;
        seriesHumidHive1.appendData(new DataPoint(graphWeightLastXValue, humid), true, 100);
        seriesHumid2Hive1.appendData(new DataPoint(graphWeightLastXValue, humid2), true, 100);

        humid = reportModel.getHive2().getHumidities().get(0);
        humid2 = reportModel.getHive2().getHumidities().get(1);
        humids[1] = humid;
        humids2[1] = humid2;
        seriesHumidHive2.appendData(new DataPoint(graphWeightLastXValue, humid), true, 100);
        seriesHumid2Hive2.appendData(new DataPoint(graphWeightLastXValue, humid2), true, 100);

        humid = reportModel.getHive3().getHumidities().get(0);
        humid2 = reportModel.getHive3().getHumidities().get(1);
        humids[2] = humid;
        humids2[2] = humid2;
        seriesHumidHive3.appendData(new DataPoint(graphWeightLastXValue, humid), true, 100);
        seriesHumid2Hive3.appendData(new DataPoint(graphWeightLastXValue, humid2), true, 100);

        seriesHumidOutside.appendData(new DataPoint(graphWeightLastXValue, humidOutside), true, 100);

    }

    public void setTemperatureGraph() {
        double temp = reportModel.getHive1().getTemperatures().get(0);
        double temp2 = reportModel.getHive1().getTemperatures().get(1);
        tempOutside = reportModel.getOutside().getTemperature();
        temps[0] = temp;
        temps2[0] = temp2;
        seriesTempHive1.appendData(new DataPoint(graphWeightLastXValue, temp), true, 100);
        seriesTemp2Hive1.appendData(new DataPoint(graphWeightLastXValue, temp2), true, 100);

        temp = reportModel.getHive2().getTemperatures().get(0);
        temp2 = reportModel.getHive2().getTemperatures().get(1);
        temps[1] = temp;
        temps2[1] = temp2;
        seriesTempHive2.appendData(new DataPoint(graphWeightLastXValue, temp), true, 100);
        seriesTemp2Hive2.appendData(new DataPoint(graphWeightLastXValue, temp2), true, 100);

        temp = reportModel.getHive3().getTemperatures().get(0);
        temp2 = reportModel.getHive3().getTemperatures().get(1);
        temps[2] = temp;
        temps2[2] = temp2;
        seriesTempHive3.appendData(new DataPoint(graphWeightLastXValue, temp), true, 100);
        seriesTemp2Hive3.appendData(new DataPoint(graphWeightLastXValue, temp2), true, 100);

        seriesTempOutside.appendData(new DataPoint(graphWeightLastXValue, tempOutside), true, 100);
        graphWeightLastXValue += 1d;
    }

    public ArrayList<LineGraphSeries<DataPoint>> getSeriesTemperature() {
        ArrayList<LineGraphSeries<DataPoint>> temps = new ArrayList<>();
        temps.add(seriesTempHive1);
        temps.add(seriesTempHive2);
        temps.add(seriesTempHive3);
        temps.add(seriesTempOutside);

        return temps;
    }

    public ArrayList<LineGraphSeries<DataPoint>> getSeriesTemperature2() {
        ArrayList<LineGraphSeries<DataPoint>> temps = new ArrayList<>();
        temps.add(seriesTemp2Hive1);
        temps.add(seriesTemp2Hive2);
        temps.add(seriesTemp2Hive3);
        temps.add(seriesTempOutside);

        return temps;
    }

    public ArrayList<LineGraphSeries<DataPoint>> getSeriesHumidity() {
        ArrayList<LineGraphSeries<DataPoint>> humidities = new ArrayList<>();
        humidities.add(seriesHumidHive1);
        humidities.add(seriesHumidHive2);
        humidities.add(seriesHumidHive3);
        humidities.add(seriesHumidOutside);

        return humidities;
    }

    public ArrayList<LineGraphSeries<DataPoint>> getSeriesHumidity2() {
        ArrayList<LineGraphSeries<DataPoint>> humidities2 = new ArrayList<>();
        humidities2.add(seriesHumid2Hive1);
        humidities2.add(seriesHumid2Hive2);
        humidities2.add(seriesHumid2Hive3);
        humidities2.add(seriesHumidOutside);

        return humidities2;
    }

    public double[] getWeights() {
        return weights;
    }

    public double[] getHumids() {
        return humids;
    }

    public double[] getHumids2() {
        return humids2;
    }

    public double[] getTemps() {
        return temps;
    }

    public double[] getTemps2() {
        return temps2;
    }

    public ArrayList<LineGraphSeries<DataPoint>> getSeriesWeights() {
        ArrayList<LineGraphSeries<DataPoint>> weights = new ArrayList<>();
        weights.add(seriesWeight1);
        weights.add(seriesWeight2);
        weights.add(seriesWeight3);

        return weights;
    }

    public Double getHumidOutside() {
        return humidOutside;
    }

    public Double getTempOutside() {
        return tempOutside;
    }
}
