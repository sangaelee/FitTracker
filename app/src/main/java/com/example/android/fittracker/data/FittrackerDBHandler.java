package com.example.android.fittracker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FittrackerDBHandler extends SQLiteOpenHelper {

    private static final String DBName = "fittracker.db";
    private static final int DBVersion = 1;

    public FittrackerDBHandler(Context context){
        super(context,DBName,null,DBVersion);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        FittrackerTableHandler.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        FittrackerTableHandler.onUpgrade(db,oldVersion,newVersion);
    }
}