package com.example.geofenceapp;

public class DbGeofence {
    public static String DB_NAME="MAPS_DB";
    public static int DB_VERSION=1;
    public static String TABLE_NAME="GEOFENCE";
    public static String FIELD1="LAT";
    public static String FIELD2="LON";
    public static String FIELD3="TIMESTAMP";
    public static String FIELD4="ACTION";
    public static String CREATE_TABLE="CREATE TABLE "+DbGeofence.TABLE_NAME+"("+DbGeofence.FIELD1+" TEXT,"+DbGeofence.FIELD2+" TEXT,"+DbGeofence.FIELD3+" TEXT,"+DbGeofence.FIELD4+" TEXT);";
    public static String AUTHORITIES="com.example.geofenceapp";
    public static String PATH=DbGeofence.TABLE_NAME;
}

