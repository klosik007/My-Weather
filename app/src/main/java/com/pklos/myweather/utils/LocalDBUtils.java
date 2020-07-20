package com.pklos.myweather.utils;

import android.content.Context;
import android.util.Log;

import com.pklos.myweather.locations_database.LocationsDB;
import com.pklos.myweather.locations_database.MyWeatherExecutors;
import com.pklos.myweather.locations_model.Location;

import java.util.List;

public class LocalDBUtils {
    private static volatile String id;

    public static String getDefaultCityId(){ return id;}

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

    public static void fetchDefaultCityID(Context context){
        final LocationsDB appDB = LocationsDB.getInstance(context);

        MyWeatherExecutors.getInstance().getDiskIO().execute(new Runnable(){
            @Override
            public void run() {
                id = appDB.locationsDao().getDefaultLocation(true);
                Log.d("defaultloc", id);
            }
        });
    }

    public static void updateDefaultCityDataInDB(Context context, final int id, final boolean setDefault){
        final LocationsDB appDB = LocationsDB.getInstance(context);
        MyWeatherExecutors.getInstance().getDiskIO().execute(new Runnable(){
            @Override
            public void run() {
                appDB.locationsDao().updateDefaultCity(setDefault, id);
            }
        });
    }

    public static void updateIsDefaultDataInDBToFalse(Context context){
        final LocationsDB appDB = LocationsDB.getInstance(context);
        MyWeatherExecutors.getInstance().getDiskIO().execute(new Runnable(){
            @Override
            public void run() {
                appDB.locationsDao().updateIsDefaultToFalse(false);
            }
        });
    }
}