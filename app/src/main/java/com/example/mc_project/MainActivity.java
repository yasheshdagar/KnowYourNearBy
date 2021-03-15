package com.example.mc_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.room.Room;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    static FragmentManager fmng;
    static RoomDbClass rdb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fmng = getSupportFragmentManager();
        rdb = Room.databaseBuilder(getApplicationContext(),RoomDbClass.class,"mydb").allowMainThreadQueries().build();
        if(findViewById(R.id.Container)!=null){
            if(savedInstanceState!=null){
                return;
            }
            fmng.beginTransaction().add(R.id.Container,new HomeFragment(),null).commit();
        }
    }
}