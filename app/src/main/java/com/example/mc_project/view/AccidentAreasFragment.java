package com.example.mc_project.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mc_project.R;
import com.example.mc_project.controller.GeofenceController;
import com.example.mc_project.receiver.GeofenceReceiver;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AccidentAreasFragment extends Fragment implements LocationListener {

    private LocationManager locationManager;
    private GoogleMap mMap;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private Geocoder geoCoderLocation;

    private String city;
    private double distanceKm;
    private double nearByThreshold = 150;
    private GeofencingRequest geofencingRequest;
    private PendingIntent pendingIntent;
    private Intent intent;
    private GeofencingClient geofencingClient;
    private GeofenceController controller;
    private float radius = 500f;

    private Context context;

    Geocoder geocoder;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
//            LatLng sydney = new LatLng(-34, 151);
//            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Permissions not granted", Toast.LENGTH_SHORT).show();
            }else {
                mMap.setMyLocationEnabled(true);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 50000, 0, AccidentAreasFragment.this); //50 sec
            }

            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        context = getActivity();

        View view =  inflater.inflate(R.layout.fragment_accident_areas, container, false);
        intent = new Intent(context, GeofenceReceiver.class);
        intent.putExtra("accidentalArea", 5);
        geofencingClient = LocationServices.getGeofencingClient(context);
        controller = new GeofenceController(context);
        geoCoderLocation = new Geocoder(context);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapAccidents);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

        geocoder = new Geocoder(context, Locale.getDefault());
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {

        mMap.clear();
        removeGeofence();

        Log.i("location_fragment", "" + location + location.getLatitude());
        LatLng myLatLang = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLang, 12));


        try {
            List<Address> currentAddress  = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String address = currentAddress.get(0).getAddressLine(0);
            city = currentAddress.get(0).getLocality();
            String state = currentAddress.get(0).getAdminArea();

            Location locationCurrent = new Location("Current Location");
            locationCurrent.setLatitude(location.getLatitude());
            locationCurrent.setLongitude(location.getLongitude());


            //state = "Haryana";
            //city = "Ambala";
            databaseReference.child(state).child(city).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.i("dataRet", "" + snapshot.getKey() + " -  " + snapshot.getValue()  + " -  "  + snapshot.getChildren() + " - " + snapshot);

                    if(snapshot.getValue() != null) {
                        String areas[] = snapshot.getValue().toString().split(" : ");

                        List<Address> addressList = null;

                        for (int i = 0; i < areas.length; i++) {
                            try {
                                String area = areas[i] + " " + city;
                                addressList = geoCoderLocation.getFromLocationName(area, 1);

                                Address areaAccident = addressList.get(0);
                                double latitude = areaAccident.getLatitude();
                                double longitude = areaAccident.getLongitude();

                                Location locationAccidentalArea = new Location("Black Spot");
                                locationAccidentalArea.setLatitude(latitude);
                                locationAccidentalArea.setLongitude(longitude);

                                Log.i("Distance12", "" + locationCurrent.distanceTo(locationAccidentalArea) / 1000 + " " + area);

                                distanceKm = locationCurrent.distanceTo(locationAccidentalArea) / 1000;
                                Log.i("lat111", "" + latitude + " - " + longitude + "    " + area);

                                if (distanceKm < nearByThreshold) {
                                    //Set Geofence

                                    Geofence geofence = GeofenceController.addGeofence(area, latitude, longitude, radius);
                                    geofencingRequest = new GeofencingRequest.Builder().setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER).addGeofence(geofence).build();

                                    /*Register Broadcast Receiver
                                     */
                                    pendingIntent = PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                    LatLng latLng = new LatLng(latitude, longitude);
                                    geofenceAdded(latLng, radius);
                                }

                            } catch (IOException e) {
                            }

                        }
                    }else {
                        Toast.makeText(controller, "No black spots nearby", Toast.LENGTH_SHORT).show();
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onProviderDisabled(@NonNull String provider) {
        Toast.makeText(context, "Enable Location", Toast.LENGTH_SHORT).show();
    }

    private void geofenceAdded(LatLng latLng, float radius){
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            String title = "" + (int) radius + " m";
                            int color = 16711680;
                            String circleColor = String.format("#%06X", (0xFFFFFF & color));

                            int r = Color.red(color);
                            int g = Color.green(color);
                            int b = Color.blue(color);

                            addMarker(latLng, title, circleColor);
                            mMap.addCircle(controller.addCircle(latLng, radius, r, g, b));

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i("Accidental_Geofence", " " + e.getLocalizedMessage());

                }
            });
        }else {
            Toast.makeText(context, "Location Permission Required!", Toast.LENGTH_SHORT).show();
        }

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

    void removeGeofence(){
        geofencingClient.removeGeofences(pendingIntent)
                .addOnSuccessListener(aVoid -> {

                }).addOnFailureListener(e -> {

                });
    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }
}


