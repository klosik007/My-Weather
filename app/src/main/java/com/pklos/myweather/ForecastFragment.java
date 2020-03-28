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

import java.util.ArrayList;
import java.util.List;

public class ForecastFragment extends Fragment {
    List<Long> dts;
    List<Double> temps;
    List<Double> precisRain;
    List<Double> precisSnow;

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
        int tableRowNumber = dts.size();
        int tableColumnNumber = 1;

        TableLayout table = new TableLayout(getContext());
        TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        TableRow.LayoutParams tableRowsParams = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        tableParams.setMargins(10, 50, 5,5);
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
}
