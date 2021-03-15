package com.example.mc_project.controller;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

public class GeofenceController extends ContextWrapper {

    public GeofenceController(Context base) {
        super(base);
    }

    public static Geofence addGeofence(String requestId, double latitude, double longitude, float radius){
        Geofence geofence = new Geofence.Builder()
                .setRequestId(requestId)
                .setCircularRegion(latitude, longitude, radius)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT | Geofence.GEOFENCE_TRANSITION_DWELL)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setLoiteringDelay(10000) //trigger if user remains inside geofence for 10 sec
                .build();
        return geofence;

    }

    public static CircleOptions addCircle(LatLng latLng, float radius, int r, int g, int b){

        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(radius);
        circleOptions.strokeColor(Color.rgb(r, g, b));
        circleOptions.strokeWidth(3f);
        circleOptions.fillColor(Color.argb(30, r, g, b));
        return circleOptions;
    }
}
