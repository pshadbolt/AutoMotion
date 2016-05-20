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
public class VehicleLocalDataSource {

    // Database fields
    private SQLiteDatabase database;
    private VehicleLocalDataOpenHelper dbHelper;
    private String[] allColumns = {VehicleLocalDataOpenHelper.COLUMN_YEAR, VehicleLocalDataOpenHelper.COLUMN_MAKE, VehicleLocalDataOpenHelper.COLUMN_MODEL, VehicleLocalDataOpenHelper.COLUMN_TRIM};

    public VehicleLocalDataSource(Context context) {
        dbHelper = new VehicleLocalDataOpenHelper(context);
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
        values.put(VehicleLocalDataOpenHelper.COLUMN_YEAR, year);
        values.put(VehicleLocalDataOpenHelper.COLUMN_MAKE, make);
        values.put(VehicleLocalDataOpenHelper.COLUMN_MODEL, model);
        values.put(VehicleLocalDataOpenHelper.COLUMN_TRIM, trim);
        long insertId = database.insert(VehicleLocalDataOpenHelper.VEHICLES_TABLE, null, values);
    }

    public void insertValue(Vehicle vehicle) {
        ContentValues values = new ContentValues();
        values.put(VehicleLocalDataOpenHelper.COLUMN_YEAR, vehicle.getYear());
        values.put(VehicleLocalDataOpenHelper.COLUMN_MAKE, vehicle.getMake());
        values.put(VehicleLocalDataOpenHelper.COLUMN_MODEL, vehicle.getModel());
        values.put(VehicleLocalDataOpenHelper.COLUMN_TRIM, vehicle.getStyle());
        long insertId = database.insert(VehicleLocalDataOpenHelper.VEHICLES_TABLE, null, values);
    }

    public void getAllEntries() {
        Cursor cursor = database.query(VehicleLocalDataOpenHelper.VEHICLES_TABLE,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            cursor.moveToNext();
        }
        cursor.close();
    }

    public List<String> getYears() {
        List<String> years = new ArrayList<String>();
        //query(boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
        Cursor cursor = database.query(true, VehicleLocalDataOpenHelper.VEHICLES_TABLE,
                new String[]{VehicleLocalDataOpenHelper.COLUMN_YEAR}, null, null, VehicleLocalDataOpenHelper.COLUMN_YEAR, null, VehicleLocalDataOpenHelper.COLUMN_YEAR + " DESC", null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            years.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return years;
    }

    public List<String> getYears(String make, String model) {
        List<String> years = new ArrayList<String>();
        //query(boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
        Cursor cursor = database.query(true, VehicleLocalDataOpenHelper.VEHICLES_TABLE,
                new String[]{VehicleLocalDataOpenHelper.COLUMN_YEAR}, VehicleLocalDataOpenHelper.COLUMN_MAKE + "=\'" + make + "\' AND " + VehicleLocalDataOpenHelper.COLUMN_MODEL + "=\'" + model + "\'", null, VehicleLocalDataOpenHelper.COLUMN_YEAR, null, VehicleLocalDataOpenHelper.COLUMN_YEAR + " DESC", null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            years.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return years;
    }

    public List<String> getMakes() {
        List<String> makes = new ArrayList<String>();
        //query(boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
        Cursor cursor = database.query(true, VehicleLocalDataOpenHelper.VEHICLES_TABLE,
                new String[]{VehicleLocalDataOpenHelper.COLUMN_MAKE}, null, null, VehicleLocalDataOpenHelper.COLUMN_MAKE, null, VehicleLocalDataOpenHelper.COLUMN_MAKE + " ASC", null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            makes.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return makes;
    }

    public List<String> getMakes(String year) {
        List<String> makes = new ArrayList<String>();
        //query(boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
        Cursor cursor = database.query(true, VehicleLocalDataOpenHelper.VEHICLES_TABLE,
                new String[]{VehicleLocalDataOpenHelper.COLUMN_MAKE}, VehicleLocalDataOpenHelper.COLUMN_YEAR + "=" + year, null, VehicleLocalDataOpenHelper.COLUMN_MAKE, null, VehicleLocalDataOpenHelper.COLUMN_MAKE + " ASC", null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            makes.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return makes;
    }

    public List<String> getModels(String make) {
        List<String> models = new ArrayList<String>();
        //query(boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
        Cursor cursor = database.query(true, VehicleLocalDataOpenHelper.VEHICLES_TABLE,
                new String[]{VehicleLocalDataOpenHelper.COLUMN_MODEL}, VehicleLocalDataOpenHelper.COLUMN_MAKE + "=\'" + make + "\'", null, VehicleLocalDataOpenHelper.COLUMN_MODEL, null, VehicleLocalDataOpenHelper.COLUMN_MODEL + " ASC", null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            models.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return models;
    }

    public List<String> getModels(String year, String make) {
        List<String> models = new ArrayList<String>();
        //query(boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
        Cursor cursor = database.query(true, VehicleLocalDataOpenHelper.VEHICLES_TABLE,
                new String[]{VehicleLocalDataOpenHelper.COLUMN_MODEL}, VehicleLocalDataOpenHelper.COLUMN_YEAR + "=\'" + year + "\' AND " + VehicleLocalDataOpenHelper.COLUMN_MAKE + "=\'" + make + "\'", null, VehicleLocalDataOpenHelper.COLUMN_MODEL, null, VehicleLocalDataOpenHelper.COLUMN_MODEL + " ASC", null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            models.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return models;
    }

    public List<String> getTrims(String make, String model, String year) {
        List<String> trims = new ArrayList<String>();

        Cursor cursor = database.query(true, VehicleLocalDataOpenHelper.VEHICLES_TABLE,
                new String[]{VehicleLocalDataOpenHelper.COLUMN_TRIM}, VehicleLocalDataOpenHelper.COLUMN_YEAR + "=\'" + year + "\' AND " + VehicleLocalDataOpenHelper.COLUMN_MAKE + "=\'" + make + "\' AND " + VehicleLocalDataOpenHelper.COLUMN_MODEL + "=\'" + model + "\'", null, VehicleLocalDataOpenHelper.COLUMN_TRIM, null, VehicleLocalDataOpenHelper.COLUMN_TRIM + " ASC", null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            trims.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return trims;

    }

}
