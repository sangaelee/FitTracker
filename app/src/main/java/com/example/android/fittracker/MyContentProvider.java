package com.example.android.fittracker;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.example.android.fittracker.data.FittrackerDBHandler;
import com.example.android.fittracker.data.FittrackerTableHandler;

import java.util.Arrays;
import java.util.HashSet;

public class MyContentProvider extends ContentProvider {


    private FittrackerDBHandler database;

    private static final int Fittracker = 1;
    private static final int Fittracker_ID = 2;

    private static final String AUTHORITY = "com.example.android.fittracker.MyContentProvider";
    private static final String BASE_PATH = "fittracker";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/fittrackers";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/fittracker";

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, Fittracker);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", Fittracker_ID);
    }



    @Override
    public boolean onCreate() {
        //Instantiate the database in the onCreate() method
        database = new FittrackerDBHandler(getContext());
        return false;
    }


    private void checkColumns(String[] projection) {
        String[] available = { FittrackerTableHandler.FittrackerID,
                FittrackerTableHandler.Date,
                FittrackerTableHandler.Calorie,
                FittrackerTableHandler.Distance,
                FittrackerTableHandler.Duration

        };

        if(projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));

            if (availableColumns.contains(requestedColumns)){
                System.out.println("here finish");
            }
            if(!availableColumns.contains(requestedColumns)) {
                //   throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }

    @Override
    public Cursor query(Uri uri,
                        String[] projection,
                        String selection,
                        String[] selectionArgs,
                        String sortOrder) {


        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        checkColumns(projection);
        queryBuilder.setTables(FittrackerTableHandler.FittrackerTable);

        int uriType = sURIMatcher.match(uri);
        switch(uriType) {
            case Fittracker:
                //no filter
                break;
            case Fittracker_ID:
                queryBuilder.appendWhere(FittrackerTableHandler.FittrackerID+ "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI:" + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection,
                selection,
                selectionArgs,
                null,null,sortOrder);
        //places a watch on the caller's content resolver such that
        // if the data changes and the caller has a registered change watcher, they'll be notified.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;


    }


    @Override
    public String getType( Uri uri) {
        return null;
    }


    @Override
    public Uri insert( Uri uri, ContentValues values) {

        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();

        long id = 0;
        //   id = sqlDB.insert(CountryTableHandler.CountryTable, null, values);
//
        switch(uriType) {
            case Fittracker:// insert INSERT INTO table-name (column-names) VALUES (values)
                id = sqlDB.insert(FittrackerTableHandler.FittrackerTable, null, values);
                break;
            case Fittracker_ID:
                id = sqlDB.insert(FittrackerTableHandler.FittrackerTable, null, values);
                break;
            default:
                throw new IllegalArgumentException(("Unknown URI: " + null));
        }

        getContext().getContentResolver().notifyChange(uri,null);
        return Uri.parse(BASE_PATH + "/" + id);

    }

    @Override
    public int delete( Uri uri,  String selection,  String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();

        int rowsDeleted = 0;

        switch(uriType) {
            case Fittracker:
                rowsDeleted = sqlDB.delete(FittrackerTableHandler.FittrackerTable,selection,selectionArgs);
                break;
            case Fittracker_ID:
                String id = uri.getLastPathSegment();
                // SELECT statement FROM  and WHERE clauses on the specific Table Column ID
                // that needs to be deleted
                if(TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(FittrackerTableHandler.FittrackerTable,
                            FittrackerTableHandler.FittrackerID + "=" + id, null);
                } else {
                    rowsDeleted = sqlDB.delete(FittrackerTableHandler.FittrackerTable,
                            FittrackerTableHandler.FittrackerID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException(("Unknown URI: " + null));
        }

        getContext().getContentResolver().notifyChange(uri,null);
        return rowsDeleted;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsUpdated = 0;

        switch (uriType) {
            case Fittracker:
                rowsUpdated = sqlDB.update(FittrackerTableHandler.FittrackerTable, values, selection, selectionArgs);
                break;
            case Fittracker_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(FittrackerTableHandler.FittrackerTable, values,
                            FittrackerTableHandler.FittrackerID + "=" + id, null);
                } else {
                    rowsUpdated = sqlDB.update(FittrackerTableHandler.FittrackerTable,
                            values, FittrackerTableHandler.FittrackerID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
