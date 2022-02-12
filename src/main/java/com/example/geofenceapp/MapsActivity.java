package com.example.geofenceapp;


import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;


import com.example.geofenceapp.databinding.ActivityMapsBinding;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    //constant variable for identify the requestPermission for ACCESS_FINE_LOCATION permission in addgeofences method .
    private static final int GEOFENCING_FINE_PERMISSION_CODE = 3;
    private static final int ENABLE_MYLOCATION_FINE_PERMISSION_CODE = 4;
    private static float GEOFENCE_RADIUS = 200;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    GeofencingClient geofencingClient;
    //list of geofences dynamic initialize from googlemap
    private List<Geofence> geofenceList=new ArrayList<>();
    private PendingIntent geofencePendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        geofencingClient = LocationServices.getGeofencingClient(MapsActivity.this);


    }

    private void addGeofences() {
        //ask fot user location to be on
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //if its not on ask for permission
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, GEOFENCING_FINE_PERMISSION_CODE);
            return;
        }

        geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                .addOnSuccessListener(MapsActivity.this, unused -> Log.d("GeofencingClient:","Successfully geofence added!!!!!!!!!!!!!!!!!!!!!!"))
                .addOnFailureListener(MapsActivity.this, e -> Log.d("GeofencingClient:","Failled in geofence addition"));
    }

    private GeofencingRequest getGeofencingRequest() {

        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        //activate when someone ENTERS the geofence
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofenceList);

        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (geofencePendingIntent != null) {


            return geofencePendingIntent;
        }

        //those who will sent the pending intent they will wake up the geobroadcast receiver
        Intent intent = new Intent(this, GeoBroadcastReceiver.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling addGeofences() and removeGeofences().
        geofencePendingIntent = PendingIntent.getBroadcast (this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        return geofencePendingIntent;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //its called by ActivityCompat.requestPermissions
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ENABLE_MYLOCATION_FINE_PERMISSION_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableMylocation();
                }
                break;
            case GEOFENCING_FINE_PERMISSION_CODE:
                //if the user give his permission in location call add geofences  and do the code after the if case
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    addGeofences();
                }
                break;

        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        //current location latLng
        mMap.setOnMapLongClickListener(latLng -> {
            //when the user longclicks an area makes a geonfence
            geofenceList.add(new Geofence.Builder()
                    .setCircularRegion(latLng.latitude,latLng.longitude,GEOFENCE_RADIUS)
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |Geofence.GEOFENCE_TRANSITION_EXIT)
                    .setRequestId("Geofence_ID")
                    //here i can enter other parameters
                    .build());
            CircleOptions circleOptions=new CircleOptions();
            circleOptions.radius(GEOFENCE_RADIUS);
            circleOptions.center(latLng);
            mMap.addCircle(circleOptions);


            addGeofences();
        });
        enableMylocation();

    }
    private void enableMylocation(){

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ENABLE_MYLOCATION_FINE_PERMISSION_CODE);
            return;
        }
        mMap.setMyLocationEnabled(true);
    }
}