package com.example.mc_project;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.divyanshu.colorseekbar.ColorSeekBar;
import com.example.mc_project.controller.GeofenceController;
import com.example.mc_project.model.response.SuggestionResponse;
import com.example.mc_project.presenter.PlacePresenter;
import com.example.mc_project.receiver.GeofenceReceiver;
import com.example.mc_project.utility.Utility;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private PendingIntent pendingIntent;
    private GeofencingClient geofencingClient;
    private GeofencingRequest geofencingRequest;

    private GeofenceController controller;
    private Intent intent;
    private float radius = 100f;
    private int r, g, b;
    private String color;
    private int circleColor;
    private StringBuilder strAddress;
    private GeoFence gf;
    private String requestId;

    private LinearLayout searchHolderLayout;
    private EditText searchBar;
    private PlacePresenter placePresenter;

    private boolean place, restaurants;

    private ProgressDialog progressDialog;


    private FusedLocationProviderClient fusedLocationProviderClient;
    //static RoomDbClass roomDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        searchHolderLayout = findViewById(R.id.search_holder_layout);
        searchBar = findViewById(R.id.search_bar);
        placePresenter = new PlacePresenter(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        geofencingClient = LocationServices.getGeofencingClient(this);
        controller = new GeofenceController(this);
        gf  = new GeoFence();

        place = getIntent().getBooleanExtra("places", false);
        restaurants = getIntent().getBooleanExtra("restaurants", false);

        if(place) searchHolderLayout.setVisibility(View.VISIBLE);
        else searchHolderLayout.setVisibility(View.GONE);

        intent = new Intent(this, GeofenceReceiver.class);

        searchBar.setOnEditorActionListener((textView,actionId,keyEvent)->{
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.setCancelable(Boolean.FALSE);
                }

                progressDialog.show();

                Utility.hideSoftKeyboard(this);
                placePresenter.getPlaceSuggestions("delhi", searchBar.getText().toString().trim());
            }
            return false;
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapLongClickListener(this);

        if(restaurants) {

            double latitude = Double.parseDouble(getIntent().getStringExtra("lat"));
            double longitude = Double.parseDouble(getIntent().getStringExtra("lng"));

            LatLng myLatLang = new LatLng(latitude, longitude);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLang, 16));

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(myLatLang);
            mMap.addMarker(markerOptions);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            Toast.makeText(this, "Location Permission Required", Toast.LENGTH_SHORT).show();
        }


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();

        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null && !restaurants) {
                    LatLng myLatLang = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLang, 16));
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        showSelectionDialog(latLng);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    void showSelectionDialog(LatLng latLng){

        radius = 100f;
        color = String.format("#%06X", (0xFFFFFF & -65536));
        circleColor = -65536;
        r = 255;
        g = 0;
        b = 0;

        final Dialog dialogUserSelection = new Dialog(this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialogUserSelection.setContentView(R.layout.dialog_user_selection);
        //dialogUserSelection.setCanceledOnTouchOutside(false);

        IndicatorSeekBar seekBarRadius = dialogUserSelection.findViewById(R.id.seekBarRadius);
        ColorSeekBar colorSeekBar = dialogUserSelection.findViewById(R.id.color_seek_bar);
        Button buttonDone = dialogUserSelection.findViewById(R.id.buttonDone);

        seekBarRadius.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                radius =  Float.parseFloat(seekParams.tickText);

            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
            }
        });

        colorSeekBar.setOnColorChangeListener(new ColorSeekBar.OnColorChangeListener() {
            @Override
            public void onColorChangeListener(int i) {
                color = String.format("#%06X", (0xFFFFFF & i));
                circleColor = i;
                r = Color.red(i);
                g = Color.green(i);
                b = Color.blue(i);


            }
        });
        dialogUserSelection.show();

        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogUserSelection.dismiss();

                double latitude = latLng.latitude;
                double longitude = latLng.longitude;

                Log.i("MapsActivity_Location", latitude + " " + longitude);


                requestId = getPlace(latitude, longitude);

                Geofence geofence = GeofenceController.addGeofence(requestId, latitude, longitude, radius);
                geofencingRequest = new GeofencingRequest.Builder().setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER).addGeofence(geofence).build();

                /*Register Broadcast Receiver
                 */
                pendingIntent = PendingIntent.getBroadcast(MapsActivity.this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                geofenceAdded(latLng, radius);
            }
        });
    }

    private String getPlace(double latitude, double longitude){
        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
        try {
            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);

            if (addressList.size() > 0) {
                Address place = addressList.get(0);
                strAddress = new StringBuilder();

                for (int i = 0; i <= place.getMaxAddressLineIndex(); i++) {

                    strAddress.append(place.getAddressLine(i)).append(" ");
                    Log.i("MapsActivity_place", strAddress.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return strAddress.toString();
    }

    private void geofenceAdded(LatLng latLng, float radius){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                    .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            String type = "custom";
                            gf.setId(0);
                            gf.setName(requestId);
                            gf.setLatitude(latLng.latitude);
                            gf.setLongitude(latLng.longitude);
                            gf.setRadius(radius);
                            gf.setColor(circleColor);
                            gf.setType(type);

                            //DatabaseActivity.rdb.geoFenceDao().addGeoFence(gf);
                            Database database = new Database(MapsActivity.this);
                            database.rdb.geoFenceDao().addGeoFence(gf);
//                            Toast.makeText(MapsActivity.this,"Data Saved !!",Toast.LENGTH_SHORT).show();


                            String title = "" + (int) radius + " m";
                            addMarker(latLng, title, color);
                            mMap.addCircle(controller.addCircle(latLng, radius, r, g, b));

                            showSuccessDialog();
                        }
                    }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MapsActivity.this, "Geofence not added" + e, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    Log.i("MapsActivity_Geofence", " " + e.getLocalizedMessage());

                }
            });
        }
        else {
            Toast.makeText(this, "Location Permission Required!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showSuccessDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Geofence was created successfully")
                .setCancelable(false)
                .setPositiveButton("Done", (dialog, which) -> {
                    dialog.dismiss();
                });

        builder.setTitle("Success");
        builder.create().show();
    }

    private void addMarker(LatLng latLng, String title, String color){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(title);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));

        Drawable markerIcon = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_location_on_24, null);
        markerIcon = DrawableCompat.wrap(markerIcon);
        markerIcon.setBounds(0, 0, markerIcon.getIntrinsicWidth(), markerIcon.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(markerIcon.getIntrinsicWidth(), markerIcon.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        DrawableCompat.setTint(markerIcon, Color.parseColor(color));
        markerIcon.draw(canvas);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));

        mMap.addMarker(markerOptions);
    }

    public void onSuccessSuggestion(SuggestionResponse response) {

        Log.i(this.getClass().getName(), "[onSuccessSuggestion] - Response received successfully");

        progressDialog.dismiss();

        if (response.getPlaceResponse().getPlaceList().size() > 0){

            LatLng myLatLang = new LatLng(Double.parseDouble(response.getPlaceResponse().getPlaceList().get(0).getLocation().getLatitude()), Double.parseDouble(response.getPlaceResponse().getPlaceList().get(0).getLocation().getLongitude()));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLang, 16));

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(myLatLang);
            markerOptions.title(response.getPlaceResponse().getPlaceList().get(0).getPlaceName());

            mMap.addMarker(markerOptions);

        } else Toast.makeText(this, "No results found", Toast.LENGTH_LONG).show();

    }

    public void onErrorSuggestion() {

        Log.i(this.getClass().getName(), "[onErrorSuggestion] - Error received");
        progressDialog.dismiss();
        Toast.makeText(this,"Request couldn't be processed",Toast.LENGTH_SHORT).show();
    }


}