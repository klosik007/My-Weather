package com.pklos.myweather.weatherforecast_openweather_model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MainParameters {
    public Coord coord;

    public List<Weather> weather;

    public int visibility;

    public Main main;

    public Wind wind;

    @SerializedName("dt")
    public long daytime;

    public Sys sys;

    @SerializedName("name")
    public String cityName;
}