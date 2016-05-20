package com.ssj.prototype.prototype.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.ssj.prototype.prototype.model.Vehicle;

import java.util.ArrayList;

/**
 * Created by shadbolt on 5/14/2016.
 */
public class GarageDataSource {

    // Database fields
    private SQLiteDatabase database;
    private GarageDataOpenHelper dbHelper;
    private String[] allColumns = {GarageDataOpenHelper.COLUMN_ID, GarageDataOpenHelper.COLUMN_YEAR, GarageDataOpenHelper.COLUMN_MAKE, GarageDataOpenHelper.COLUMN_MODEL, GarageDataOpenHelper.COLUMN_TRIM};

    public GarageDataSource(Context context) {
        dbHelper = new GarageDataOpenHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void drop() throws SQLException {
        dbHelper.drop(database);
    }

    public void insertVehicle(Vehicle vehicle) {
        ContentValues values = new ContentValues();
        values.put(GarageDataOpenHelper.COLUMN_YEAR, vehicle.getYear());
        values.put(GarageDataOpenHelper.COLUMN_MAKE, vehicle.getMake());
        values.put(GarageDataOpenHelper.COLUMN_MODEL, vehicle.getModel());
        values.put(GarageDataOpenHelper.COLUMN_TRIM, vehicle.getStyle());
        long insertId = database.insert(GarageDataOpenHelper.GARAGE_TABLE, null, values);
    }

    public void deleteVehicle(long id) {
        database.delete(GarageDataOpenHelper.GARAGE_TABLE, GarageDataOpenHelper.COLUMN_ID
                + " = " + id, null);
    }

    public ArrayList<Vehicle> getAllEntries() {
        Cursor cursor = database.query(GarageDataOpenHelper.GARAGE_TABLE, allColumns, null, null, null, null, null);

        ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Vehicle vehicle = new Vehicle(cursor);
            vehicles.add(vehicle);
            cursor.moveToNext();
        }
        cursor.close();

        return vehicles;
    }

}
