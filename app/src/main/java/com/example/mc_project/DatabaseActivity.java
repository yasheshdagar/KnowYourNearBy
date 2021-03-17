package com.example.mc_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

public class DatabaseActivity extends AppCompatActivity {

    static FragmentManager fmng;
//    static RoomDbClass rdb;

    DatabaseActivity(){
//       rdb = Room.databaseBuilder(getApplicationContext(),RoomDbClass.class,"mydb").allowMainThreadQueries().build();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
//        rdb = Room.databaseBuilder(getApplicationContext(),RoomDbClass.class,"mydb").allowMainThreadQueries().build();

        fmng = getSupportFragmentManager();
        if(findViewById(R.id.Container)!=null){
            if(savedInstanceState!=null){
                return;
            }
            //fmng.beginTransaction().add(R.id.Container,new HomeFragment(),null).commit();
            fmng.beginTransaction().replace(R.id.Container,new ViewFragment(),null).addToBackStack(null).commit();

        }
    }

}