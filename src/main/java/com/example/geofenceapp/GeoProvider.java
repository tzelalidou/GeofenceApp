package com.example.geofenceapp;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class GeoProvider extends ContentProvider {
    private GeofenceHelper helper;
    public UriMatcher uriMatcher;

    @Override
    public boolean onCreate() {
        uriMatcher=new UriMatcher(android.content.UriMatcher.NO_MATCH);
        uriMatcher.addURI(DbGeofence.AUTHORITIES,DbGeofence.PATH,1);
        uriMatcher.addURI(DbGeofence.AUTHORITIES,DbGeofence.PATH+"/#",2);
        helper=new GeofenceHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db=helper.getReadableDatabase();
        Cursor crs=null;
        switch (uriMatcher.match(uri)) {
            case 1:
                crs=db.query(DbGeofence.TABLE_NAME,null,null,null,null,null,null,null);
                break;
            case 2:
                String id=uri.getLastPathSegment();
                crs=db.query(DbGeofence.TABLE_NAME,null,"ROWID=?",new String[]{id},null,null,null,null);
                break;
            default:
                System.out.println("Midweek days are so-so.");
                break;
        }
        return crs;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}

