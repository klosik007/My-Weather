package com.pklos.myweather.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.pklos.myweather.R;
import com.pklos.myweather.locations_database.LocationsDB;
import com.pklos.myweather.locations_database.MyWeatherExecutors;
import com.pklos.myweather.locations_model.Location;

import java.util.List;

public class LocalDBUtils {
    private static String _id;

    public static String getDefaultCityId(){ return _id;}

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

//    public static void fetchDefaultCityID(Context context){
//        final LocationsDB appDB = LocationsDB.getInstance(context);
//        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
//        final SharedPreferences.Editor editor = sharedPref.edit();
//
//        MyWeatherExecutors.getInstance().getDiskIO().execute(new Runnable(){
//            @Override
//            public void run() {
//                _id = appDB.locationsDao().getDefaultLocation(true);
//                Log.d("defaultloc", _id);
//            }
//        });
//
//        editor.putInt(getString(R.string.saved_high_score_key), _id);
//        editor.commit();
//    }

    public static void fetchDefaultCityID(final Context context){
        final LocationsDB appDB = LocationsDB.getInstance(context);
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = sharedPref.edit();

        MyWeatherExecutors.getInstance().getDiskIO().execute(new Runnable(){
            @Override
            public void run() {
                String _id = appDB.locationsDao().getDefaultLocation(true);
                Log.d("defaultloc", _id);
                editor.putString(context.getString(R.string.sp_default_city_id), _id);
                editor.commit();
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