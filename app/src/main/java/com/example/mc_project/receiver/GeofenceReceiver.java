package com.example.mc_project.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.mc_project.notification.SendingNotification;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

public class GeofenceReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {

        String title = "", text = "";

        SendingNotification notification = new SendingNotification(context);

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if(geofencingEvent.hasError()){
            Log.i("GeofenceReceiver_Error", "Error in receiving geofence event");
            Toast.makeText(context, "Error receiving event", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Geofence> geofenceList =  geofencingEvent.getTriggeringGeofences();

        if(geofenceList.size() == 1){
            title = "Hey! Geofence ";
            text = "" + geofenceList.get(0).getRequestId();;
        }else {
            title = "Multiple geofences ";
            text = "Have a nice day...";
        }

        int type = geofencingEvent.getGeofenceTransition();

        if (type == Geofence.GEOFENCE_TRANSITION_ENTER){
            notification.sendNotification(title + "entered", text);
        }else if (type == Geofence.GEOFENCE_TRANSITION_EXIT){
            notification.sendNotification(title + "exited", text);
        }
    }
}