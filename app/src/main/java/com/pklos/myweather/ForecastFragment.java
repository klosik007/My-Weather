package com.pklos.myweather;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class ForecastFragment extends Fragment {
    TextView daytime;
    TextView temp;
    TextView precipitation;

    List<Long> dts;
    List<Double> temps;
    List<Double> precisRain;
    List<Double> precisSnow;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forecast, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout)view.findViewById(R.id.layout);
        TableLayout forecastTable = (TableLayout)view.findViewById(R.id.forecastTable);
        dts = MainActivity.getDtList();
        temps = MainActivity.getTempsList();
        int tableColNum = 3;
        int tableRowNum = 3;

        TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        //forecastTable.setLayoutParams(tableParams);

        TableRow tblRow = new TableRow(getContext());
        tblRow.setLayoutParams(rowParams);

        TextView label_date = new TextView(getContext());
        label_date.setLayoutParams(rowParams);
        label_date.setText("HELLO");
        label_date.setTextColor(Color.WHITE);          // part2
        label_date.setPadding(5, 5, 5, 5);
        tblRow.addView(label_date);// add the column to the table row here

        TextView label_temp = new TextView(getContext());    // part3
                label_temp.setText("ANDROID..!!"); // set the text for the header
                label_temp.setTextColor(Color.WHITE); // set the color
                label_temp.setPadding(5, 5, 5, 5); // set the padding (if required)
                tblRow.addView(label_temp); // add the column to the table row here

                TextView label_precip = new TextView(getContext());    // part3
                label_precip.setText("PREC"); // set the text for the header
                label_precip.setTextColor(Color.WHITE); // set the color
                label_precip.setPadding(5, 5, 5, 5); // set the padding (if required)
                tblRow.addView(label_precip); // add the column to the table row here

        forecastTable.addView(tblRow, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,                    //part4
                TableLayout.LayoutParams.WRAP_CONTENT));

//        for(int tableRowId = 1; tableRowId < tableRowNum; tableRowId++)
//        {
//            TableRow tblRowVar = new TableRow(getContext());
//            tblRowVar.setId(tableRowId);
//            tblRowVar.setLayoutParams(new TableRow.LayoutParams(
//                    TableRow.LayoutParams.MATCH_PARENT,
//                    TableRow.LayoutParams.WRAP_CONTENT));
//            for(int tblColIdxVar = 0; tblColIdxVar < tableColNum; tblColIdxVar++)
//            {
//                TextView label_date = new TextView(getContext());
//                label_date.setText("HELLO");
//                label_date.setTextColor(Color.WHITE);          // part2
//                label_date.setPadding(5, 5, 5, 5);
//                tblRowVar.addView(label_date);// add the column to the table row here
//
//                TextView label_temp = new TextView(getContext());    // part3
//                label_temp.setText("ANDROID..!!"); // set the text for the header
//                label_temp.setTextColor(Color.WHITE); // set the color
//                label_temp.setPadding(5, 5, 5, 5); // set the padding (if required)
//                tblRowVar.addView(label_temp); // add the column to the table row here
//
//                TextView label_precip = new TextView(getContext());    // part3
//                label_precip.setText("PREC"); // set the text for the header
//                label_precip.setTextColor(Color.WHITE); // set the color
//                label_precip.setPadding(5, 5, 5, 5); // set the padding (if required)
//                tblRowVar.addView(label_precip); // add the column to the table row here
//            }
//            forecastTable.addView(tblRowVar, new TableLayout.LayoutParams(
//                    TableLayout.LayoutParams.WRAP_CONTENT,                    //part4
//                    TableLayout.LayoutParams.WRAP_CONTENT));
//        }
//        precisRain = MainActivity.getRainPrepsList();
//        precisSnow = MainActivity.getSnowPrepsList();
//        for (Long dt : dts){
//            Log.d("dt", String.valueOf(dt));
//        }

    }
}
