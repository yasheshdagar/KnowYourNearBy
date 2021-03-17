package com.example.mc_project;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {GeoFence.class},version = 2)
public abstract class RoomDbClass extends RoomDatabase {
    public abstract GeoFenceDao geoFenceDao();
}
