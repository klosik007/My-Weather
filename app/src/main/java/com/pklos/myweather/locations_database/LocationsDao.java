package com.pklos.myweather.locations_database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.pklos.myweather.locations_model.Location;

import java.util.List;

@Dao
public interface LocationsDao {
    @Query("SELECT * FROM locations")
    List<Location> getLocationsList();

//    @Query("SELECT cityName, isDefault FROM locations WHERE id LIKE :ID")
//    List<Location> getLocation(int ID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLocation(Location... locations);

    @Update
    void updateLocation(Location... locations);

    @Delete
    void deleteLocation(Location... locations);
}