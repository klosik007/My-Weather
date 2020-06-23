package com.pklos.myweather.utils;

import android.content.Context;
import android.util.Log;

import com.pklos.myweather.locations_database.LocationsDB;
import com.pklos.myweather.locations_database.MyWeatherExecutors;
import com.pklos.myweather.locations_model.Location;

import java.util.List;

public class LocalDBUtils {
    public static void insertDataToDB(Context context, int id, String cityName){
        final LocationsDB appDB = LocationsDB.getInstance(context);
        final Location location = new Location(cityName, String.valueOf(id), false);
        MyWeatherExecutors.getInstance().getDiskIO().execute(new Runnable(){
            @Override
            public void run() {
                appDB.locationsDao().insertLocation(location);
            }
        });
    }

    public static void deleteDataFromDB(Context context, final Location location){
        final LocationsDB appDB = LocationsDB.getInstance(context);
        MyWeatherExecutors.getInstance().getDiskIO().execute(new Runnable(){
            @Override
            public void run() {
                appDB.locationsDao().deleteLocation(location);
            }
        });
    }

    public static void fetchLocationsData(Context context){
        final LocationsDB appDB = LocationsDB.getInstance(context);

        MyWeatherExecutors.getInstance().getDiskIO().execute(new Runnable(){
            @Override
            public void run() {
                final List<Location> locations = appDB.locationsDao().getLocationsList();
                for (Location location : locations){
                    Log.d("Locations", location.getId() + location.getCityName() + location.getCityID() + location.isDefault());
                }
            }
        });
    }
}