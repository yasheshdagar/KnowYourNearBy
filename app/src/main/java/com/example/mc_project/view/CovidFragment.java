package com.example.mc_project.view;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mc_project.Database;
import com.example.mc_project.GeoFence;
import com.example.mc_project.MapsActivity;
import com.example.mc_project.R;
import com.example.mc_project.adapter.CovidAdapter;
import com.example.mc_project.api.ApiUrl;
import com.example.mc_project.controller.GeofenceController;
import com.example.mc_project.listener.CovidListener;
import com.example.mc_project.model.request.LocationGeoIQRequest;
import com.example.mc_project.model.response.LocationGeoIQResponse;
import com.example.mc_project.presenter.CovidPresenter;
import com.example.mc_project.receiver.GeofenceReceiver;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CovidFragment extends Fragment implements CovidListener {

    private LinearLayoutManager layoutManager;

    private CovidAdapter covidAdapter;

    private RecyclerView recyclerView;

    private List<String> zonesList;

    private CovidPresenter covidPresenter;

    private Geocoder geocoder;

    private GeofencingClient geofencingClient;

    private GeofencingRequest geofencingRequest;

    private ProgressDialog progressDialog;

    private TextView emptyView;

    private double latitude, longitude;

    private final int PENDING_INTENT_REQUEST_CODE = 101;

    private static final String[] LOCATION_PERMISSION = {
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(this.getActivity().getClass().getName(), "[OnCreateView] - Fragment inflated");
        return inflater.inflate(R.layout.fragment_covid_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(this.getActivity().getClass().getName(), "[OnViewCreated] - Fragment view created");

        latitude = getArguments().getDouble("latitude");
        longitude = getArguments().getDouble("longitude");

        recyclerView = view.findViewById(R.id.covid_recycler);
        emptyView = view.findViewById(R.id.empty_view);

        layoutManager = new LinearLayoutManager(getActivity());
        zonesList = new ArrayList<>();
        covidAdapter = new CovidAdapter(getActivity(), zonesList, this);
        covidPresenter = new CovidPresenter(this);
        geocoder = new Geocoder(getActivity());
        geofencingClient = LocationServices.getGeofencingClient(getActivity());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(covidAdapter);

        getLocationBasedRegions();
    }

    private void getLocationBasedRegions() {

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(Boolean.FALSE);
        }

        progressDialog.show();
        covidPresenter.getLocationBasedRegions(createRequest(latitude, longitude));
    }

    private boolean checkPermission(String[] locationPermission) {

        for (String perm : locationPermission) {
            boolean hasPerm = (ContextCompat.checkSelfPermission(getActivity(), perm) == PackageManager.PERMISSION_GRANTED);
            if (!hasPerm) return false;
        }

        return true;
    }

    private LocationGeoIQRequest createRequest(double latitude, double longitude) {

        LocationGeoIQRequest locationGeoIQRequest = new LocationGeoIQRequest();
        locationGeoIQRequest.setKey(ApiUrl.API_KEY);
        locationGeoIQRequest.setLatitude(Double.toString(latitude));
        locationGeoIQRequest.setLongitude(Double.toString(longitude));
        locationGeoIQRequest.setRadius(Integer.toString(5000));

        return locationGeoIQRequest;
    }

    public void onSuccessLocationBased(LocationGeoIQResponse response) {
        Log.i(this.getActivity().getClass().getName(), "[onSuccessLocationBased] - Response received successfully");

        progressDialog.dismiss();

        if (response.getContainmentZones().size() > 0) {
            zonesList.addAll(response.getContainmentZones());
            covidAdapter.notifyDataSetChanged();
        } else {
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClickCreate(int position) {

        List<Address> list = null;

        try {
            list = geocoder.getFromLocationName(zonesList.get(position), 1);
        } catch (IOException e) {
            Log.i(this.getActivity().getClass().getName(), "[onClickCreate] - Exception message:" + e.getMessage());
        }

        if (list == null) {
            Log.i(this.getActivity().getClass().getName(), "[onClickCreate] - No location found");
        } else {
            setUpGeofence(list.get(0).getLatitude(), list.get(0).getLongitude(), zonesList.get(position));
        }
    }

    private void setUpGeofence(double latitude, double longitude, String placeName) {

        Intent intent = new Intent(getActivity(), GeofenceReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), PENDING_INTENT_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Geofence geofence = GeofenceController.addGeofence(placeName, latitude, longitude, 300); //radius is in metres

        geofencingRequest = new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofence(geofence)
                .build();

        if (checkPermission(LOCATION_PERMISSION)) {
            geofencingClient.addGeofences(geofencingRequest, pendingIntent).addOnSuccessListener(getActivity(), (aVoid) -> {
                Toast.makeText(getActivity(), "Geofence created successfully...", Toast.LENGTH_LONG).show();
                Database.getRdb().geoFenceDao().addGeoFence(createGeofenceData(latitude, longitude, placeName));

            }).addOnFailureListener(getActivity(), e -> {
                Log.i(this.getActivity().getClass().getName(), "[setUpGeofence] - error:" + e.getMessage());
                Toast.makeText(getActivity(), "Geofence not available...", Toast.LENGTH_LONG).show();
            });
        }

    }

    private GeoFence createGeofenceData(Double latitude, Double longitude, String placeName) {

        GeoFence geoFence = new GeoFence();
        geoFence.setId(0);
        geoFence.setName(placeName);
        geoFence.setLatitude(latitude);
        geoFence.setLongitude(longitude);
        geoFence.setRadius(300);
        geoFence.setColor(-65536); //red integer value
        geoFence.setType("covid");

        return geoFence;
    }
}
