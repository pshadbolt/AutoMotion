package com.ssj.prototype.prototype.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by shadbolt on 5/14/2016.
 */
public class GarageDataOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "garage.db";
    private static final int DATABASE_VERSION = 7;

    public static final String TABLE_VEHICLE_NAME = "garage";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_MAKE = "make";
    public static final String COLUMN_MODEL = "model";
    public static final String COLUMN_TRIM = "trim";

    public static final String TABLE_MAINTENANCE_NAME = "maintenance";
    public static final String COLUMN_VEHICLE_ID = "_id";
    public static final String COLUMN_MILEAGE = "intervalMileage";
    public static final String COLUMN_ACTION = "action";
    public static final String COLUMN_ITEM = "item";

    private static final String TABLE_VEHICLE_CREATE = "CREATE TABLE " + TABLE_VEHICLE_NAME
            + " (" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_YEAR + " TEXT,"
            + COLUMN_MAKE + " TEXT,"
            + COLUMN_MODEL + " TEXT,"
            + COLUMN_TRIM + " TEXT"
            + ");";

    private static final String TABLE_MAINTENANCE_CREATE = "CREATE TABLE " + TABLE_MAINTENANCE_NAME
            + " (" + COLUMN_VEHICLE_ID + " integer"
            + "FOREIGN KEY REFERENCES " + TABLE_VEHICLE_NAME + "(" + COLUMN_ID + "),"
            + COLUMN_MILEAGE + " integer,"
            + COLUMN_ACTION + " TEXT,"
            + COLUMN_ITEM + " TEXT"
            + ");";

    private static final String TABLE_VEHICLE_DROP = "DROP TABLE IF EXISTS " + TABLE_VEHICLE_NAME;
    private static final String TABLE_MAINTENANCE_DROP = "DROP TABLE IF EXISTS " + TABLE_MAINTENANCE_NAME;

    GarageDataOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DB", TABLE_VEHICLE_CREATE);
        db.execSQL(TABLE_VEHICLE_CREATE);
        Log.d("DB", TABLE_MAINTENANCE_CREATE);
        db.execSQL(TABLE_MAINTENANCE_CREATE);
    }

    public void drop(SQLiteDatabase db) {
        db.execSQL(TABLE_VEHICLE_DROP);
        db.execSQL(TABLE_MAINTENANCE_DROP);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(VehicleLocalDataOpenHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        drop(db);
        onCreate(db);
    }
}
