package com.example.mc_project;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GeoFenceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addGeoFence(GeoFence geoFence);

    @Query("SELECT * FROM GeoFence")
    List<GeoFence> getAllGeoFences();

    @Query("DELETE FROM GeoFence WHERE Id = :id")
    void deleteById(int id);

}
