package com.pklos.myweather.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.pklos.myweather.R;
import com.pklos.myweather.activities.MainActivity;
import com.pklos.myweather.utils.TimeStampConverter;

public class HomeFragment extends Fragment {
    TextView city;
    TextView temperature;
    TextView feelsLike;
    TextView time;
    TextView advDescription;
    ImageView weatherIconImg;

    String cityName;
    int temp;
    int feelTemp;
    long timeStamp;
    String timeStampConverted = "";
    String advDesc = "";
    String icon = "";


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
        advDescription = (TextView)view.findViewById(R.id.adv_description);
        weatherIconImg = (ImageView)view.findViewById(R.id.weatherIconImg);

        temp = (int)(Math.round(MainActivity.getTemp() - 273.15));
        temperature.setText(String.valueOf(temp));

        cityName = MainActivity.getCityName();
        city.setText(cityName);

        feelTemp = (int)(Math.round(MainActivity.getFeelsLike() - 273.15));
        feelsLike.setText(String.valueOf(feelTemp));

        timeStamp = MainActivity.getTimeFrom();
        timeStampConverted = TimeStampConverter.ConvertTimeStampToDate(timeStamp);
        time.setText(timeStampConverted);

        icon = MainActivity.getWeatherIcon();
        StringBuilder weatherIconURL = new StringBuilder("http://openweathermap.org/img/wn/")
                .append(icon)
                .append("@4x.png");
        //weatherIconImg.set


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