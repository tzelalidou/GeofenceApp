package com.example.geofenceapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class GeofenceHelper extends SQLiteOpenHelper {

    public GeofenceHelper(@Nullable Context context) {
        super(context, DbGeofence.DB_NAME, null, DbGeofence.DB_VERSION);
    }
    //METHOD ΠΟΥ ΔΗΜΙΟΥΡΓΕΙ ΣΥΝΔΕΣΗ ΜΕ ΒΔ.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbGeofence.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}