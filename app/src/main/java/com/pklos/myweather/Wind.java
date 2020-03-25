package com.pklos.myweather;

import com.google.gson.annotations.SerializedName;

public class Wind {
    @SerializedName("speed")
    public double windSpeed;
    @SerializedName("deg")
    public int windDegree;
}
