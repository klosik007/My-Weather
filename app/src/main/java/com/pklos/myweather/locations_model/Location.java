package com.pklos.myweather.locations_model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "locations")
public class Location {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name="cityName")
    private String cityName;

    @ColumnInfo(name="cityID")
    private String cityID;

    @ColumnInfo(name="isDefault")
    private boolean isDefault;//should be used only once per table

    public Location(int id, String cityName, String cityID, boolean isDefault){
        this.id = id;
        this.cityName = cityName;
        this.cityID = cityID;
        this.isDefault = isDefault;
    }

    @Ignore
    public Location(String cityName, String cityID, boolean isDefault){
        this.cityName = cityName;
        this.cityID = cityID;
        this.isDefault = isDefault;
    }

    public int getId() {
        return id;
    }

    public String getCityName() {
        return cityName;
    }

    public String getCityID() {
        return cityID;
    }

    public boolean isDefault() {
        return isDefault;
    }
}