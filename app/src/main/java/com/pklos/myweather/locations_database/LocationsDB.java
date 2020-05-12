package com.pklos.myweather.locations_database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.pklos.myweather.locations_model.Location;

@Database(entities={Location.class}, exportSchema = false, version = 1)
public abstract class LocationsDB extends RoomDatabase {
    private static final String DB_NAME = "locations_db";
    private static LocationsDB instance;

    public static synchronized LocationsDB getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), LocationsDB.class, DB_NAME)
                           .fallbackToDestructiveMigration()
                           .build();
        }

        return instance;
    }

    public abstract LocationsDao locationsDao();
}