package com.example.android.fittracker.data;

import android.database.sqlite.SQLiteDatabase;

public class FittrackerTableHandler {
    public static final String FittrackerTable =  "fittracker";
    public static final String FittrackerID =  "_id";
    public static final String Date = "date";
    public static final String Type = "type";

    public static final String Distance = "distance";
    public static final String Duration = "duration";
    public static final String Calorie = "calorie";
    private static final String Create_table = "create table "
            +FittrackerTable
            +"("
            +FittrackerID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            +Date + " text not null,"
            +Type + " text not null,"
            +Distance + " text not null,"
            +Duration + " text not null,"
            +Calorie + " text not null"

            +")";

    public static void onCreate (SQLiteDatabase db){

        db.execSQL(Create_table);
    }

    public static void onUpgrade(SQLiteDatabase db,int oldV, int newV){
        db.execSQL("DROP TABLE IF EXISTS "+FittrackerTable);
        onCreate(db);

    }

}
