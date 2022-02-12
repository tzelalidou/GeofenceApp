package com.example.mydatafrommapsviacontentprovider;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.mydatafrommapsviacontentprovider.databinding.ActivityMapsBinding;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int GEOFENCING_FINE_PERMISSION_CODE = 3;
    private static final int ENABLE_MYLOCATION_FINE_PERMISSION_CODE = 4;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        ContentResolver resolver = this.getContentResolver();//den t dhmioyrgw einai etoimo gia ka8e app

/*
        enableMylocation();

        while(true){
            Location myLocation = googleMap.getMyLocation();  //Nullpointer exception.........
            LatLng mark = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(mark).title("Marked"));
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d("23",String.valueOf(mark.latitude));
            Log.d("vv",String.valueOf(mark.longitude));
        }



 */





        Uri uri=Uri.parse("content://"+DbGeofenceMAPS.AUTHORITIES+"/"+DbGeofenceMAPS.PATH);
        //ContentProviderClient yourCR = getContentResolver().acquireContentProviderClient(uri);
        Cursor crs;

            crs = resolver.query(uri,null,null,null,null);
            if (crs.moveToFirst()) {
                do {
                    LatLng mark = new LatLng( Double.parseDouble(crs.getString(0)), Double.parseDouble(crs.getString(1)));
                    mMap.addMarker(new MarkerOptions().position(mark).title("Marked at time:"+crs.getString(2)+" actioned:"+crs.getString(3)));
                } while (crs.moveToNext());
            }






    }
/*
    private void enableMylocation(){

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ENABLE_MYLOCATION_FINE_PERMISSION_CODE);
            return;
        }
        mMap.setMyLocationEnabled(true);
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

        }
    }


 */


}