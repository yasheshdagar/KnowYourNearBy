package com.example.mc_project;

import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.example.mc_project.controller.GeofenceController;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ViewGeofence extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private double latitude, longitude;
    private float radius;
    private int color;
    private String type;
    private int r,g,b;
    private String requestId;

    private GeofenceController geofenceController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_geofence);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        requestId = intent.getStringExtra("requestId");
        latitude = intent.getDoubleExtra("latitude", 0);
        longitude = intent.getDoubleExtra("longitude", 0);
        color = intent.getIntExtra("color", 0);
        radius = intent.getFloatExtra("radius", 0);
        type = intent.getStringExtra("type");

        geofenceController = new GeofenceController(this);

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

        LatLng LatLang = new LatLng(latitude, longitude);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLang, 16));

        String circleColor = String.format("#%06X", (0xFFFFFF & color));

        String title = "" + (int) radius + " m";
        r = Color.red(color);
        g = Color.green(color);
        b = Color.blue(color);

        CircleOptions circleOptions= geofenceController.addCircle(LatLang, radius, r,  g, b);
        mMap.addCircle(circleOptions);
        addMarker(LatLang, title, circleColor);
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
}