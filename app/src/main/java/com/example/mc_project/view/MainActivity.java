package com.example.mc_project.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.mc_project.DatabaseActivity;
import com.example.mc_project.MapsActivity;
import com.example.mc_project.R;
import com.example.mc_project.ViewFragment;

public class MainActivity extends AppCompatActivity {

    private Button places, restaurants, covidHotSpots, myGeofences;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        places = findViewById(R.id.places);
        restaurants = findViewById(R.id.restaurants);
        covidHotSpots = findViewById(R.id.covid_hotspots);
        myGeofences = findViewById(R.id.my_geofences);

        frameLayout = findViewById(R.id.fragment_container);


        //link your fragments here
//        covidHotSpots.setOnClickListener((view) ->
//            addFragment(new CovidFragment(),"CovidFragment", true, null, R.id.fragment_container)
//
//        );
//
        covidHotSpots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFragment(new CovidFragment(), "CovidFragment", true, null, R.id.fragment_container);
                frameLayout.setVisibility(View.VISIBLE);
            }
        });


        myGeofences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frameLayout.setVisibility(View.VISIBLE);
                addFragment(new ViewFragment(), "ViewFragment", true, null, R.id.fragment_container);
            }
        });
    }

    public void addFragment(Fragment fragment, String tag, boolean addToBackStack, Bundle bundle, int frameLayout) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragment.setArguments(bundle);
        transaction.add(frameLayout, fragment, tag);

        if (addToBackStack) transaction.addToBackStack(tag);
        else transaction.disallowAddToBackStack();

        transaction.commitAllowingStateLoss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.addgeofence, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.menu_add_geofence) {
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}