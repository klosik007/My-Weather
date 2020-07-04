package com.pklos.myweather.weatherforecast_openweather_model;

import com.google.gson.annotations.SerializedName;

public class Wind {
    @SerializedName("speed")
    public double windSpeed;
    @SerializedName("deg")
    public int windDegree;
}
