package com.example.mc_project.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Button;

import com.example.mc_project.R;

public class MainActivity extends AppCompatActivity {

    private Button places, restaurants, covidHotSpots, myGeofences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        places = findViewById(R.id.places);
        restaurants = findViewById(R.id.restaurants);
        covidHotSpots = findViewById(R.id.covid_hotspots);
        myGeofences = findViewById(R.id.my_geofences);

        //link your fragments here
        covidHotSpots.setOnClickListener((view) ->
            addFragment(new CovidFragment(),"CovidFragment", true, null, R.id.fragment_container)
        );
    }

    public void addFragment(Fragment fragment, String tag, boolean addToBackStack, Bundle bundle, int frameLayout) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragment.setArguments(bundle);
        transaction.add(frameLayout, fragment, tag);

        if (addToBackStack) transaction.addToBackStack(tag);
        else transaction.disallowAddToBackStack();

        transaction.commitAllowingStateLoss();
    }
}