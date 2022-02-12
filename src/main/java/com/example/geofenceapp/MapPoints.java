package com.example.geofenceapp;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class MapPoints {
    private double lat;
    private double lon;
    private long timestamp;
    private String action;
    public GeofenceHelper help;

    public MapPoints() {
    }

    public MapPoints(double lat, double lon, long timestamp, String action) {
        this.lat = lat;
        this.lon = lon;
        this.timestamp = timestamp;
        this.action = action;
    }
    public MapPoints(double lat, double lon, long timestamp, String action, Context context) {
        this.lat = lat;
        this.lon = lon;
        this.timestamp = timestamp;
        this.action = action;
        help=new GeofenceHelper(context);

    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(long lon) {
        this.lon = lon;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
    public  long persist() throws Exception {
        //insert geofence in db
        ContentValues cntValues=new ContentValues();

        Log.d("LAT",String.valueOf(this.lat));
        Log.d("long",String.valueOf(this.lon));
        Log.d("time",String.valueOf(this.timestamp));
        Log.d("action",this.action);
        cntValues.put(DbGeofence.FIELD1,String.valueOf(this.lat));
        cntValues.put(DbGeofence.FIELD2,String.valueOf(this.lon));
        cntValues.put(DbGeofence.FIELD3,String.valueOf(this.timestamp));
        cntValues.put(DbGeofence.FIELD4,this.action);
        SQLiteDatabase db=help.getWritableDatabase();
        long result= db.insert(DbGeofence.TABLE_NAME,null,cntValues);
        db.close();
        if(result==-1){
            throw new Exception("Insert Failed!!");
        }
        else{
            Log.d("INSERED","TRUE");
        }
        return  result;
    }

    public static ArrayList<MapPoints> getContacts(Context context ) throws Exception {
        //get all geofence from db
        GeofenceHelper helper=new GeofenceHelper(context);
        ArrayList<MapPoints> points=new ArrayList<>();
        SQLiteDatabase db=helper.getReadableDatabase();
        @SuppressLint("Recycle") Cursor word = db.query(DbGeofence.TABLE_NAME, null, null, null, null, null, null, null);
        if (word.moveToFirst()) {
            do {
                MapPoints point=new MapPoints(Double.parseDouble(word.getString(0)),Double.parseDouble(word.getString(1)),Long.parseLong(word.getString(2)),word.getString(3));
                points.add(point);
            } while (word.moveToNext());
        }
        db.close();
        return points;

    }
}
