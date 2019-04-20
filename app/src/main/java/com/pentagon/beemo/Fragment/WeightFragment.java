package com.pentagon.beemo.Fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.pentagon.beemo.R;
import com.pentagon.beemo.ReportViewModel;

import java.text.SimpleDateFormat;

public class WeightFragment extends Fragment {

    private ReportViewModel mReportViewModel;

    public WeightFragment() {
        // Required empty public constructor
    }


    public static WeightFragment newInstance() {
        WeightFragment fragment = new WeightFragment();
        Bundle args = new Bundle();
//        args.putParcelable("REPORTCLASS", (Parcelable) reportModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReportViewModel = ViewModelProviders.of(getActivity()).get(ReportViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weight, container, false);

        GraphView graph = (GraphView) view.findViewById(R.id.graph_weight);
        TextView txtViewWeight1 = view.findViewById(R.id.txtViewWeightHive1);
        TextView txtViewWeight2 = view.findViewById(R.id.txtViewWeightHive2);
        TextView txtViewWeight3 = view.findViewById(R.id.txtViewWeightHive3);

        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);

        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<DataPoint>();
        LineGraphSeries<DataPoint> series3 = new LineGraphSeries<DataPoint>();

       if(mReportViewModel.reportModel != null) {
           double[] weights = mReportViewModel.getWeights();
           txtViewWeight1.setText(String.valueOf(weights[0]) + "kg");
           txtViewWeight2.setText(String.valueOf(weights[1]) + "kg");
           txtViewWeight3.setText(String.valueOf(weights[2]) + "kg");

           series = mReportViewModel.getSeriesWeights().get(0);

           series2 = mReportViewModel.getSeriesWeights().get(1);

           series3 = mReportViewModel.getSeriesWeights().get(2);

       }

        series.setDrawDataPoints(true);
        series.setDataPointsRadius(8);

        series2.setDrawDataPoints(true);
        series2.setDataPointsRadius(8);

        series3.setDrawDataPoints(true);
        series3.setDataPointsRadius(8);

        series.setColor(Color.RED);
        series.setTitle("HIVE 1");

        series2.setColor(Color.BLUE);
        series2.setTitle("HIVE 2");

        series3.setColor(Color.GREEN);
        series3.setTitle("HIVE 3");

        graph.addSeries(series);
        graph.addSeries(series2);
        graph.addSeries(series3);
        // Inflate the layout for this fragment
        return view;
    }

}
