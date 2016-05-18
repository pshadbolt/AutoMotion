package com.ssj.prototype.prototype.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by shadbolt on 5/1/2016.
 */
public class VehicleDataOpenHelper extends SQLiteOpenHelper {

    public static final String VEHICLES_TABLE = "vehicles";

    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_MAKE = "make";
    public static final String COLUMN_MODEL = "model";
    public static final String COLUMN_TRIM = "trim";

    private static final String DATABASE_NAME = "vehicles.db";
    private static final int DATABASE_VERSION = 1;

    private static final String PROGRAM_TABLE_CREATE = "CREATE TABLE " + VEHICLES_TABLE
            + " (" + COLUMN_YEAR + " TEXT,"
            + COLUMN_MAKE + " TEXT,"
            + COLUMN_MODEL + " TEXT,"
            + COLUMN_TRIM + " TEXT"
            + ");";


    private static final String PROGRAM_TABLE_DROP = "DROP TABLE IF EXISTS " + VEHICLES_TABLE;

    VehicleDataOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PROGRAM_TABLE_CREATE);
    }

    public void drop(SQLiteDatabase db) {
        db.execSQL(PROGRAM_TABLE_DROP);
        db.execSQL(PROGRAM_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(VehicleDataOpenHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL(PROGRAM_TABLE_DROP);
        onCreate(db);
    }
}
