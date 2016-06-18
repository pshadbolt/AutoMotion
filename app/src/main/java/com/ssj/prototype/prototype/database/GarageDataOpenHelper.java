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
    private static final int DATABASE_VERSION = 10;

    public static final String TABLE_NAME_GARAGE = "garage";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_MAKE = "make";
    public static final String COLUMN_MODEL = "model";
    public static final String COLUMN_STYLE = "style";
    public static final String COLUMN_ENGINE = "engine";
    public static final String COLUMN_TRANSMISSION = "transmission";
    public static final String COLUMN_MILEAGE_TOTAL = "mileageTotal";
    public static final String COLUMN_MILEAGE_ANNUAL = "mileageAnnual";

    public static final String TABLE_NAME_MAINTENANCE = "maintenance";
    public static final String COLUMN_VEHICLE_ID = "_id";
    public static final String COLUMN_ENGINE_CODE = "engineCode";
    public static final String COLUMN_TRANSMISSION_CODE = "transmissionCode";
    public static final String COLUMN_INTERVAL_MILEAGE = "intervalMileage";
    public static final String COLUMN_FREQUENCY = "frequency";
    public static final String COLUMN_ACTION = "action";
    public static final String COLUMN_ITEM = "item";
    public static final String COLUMN_ITEM_DESCRIPTION = "itemDescription";

    private static final String TABLE_CREATE_GARAGE = "CREATE TABLE " + TABLE_NAME_GARAGE
            + " (" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_YEAR + " TEXT,"
            + COLUMN_MAKE + " TEXT,"
            + COLUMN_MODEL + " TEXT,"
            + COLUMN_STYLE + " TEXT,"
            + COLUMN_ENGINE + " TEXT,"
            + COLUMN_TRANSMISSION + " TEXT,"
            + COLUMN_MILEAGE_TOTAL + " integer,"
            + COLUMN_MILEAGE_ANNUAL + " integer"
            + ");";

    private static final String TABLE_CREATE_MAINTENANCE = "CREATE TABLE " + TABLE_NAME_MAINTENANCE
            + " (" + COLUMN_VEHICLE_ID + " integer"
            + "FOREIGN KEY REFERENCES " + TABLE_NAME_GARAGE + "(" + COLUMN_ID + "),"
            + COLUMN_ENGINE_CODE + " TEXT,"
            + COLUMN_TRANSMISSION_CODE + " TEXT,"
            + COLUMN_INTERVAL_MILEAGE + " integer,"
            + COLUMN_FREQUENCY + " integer,"
            + COLUMN_ACTION + " TEXT,"
            + COLUMN_ITEM + " TEXT,"
            + COLUMN_ITEM_DESCRIPTION + " TEXT"
            + ");";

    private static final String TABLE_DROP_GARAGE = "DROP TABLE IF EXISTS " + TABLE_NAME_GARAGE;
    private static final String TABLE_DROP_MAINTENANCE = "DROP TABLE IF EXISTS " + TABLE_NAME_MAINTENANCE;

    GarageDataOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DB", TABLE_CREATE_GARAGE);
        db.execSQL(TABLE_CREATE_GARAGE);
        Log.d("DB", TABLE_CREATE_MAINTENANCE);
        db.execSQL(TABLE_CREATE_MAINTENANCE);
    }

    public void drop(SQLiteDatabase db) {
        db.execSQL(TABLE_DROP_GARAGE);
        db.execSQL(TABLE_DROP_MAINTENANCE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(GarageDataOpenHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        drop(db);
        onCreate(db);
    }
}
