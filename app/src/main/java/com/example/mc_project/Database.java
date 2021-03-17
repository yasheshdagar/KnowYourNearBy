package com.example.mc_project;

import android.content.Context;
import android.content.ContextWrapper;

import androidx.room.Room;

class Database extends ContextWrapper {
    static RoomDbClass rdb;

    public Database(Context base) {
        super(base);
        rdb = Room.databaseBuilder(base, RoomDbClass.class,"mydb").allowMainThreadQueries().build();

    }
}
