package com.ssj.prototype.prototype.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ssj.prototype.prototype.model.Vehicle;

import java.util.ArrayList;

/**
 * Created by shadbolt on 5/14/2016.
 */
public class GarageDataSource {

    // Database fields
    private SQLiteDatabase database;
    private GarageDataOpenHelper dbHelper;
    private String[] allColumns = {GarageDataOpenHelper.COLUMN_YEAR, GarageDataOpenHelper.COLUMN_MAKE, GarageDataOpenHelper.COLUMN_MODEL, GarageDataOpenHelper.COLUMN_TRIM};

    public GarageDataSource(Context context) {
        dbHelper = new GarageDataOpenHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void insertValue(Vehicle vehicle) {
        ContentValues values = new ContentValues();
        values.put(GarageDataOpenHelper.COLUMN_YEAR, vehicle.getYear());
        values.put(GarageDataOpenHelper.COLUMN_MAKE, vehicle.getMake());
        values.put(GarageDataOpenHelper.COLUMN_MODEL, vehicle.getModel());
        values.put(GarageDataOpenHelper.COLUMN_TRIM, vehicle.getTrim());
        long insertId = database.insert(GarageDataOpenHelper.PROGRAM_TABLE_NAME, null, values);
    }

    public ArrayList<String> getAllEntries() {
        Cursor cursor = database.query(GarageDataOpenHelper.PROGRAM_TABLE_NAME, allColumns, null, null, null, null, null);

        ArrayList<String> vehicles = new ArrayList<String>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Vehicle vehicle = new Vehicle(cursor);
            vehicles.add(vehicle.toString());
            cursor.moveToNext();
        }
        cursor.close();

        return vehicles;
    }
}
