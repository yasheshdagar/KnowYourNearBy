package com.example.mc_project.view;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mc_project.R;
import com.example.mc_project.api.ApiUrl;
import com.example.mc_project.model.request.InContainmentZoneRequest;
import com.example.mc_project.model.response.InContainmentZoneResponse;
import com.example.mc_project.presenter.InContainmentZonePresenter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private DrawerLayout drawerLayout;

    private ActionBarDrawerToggle toggle;

    private NavigationView navigationView;

    private TextView state, district, totalCases, activeCases;

    private InContainmentZonePresenter containmentZonePresenter;

    private ProgressDialog progressDialog;

    private static final String[] LOCATION_PERMISSION = {
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION"
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation);
        state = findViewById(R.id.state);
        district = findViewById(R.id.district_name);
        totalCases = findViewById(R.id.total_cases);
        activeCases = findViewById(R.id.active_cases);

        toolbar.setTitle("Know Your Nearby");

        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        containmentZonePresenter = new InContainmentZonePresenter(this);

        toggle.getDrawerArrowDrawable().setColor(getColor(R.color.white));

        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        navigationView.setNavigationItemSelectedListener((item) -> {

            switch (item.getItemId()){

                case R.id.hot_spots:
                    if(!checkVisibility("CovidFragment"))
                        addFragment(new CovidFragment(),"CovidFragment", true, null, R.id.fragment_container);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
            }

            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLatLng();
    }

    private void getLatLng() {
        if (checkPermission(LOCATION_PERMISSION)) {
            FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
            Task<Location> locationTask = client.getLastLocation();

            locationTask.addOnSuccessListener(location -> {
                Log.i(this.getClass().getName(), "[getLatLng] - Latitude " + location.getLatitude());
                Log.i(this.getClass().getName(), "[getLatLng] - Longitude " + location.getLongitude());

                if(progressDialog == null){
                    progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.setCancelable(Boolean.FALSE);
                }

                progressDialog.show();
                containmentZonePresenter.getInContainmentZoneStatus(createInContainmentZoneRequest(location.getLatitude(),location.getLongitude()));
            });

            locationTask.addOnFailureListener(e -> {});

        } else Toast.makeText(this, "Location Permission Required", Toast.LENGTH_SHORT).show();

    }

    private boolean checkPermission(String[] locationPermission) {

        for (String perm : locationPermission) {
            boolean hasPerm = (ContextCompat.checkSelfPermission(this, perm) == PackageManager.PERMISSION_GRANTED);
            if (!hasPerm) return false;
        }

        return true;
    }

    private InContainmentZoneRequest createInContainmentZoneRequest(double latitude, double longitude) {
        InContainmentZoneRequest request = new InContainmentZoneRequest();

        ArrayList<ArrayList<Double>> arrayList = new ArrayList<>();
        arrayList.add(new ArrayList<>());

        arrayList.get(0).add(latitude);
        arrayList.get(0).add(longitude);

        request.setKey(ApiUrl.API_KEY);
        request.setLatLngs(arrayList);

        return request;
    }

    public void onSuccessZoneStatus(InContainmentZoneResponse response){

        Log.i(this.getClass().getName(), "[onSuccessZoneStatus] - Response received successfully");

        progressDialog.dismiss();

        if(!response.getInContainmentList().get(0).isZoneStatus()) state.setText("Safe");
        else state.setText("UnSafe");

        district.setText(response.getInContainmentList().get(0).getDistrict());
        totalCases.setText(Integer.toString(response.getInContainmentList().get(0).getTotalCases()));
        activeCases.setText(Integer.toString(response.getInContainmentList().get(0).getActiveCases()));
    }

    public void addFragment(Fragment fragment, String tag, boolean addToBackStack, Bundle bundle, int frameLayout) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragment.setArguments(bundle);
        transaction.add(frameLayout, fragment, tag);

        if (addToBackStack) transaction.addToBackStack(tag);
        else transaction.disallowAddToBackStack();

        transaction.commitAllowingStateLoss();
    }

    private boolean checkVisibility(String tag) {

        int topOfStack = (getSupportFragmentManager().getBackStackEntryCount() - 1);

        if(topOfStack == -1) return false;

        if (getSupportFragmentManager().getBackStackEntryAt(topOfStack).getName().equalsIgnoreCase(tag))
            return true;

        return false;
    }
}