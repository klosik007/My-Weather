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

public class MoreInfoFragment extends Fragment {
    TextView cityText;
    TextView sunriseText;
    TextView sunsetText;
    TextView pressureText;
    TextView humidityText;
    TextView windSpeedText;
    TextView windDegreeText;
    TextView visibilityText;
    TextView tempMaxText;
    TextView tempMinText;

    String cityName;
    long sunrise;
    long sunset;
    int pressure;
    int humidity;
    double windSpeed;
    int windDegree;
    int visibility;
    int tempMax;
    int tempMin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_moreinfo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cityText = (TextView) view.findViewById(R.id.city_name);
        sunriseText = (TextView) view.findViewById(R.id.sunrise);
        sunsetText = (TextView) view.findViewById(R.id.sunset);
        pressureText = (TextView) view.findViewById(R.id.pressure);
        humidityText = (TextView)view.findViewById(R.id.humidity);
        windSpeedText = (TextView) view.findViewById(R.id.windSpeed);
        windDegreeText = (TextView) view.findViewById(R.id.wind_deg);
        visibilityText = (TextView) view.findViewById(R.id.visibility);
        tempMaxText = (TextView) view.findViewById(R.id.tempMax);
        tempMinText = (TextView) view.findViewById(R.id.tempMin);
        TimeStampConverter.setPattern("HH:ss");

        cityName = MainActivity.getCityName();
        cityText.setText(cityName);

        sunrise = MainActivity.getSunrise();
        sunriseText.setText(TimeStampConverter.ConvertTimeStampToDate(sunrise));

        sunset = MainActivity.getSunset();
        sunsetText.setText(TimeStampConverter.ConvertTimeStampToDate(sunset));

        pressure = MainActivity.getPressure();
        StringBuilder pressureTextBuilder = new StringBuilder().append(getResources().getString(R.string.pressure)).append(": ").append(pressure).append(" hPa");
        pressureText.setText(pressureTextBuilder);

        humidity = MainActivity.getHumidity();
        StringBuilder humidityTextBuilder = new StringBuilder().append(getResources().getString(R.string.humidity)).append(": ").append(humidity).append("%");
        humidityText.setText(humidityTextBuilder);

        windSpeed = MainActivity.getWindSpeed();
        StringBuilder windSpeedTextBuilder = new StringBuilder().append(getResources().getString(R.string.wind_speed)).append(": ").append(windSpeed).append(" m/s");
        windSpeedText.setText(windSpeedTextBuilder);

        windDegree = MainActivity.getWindDegree();
        StringBuilder windDegreeTextBuilder = new StringBuilder().append(getResources().getString(R.string.wind_deg)).append(": ").append(windDegree);
        windDegreeText.setText(windDegreeTextBuilder);

        visibility = MainActivity.getVisibility();
        StringBuilder visibilityTextBuilder = new StringBuilder().append(visibility).append(" m");
        visibilityText.setText(visibilityTextBuilder);

        tempMax = (int)(Math.round(MainActivity.getTempMax() - 273.15));
        StringBuilder tempMaxTextBuilder = new StringBuilder().append(tempMax);
        tempMaxText.setText(tempMaxTextBuilder);

        tempMin = (int)(Math.round(MainActivity.getTempMin() - 273.15));
        StringBuilder tempMinTextBuilder = new StringBuilder().append(tempMin);
        tempMinText.setText(tempMinTextBuilder);
    }
}
