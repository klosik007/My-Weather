package com.pklos.myweather;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class ForecastFragment extends Fragment {
    private List<Long> dts;
    private List<Double> temps;
    private List<Double> precisRain;
    private List<Double> precisSnow;

    private CombinedChart chart;
    private final int dataPeriod = 40;//every 3 hours, total amount 120 hours, 5 days

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forecast, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout)view.findViewById(R.id.layout);
        dts = MainActivity.getDtList();
        temps = MainActivity.getTempsList();

        //CHART
        chart = (CombinedChart)view.findViewById(R.id.forecastChart);
        chart.getDescription().setText("Forecast");
        chart.setBackgroundColor(Color.WHITE);
        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(false);
        chart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR,
//                CombinedChart.DrawOrder.BUBBLE,
//                CombinedChart.DrawOrder.CANDLE,
                CombinedChart.DrawOrder.LINE,
//                CombinedChart.DrawOrder.SCATTER
        });

        Legend legend = chart.getLegend();
        legend.setWordWrapEnabled(true);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(0.5f);
//        xAxis.setValueFormatter(new ValueFormatter() {
//            @Override
//            public String getFormattedValue(float value) {
//                return super.getFormattedValue(value);
//            }
//        });

        CombinedData data = new CombinedData();
        data.setData(precipitationData());//BarData
        data.setData(tempsData());//LineData

        xAxis.setAxisMaximum(data.getXMax() + 0.2f);

        chart.setData(data);
        chart.invalidate();

        //TABLE
        int tableRowNumber = dts.size();
        int tableColumnNumber = 1;

        TableLayout table = new TableLayout(getContext());
        TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        TableRow.LayoutParams tableRowsParams = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        tableParams.setMargins(10, 350, 5,5);
        table.setStretchAllColumns(true);
        TimeStampConverter.setPattern("dd.MM HH:ss");

        for(int tableRowId = 0; tableRowId < tableRowNumber; tableRowId++) {
            TableRow tableRow = new TableRow(getContext());

            for(int tableColId = 0; tableColId < tableColumnNumber; tableColId++) {
                TextView date = new TextView(getContext());
                date.setText(TimeStampConverter.ConvertTimeStampToDate(dts.get(tableRowId)));
                date.setTextColor(Color.BLACK);          // part2
                date.setPadding(5, 5, 5, 5);
                date.setGravity(11); //11 - center
                date.setTextAppearance(android.R.style.TextAppearance_Large);
                tableRow.addView(date, tableRowsParams);// add the column to the table row here

                TextView temp = new TextView(getContext());    // part3
                int tempVal =  (int)(Math.round(temps.get(tableRowId) - 273.15));
                temp.setText(String.valueOf(tempVal)); // set the text for the header
                temp.setTextColor(Color.BLACK); // set the color
                temp.setPadding(5, 5, 5, 5); // set the padding (if required)
                temp.setGravity(11); //11 - center
                temp.setTextAppearance(android.R.style.TextAppearance_Large);
                tableRow.addView(temp, tableRowsParams); // add the column to the table row here

                TextView precip = new TextView(getContext());    // part3
                precip.setText("0.0"); // set the text for the header
                precip.setTextColor(Color.BLACK); // set the color
                precip.setPadding(5, 5, 5, 5); // set the padding (if required)
                precip.setGravity(11); //11 - center
                precip.setTextAppearance(android.R.style.TextAppearance_Large);
                tableRow.addView(precip, tableRowsParams); // add the column to the table row here
            }
            table.addView(tableRow, tableParams);
        }
        table.setLayoutParams(tableParams);
        layout.addView(table);
    }

    private BarData precipitationData(){
        BarData preps = new BarData();

        return preps;
    }

    private LineData tempsData(){
        LineData temps = new LineData();

        return temps;
    }
}
