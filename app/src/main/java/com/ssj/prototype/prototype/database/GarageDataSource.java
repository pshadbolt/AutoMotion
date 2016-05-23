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
    private String[] allVehicleColumns = {GarageDataOpenHelper.COLUMN_ID, GarageDataOpenHelper.COLUMN_YEAR, GarageDataOpenHelper.COLUMN_MAKE, GarageDataOpenHelper.COLUMN_MODEL, GarageDataOpenHelper.COLUMN_TRIM};
    private String[] allMaintenanceColumns = {GarageDataOpenHelper.COLUMN_VEHICLE_ID, GarageDataOpenHelper.COLUMN_MILEAGE, GarageDataOpenHelper.COLUMN_ACTION, GarageDataOpenHelper.COLUMN_ITEM};

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

    public Vehicle insertVehicle(String year, String make, String model, String style) {
        ContentValues values = new ContentValues();
        values.put(GarageDataOpenHelper.COLUMN_YEAR, year);
        values.put(GarageDataOpenHelper.COLUMN_MAKE, make);
        values.put(GarageDataOpenHelper.COLUMN_MODEL, model);
        values.put(GarageDataOpenHelper.COLUMN_TRIM, style);
        long id = database.insert(GarageDataOpenHelper.TABLE_VEHICLE_NAME, null, values);
        return new Vehicle(id, year, make, model, style);
    }

    public void insertMaintenance(Vehicle vehicle, String mileage, String action, String item) {
        ContentValues values = new ContentValues();
        values.put(GarageDataOpenHelper.COLUMN_VEHICLE_ID, vehicle.getId());
        values.put(GarageDataOpenHelper.COLUMN_MILEAGE, Integer.parseInt(mileage));
        values.put(GarageDataOpenHelper.COLUMN_ACTION, action);
        values.put(GarageDataOpenHelper.COLUMN_ITEM, item);
        database.insert(GarageDataOpenHelper.TABLE_MAINTENANCE_NAME, null, values);
    }

    public void deleteVehicle(long id) {
        database.delete(GarageDataOpenHelper.TABLE_VEHICLE_NAME, GarageDataOpenHelper.COLUMN_ID
                + " = " + id, null);
    }

    public ArrayList<Vehicle> getAllVehicles() {
        Cursor cursor = database.query(GarageDataOpenHelper.TABLE_VEHICLE_NAME, allVehicleColumns, null, null, null, null, null);

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

    public String getAllMaintenance() {
        Cursor cursor = database.query(GarageDataOpenHelper.TABLE_MAINTENANCE_NAME, allMaintenanceColumns, null, null, null, null, null);

        String response = "";
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            response += cursor.getLong(0) + " " + cursor.getString(1) + " " + cursor.getString(2) + " " + cursor.getString(3) + System.getProperty("line.separator");
            cursor.moveToNext();
        }
        cursor.close();

        return response;
    }

    public String getMaintenance(long id) {
        //query(boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
        Cursor cursor = database.query(GarageDataOpenHelper.TABLE_MAINTENANCE_NAME,
                new String[]{GarageDataOpenHelper.COLUMN_MILEAGE, GarageDataOpenHelper.COLUMN_ACTION, GarageDataOpenHelper.COLUMN_ITEM}, GarageDataOpenHelper.COLUMN_VEHICLE_ID + "=\'" + id + "\'", null, null, null, GarageDataOpenHelper.COLUMN_MILEAGE + " ASC", null);

        String response = "";
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            response += cursor.getString(0) + " " + cursor.getString(1) + " " + cursor.getString(2) + System.getProperty("line.separator");
            cursor.moveToNext();
        }
        cursor.close();

        return response;
    }

}
