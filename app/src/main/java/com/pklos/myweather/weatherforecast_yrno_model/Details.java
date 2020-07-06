package com.pklos.myweather.weatherforecast_yrno_model;

import com.google.gson.annotations.SerializedName;

public class Details {
    @SerializedName("air_pressure_at_sea_level")
    public double air_pressure;

    public double air_temperature;

    public double relative_humidity;

    public double wind_speed;

    public double wind_from_direction;
}
