package com.ssj.prototype.prototype.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.ssj.prototype.prototype.model.Vehicle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shadbolt on 5/1/2016.
 */
public class VehicleDataSource {

    // Database fields
    private SQLiteDatabase database;
    private VehicleDataOpenHelper dbHelper;
    private String[] allColumns = {VehicleDataOpenHelper.COLUMN_YEAR, VehicleDataOpenHelper.COLUMN_MAKE, VehicleDataOpenHelper.COLUMN_MODEL, VehicleDataOpenHelper.COLUMN_TRIM};

    public VehicleDataSource(Context context) {
        dbHelper = new VehicleDataOpenHelper(context);
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

    public void insertValue(String year, String make, String model, String trim) {
        ContentValues values = new ContentValues();
        values.put(VehicleDataOpenHelper.COLUMN_YEAR, year);
        values.put(VehicleDataOpenHelper.COLUMN_MAKE, make);
        values.put(VehicleDataOpenHelper.COLUMN_MODEL, model);
        values.put(VehicleDataOpenHelper.COLUMN_TRIM, trim);
        long insertId = database.insert(VehicleDataOpenHelper.VEHICLES_TABLE, null, values);
    }

    public void insertValue(Vehicle vehicle) {
        ContentValues values = new ContentValues();
        values.put(VehicleDataOpenHelper.COLUMN_YEAR, vehicle.getYear());
        values.put(VehicleDataOpenHelper.COLUMN_MAKE, vehicle.getMake());
        values.put(VehicleDataOpenHelper.COLUMN_MODEL, vehicle.getModel());
        values.put(VehicleDataOpenHelper.COLUMN_TRIM, vehicle.getTrim());
        long insertId = database.insert(VehicleDataOpenHelper.VEHICLES_TABLE, null, values);
    }

    public void getAllEntries() {
        Cursor cursor = database.query(VehicleDataOpenHelper.VEHICLES_TABLE,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            cursor.moveToNext();
        }
        cursor.close();
    }

    public List<String> getAllYears() {
        List<String> years = new ArrayList<String>();
        //query(boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
        Cursor cursor = database.query(true, VehicleDataOpenHelper.VEHICLES_TABLE,
                new String[]{VehicleDataOpenHelper.COLUMN_YEAR}, null, null, VehicleDataOpenHelper.COLUMN_YEAR, null, VehicleDataOpenHelper.COLUMN_YEAR + " DESC", null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            years.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return years;
    }

    public List<String> getMakes(String year) {
        List<String> makes = new ArrayList<String>();
        //query(boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
        Cursor cursor = database.query(true, VehicleDataOpenHelper.VEHICLES_TABLE,
                new String[]{VehicleDataOpenHelper.COLUMN_MAKE}, VehicleDataOpenHelper.COLUMN_YEAR + "=" + year, null, VehicleDataOpenHelper.COLUMN_MAKE, null, VehicleDataOpenHelper.COLUMN_MAKE + " ASC", null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            makes.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return makes;
    }

    public List<String> getModels(String year, String make) {
        List<String> models = new ArrayList<String>();
        //query(boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
        Cursor cursor = database.query(true, VehicleDataOpenHelper.VEHICLES_TABLE,
                new String[]{VehicleDataOpenHelper.COLUMN_MODEL}, VehicleDataOpenHelper.COLUMN_YEAR + "=\'" + year + "\' AND " + VehicleDataOpenHelper.COLUMN_MAKE + "=\'" + make + "\'", null, VehicleDataOpenHelper.COLUMN_MODEL, null, VehicleDataOpenHelper.COLUMN_MODEL + " ASC", null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            models.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return models;
    }

    public List<String> getTrims(String year, String make, String model) {
        List<String> trims = new ArrayList<String>();

        Cursor cursor = database.query(true, VehicleDataOpenHelper.VEHICLES_TABLE,
                new String[]{VehicleDataOpenHelper.COLUMN_TRIM}, VehicleDataOpenHelper.COLUMN_YEAR + "=\'" + year + "\' AND " + VehicleDataOpenHelper.COLUMN_MAKE + "=\'" + make + "\' AND " + VehicleDataOpenHelper.COLUMN_MODEL + "=\'" + model + "\'", null, VehicleDataOpenHelper.COLUMN_TRIM, null, VehicleDataOpenHelper.COLUMN_TRIM + " ASC", null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            trims.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return trims;

    }

}
