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

    @Query("SELECT cityID FROM locations WHERE isDefault=:isDefault")
    String getDefaultLocation(boolean isDefault);

    @Query("UPDATE locations SET isDefault=:setDefault")
    void updateIsDefaultToFalse(boolean setDefault);

    @Query("UPDATE locations SET isDefault=:setDefault WHERE id=:id")
    void updateDefaultCity(boolean setDefault, int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLocation(Location... locations);

    @Update
    void updateLocation(Location... locations);

    @Delete
    void deleteLocation(Location... locations);
}