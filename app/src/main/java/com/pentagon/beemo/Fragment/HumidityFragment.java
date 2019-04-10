package com.pentagon.beemo.Fragment;


import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.pentagon.beemo.R;
import com.pentagon.beemo.ReportViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class HumidityFragment extends Fragment {

    private ReportViewModel mReportViewModel;

    public HumidityFragment() {
        // Required empty public constructor
    }

    public static HumidityFragment newInstance() {
        HumidityFragment fragment = new HumidityFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReportViewModel = ViewModelProviders.of(getActivity()).get(ReportViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_humidity, container, false);
        GraphView graph = (GraphView) view.findViewById(R.id.graph_humidity);
        GraphView graph2 = (GraphView) view.findViewById(R.id.graph_humidity2);

        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph.getLegendRenderer().setTextSize(28);

        graph2.getViewport().setScalable(true);
        graph2.getViewport().setScalableY(true);
        graph2.getLegendRenderer().setVisible(true);
        graph2.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph2.getLegendRenderer().setTextSize(28);

        TextView txtViewHumidHive1 = view.findViewById(R.id.txtViewHumidHive1);
        TextView txtViewHumidHive2 = view.findViewById(R.id.txtViewHumidHive2);
        TextView txtViewHumidHive3 = view.findViewById(R.id.txtViewHumidHive3);

        TextView txtViewHumid2Hive1 = view.findViewById(R.id.txtViewHumid2Hive1);
        TextView txtViewHumid2Hive2 = view.findViewById(R.id.txtViewHumid2Hive2);
        TextView txtViewHumid2Hive3 = view.findViewById(R.id.txtViewHumid2Hive3);

        TextView txtViewHumidOutside = view.findViewById(R.id.txtViewHumidOutside);

        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<DataPoint>();
        LineGraphSeries<DataPoint> series3 = new LineGraphSeries<DataPoint>();
        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<DataPoint>();

        LineGraphSeries<DataPoint> series2_1 = new LineGraphSeries<DataPoint>();
        LineGraphSeries<DataPoint> series2_2 = new LineGraphSeries<DataPoint>();
        LineGraphSeries<DataPoint> series2_3 = new LineGraphSeries<DataPoint>();
        LineGraphSeries<DataPoint> series2_4 = new LineGraphSeries<DataPoint>();


        if(mReportViewModel.reportModel != null) {
            double[] humid = mReportViewModel.getHumids();
            double[] humid2 = mReportViewModel.getHumids2();
            txtViewHumidHive1.setText(String.valueOf(humid[0]) + "%");
            txtViewHumidHive2.setText(String.valueOf(humid[1]) + "%");
            txtViewHumidHive3.setText(String.valueOf(humid[2]) + "%");

            txtViewHumid2Hive1.setText(String.valueOf(humid2[0]) + "%");
            txtViewHumid2Hive2.setText(String.valueOf(humid2[1]) + "%");
            txtViewHumid2Hive3.setText(String.valueOf(humid2[2]) + "%");

            txtViewHumidOutside.setText(String.valueOf(mReportViewModel.getHumidOutside()) + "%");

            series = mReportViewModel.getSeriesHumidity().get(0);
            series2 = mReportViewModel.getSeriesHumidity().get(1);
            series3 = mReportViewModel.getSeriesHumidity().get(2);
            series4 = mReportViewModel.getSeriesHumidity().get(3);

            series2_1 = mReportViewModel.getSeriesHumidity2().get(0);
            series2_2 = mReportViewModel.getSeriesHumidity2().get(1);
            series2_3 = mReportViewModel.getSeriesHumidity2().get(2);
            series2_4 = mReportViewModel.getSeriesHumidity2().get(3);
        }

        series.setDrawDataPoints(true);
        series.setDataPointsRadius(5);
        series2.setDrawDataPoints(true);
        series2.setDataPointsRadius(5);
        series3.setDrawDataPoints(true);
        series3.setDataPointsRadius(5);
        series4.setDrawDataPoints(true);
        series4.setDataPointsRadius(5);
        series.setColor(Color.RED);
        series.setTitle("HIVE 1");
        series2.setColor(Color.BLUE);
        series2.setTitle("HIVE 2");
        series3.setColor(Color.GREEN);
        series3.setTitle("HIVE 3");
        series4.setColor(Color.YELLOW);
        series4.setTitle("OUTSIDE");

        series2_1.setDrawDataPoints(true);
        series2_1.setDataPointsRadius(5);
        series2_2.setDrawDataPoints(true);
        series2_2.setDataPointsRadius(5);
        series2_3.setDrawDataPoints(true);
        series2_3.setDataPointsRadius(5);
        series2_4.setDrawDataPoints(true);
        series2_4.setDataPointsRadius(5);
        series2_1.setColor(Color.RED);
        series2_1.setTitle("HIVE 1");
        series2_2.setColor(Color.BLUE);
        series2_2.setTitle("HIVE 2");
        series2_3.setColor(Color.GREEN);
        series2_3.setTitle("HIVE 3");
        series2_4.setColor(Color.YELLOW);
        series2_4.setTitle("OUTSIDE");

        graph.addSeries(series);
        graph.addSeries(series2);
        graph.addSeries(series3);
        graph.addSeries(series4);

        graph2.addSeries(series2_1);
        graph2.addSeries(series2_2);
        graph2.addSeries(series2_3);
        graph2.addSeries(series2_4);
        return view;
    }

}
