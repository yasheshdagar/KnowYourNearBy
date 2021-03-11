package com.example.mc_project;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

public class GeofenceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        SendingNotification notification = new SendingNotification(context);

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if(geofencingEvent.hasError()){
            Log.i("GeofenceReceiver_Error", "Error in receving geofence event");
            Toast.makeText(context, "Error receiving event", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Geofence> geofenceList =  geofencingEvent.getTriggeringGeofences();

        int type = geofencingEvent.getGeofenceTransition();
        if (type == Geofence.GEOFENCE_TRANSITION_ENTER){
            notification.sendNotification("Entered", "You entered");
            Toast.makeText(context, "Entered", Toast.LENGTH_LONG).show();

        }else if (type == Geofence.GEOFENCE_TRANSITION_EXIT){
            notification.sendNotification("Exited", "You Exited");
            Toast.makeText(context, "Exited", Toast.LENGTH_LONG).show();

        }else if (type == Geofence.GEOFENCE_TRANSITION_DWELL){
            notification.sendNotification("Stayed inside", "Stayed Inside for 10 sec");
            Toast.makeText(context, "Stayed Inside", Toast.LENGTH_LONG).show();
        }
    }
}