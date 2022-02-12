package com.example.geofenceapp;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

import java.util.Date;

public class GeoBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        GeofencingEvent event = GeofencingEvent.fromIntent(intent);
        if (event.hasError()) {
            String errorMessage = GeofenceStatusCodes.getStatusCodeString(event.getErrorCode());
            Log.e("@@@BroadcastReceiver@@@", errorMessage);
            return;
        }
        if (event.getGeofenceTransition () == Geofence.GEOFENCE_TRANSITION_ENTER){
            Log.d("BroadcastReceiver", "- - - - - Entered in Geofence! - - - - -");

            final PendingResult result = goAsync();
            new Thread(() -> {
                MapPoints p=new MapPoints(event.getTriggeringLocation().getLatitude(),event.getTriggeringLocation().getLongitude(),(new Date()).getTime(),"ENTER",context);
                try {
                    p.persist();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                result.finish();
            }).start();
        }
        if (event.getGeofenceTransition () == Geofence.GEOFENCE_TRANSITION_EXIT){
            Log.d("BroadcastReceiver", "- - - - - Exited Geofence! - - - - -");
            final PendingResult result = goAsync();
            new Thread(() -> {

                MapPoints p2=new MapPoints(event.getTriggeringLocation().getLatitude(),event.getTriggeringLocation().getLongitude(),(new Date()).getTime(),"EXIT",context);
                try {
                    p2.persist();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                result.finish();
            }).start();

        }
    }
}