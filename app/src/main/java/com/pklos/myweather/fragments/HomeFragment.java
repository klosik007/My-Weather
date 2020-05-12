package com.pklos.myweather.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pklos.myweather.R;
import com.pklos.myweather.activities.MainActivity;
import com.pklos.myweather.utils.TimeStampConverter;

public class HomeFragment extends Fragment {
    TextView city;
    TextView temperature;
    TextView feelsLike;
    TextView time;
    TextView mainDescription;
    TextView advDescription;

    String cityName;
    int temp;
    int feelTemp;
    long timeStamp;
    String timeStampConverted = "";
    String mainDesc = "";
    String advDesc = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        city = (TextView)view.findViewById(R.id.city_name);
        temperature = (TextView)view.findViewById(R.id.current_temp);
        feelsLike = (TextView)view.findViewById(R.id.feels_like);
        time = (TextView)view.findViewById(R.id.time_stamp);
        //mainDescription = (TextView)view.findViewById(R.id.main_description);
        advDescription = (TextView)view.findViewById(R.id.adv_description);

//        mainParameters = MainActivity.getMainParameters();
//        Log.d("mainParams", mainParameters.getCityName());
        temp = (int)(Math.round(MainActivity.getTemp() - 273.15));
        temperature.setText(String.valueOf(temp));

        cityName = MainActivity.getCityName();
        city.setText(cityName);

        feelTemp = (int)(Math.round(MainActivity.getFeelsLike() - 273.15));
        feelsLike.setText(String.valueOf(feelTemp));

        timeStamp = MainActivity.getTimeFrom();
        timeStampConverted = TimeStampConverter.ConvertTimeStampToDate(timeStamp);
        time.setText(timeStampConverted);

//        mainDesc = MainActivity.getMainDescription();
//        mainDescription.setText(mainDesc);

        advDesc = MainActivity.getWindAdvDescription();
        advDescription.setText(advDesc);
//
//        try {
//            cityName = mainParameters.cityName;
//            temp = mainParameters.temperature - 273.15;
//            feelTemp = mainParameters.feels_like_temp - 273.15;
//            //timeStamp = mainParameters.get("")
//            city.setText(cityName);
//            temperature.setText(String.valueOf(temp));
//            feelsLike.setText(String.valueOf(feelTemp));
//        }catch (NullPointerException e){
//            e.printStackTrace();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
    }
}