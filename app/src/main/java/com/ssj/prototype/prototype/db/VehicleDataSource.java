package com.ssj.prototype.prototype.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shadbolt on 5/1/2016.
 */
public class VehicleDataSource {

    // Database fields
    private SQLiteDatabase database;
    private DatabaseOpenHelper dbHelper;
    private String[] allColumns = {DatabaseOpenHelper.COLUMN_YEAR, DatabaseOpenHelper.COLUMN_MAKE, DatabaseOpenHelper.COLUMN_MODEL, DatabaseOpenHelper.COLUMN_TRIM};

    public VehicleDataSource(Context context) {
        dbHelper = new DatabaseOpenHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void insertValue(String year, String make, String model, String trim) {
        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.COLUMN_YEAR, year);
        values.put(DatabaseOpenHelper.COLUMN_MAKE, make);
        values.put(DatabaseOpenHelper.COLUMN_MODEL, model);
        values.put(DatabaseOpenHelper.COLUMN_TRIM, trim);
        long insertId = database.insert(DatabaseOpenHelper.PROGRAM_TABLE_NAME, null, values);
    }

    public void getAllEntries() {
        Cursor cursor = database.query(DatabaseOpenHelper.PROGRAM_TABLE_NAME,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Log.d("INFO", " YEAR: " + cursor.getString(0) + " MAKE: " + cursor.getString(1) + " MODEL: " + cursor.getString(2) + " TRIM: " + cursor.getString(3));
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
    }

    public List<String> getAllYears() {
        List<String> years = new ArrayList<String>();

        Cursor cursor = database.query(true, DatabaseOpenHelper.PROGRAM_TABLE_NAME,
                new String[]{DatabaseOpenHelper.COLUMN_YEAR}, null, null, DatabaseOpenHelper.COLUMN_YEAR, null, DatabaseOpenHelper.COLUMN_YEAR + " DESC", null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            years.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        Log.d("DATA", "YEARS: " + years.toString());
        return years;
    }

    public List<String> getMakes(String year) {
        List<String> makes = new ArrayList<String>();

        Cursor cursor = database.query(true, DatabaseOpenHelper.PROGRAM_TABLE_NAME,
                new String[]{DatabaseOpenHelper.COLUMN_MAKE}, DatabaseOpenHelper.COLUMN_YEAR + "=" + year, null, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            makes.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        Log.d("DATA", "MAKES: " + makes.toString());
        return makes;
    }

    public List<String> getModels(String year, String make) {
        List<String> models = new ArrayList<String>();

        Cursor cursor = database.query(true, DatabaseOpenHelper.PROGRAM_TABLE_NAME,
                new String[]{DatabaseOpenHelper.COLUMN_MODEL}, DatabaseOpenHelper.COLUMN_YEAR + "=\'" + year + "\' AND " + DatabaseOpenHelper.COLUMN_MAKE + "=\'" + make + "\'", null, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            models.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        Log.d("DATA", "MODELS: " + models.toString());
        return models;
    }

    public List<String> getTrims(String year, String make, String model) {
        List<String> trims = new ArrayList<String>();

        Cursor cursor = database.query(true, DatabaseOpenHelper.PROGRAM_TABLE_NAME,
                new String[]{DatabaseOpenHelper.COLUMN_TRIM}, DatabaseOpenHelper.COLUMN_YEAR + "=\'" + year + "\' AND " + DatabaseOpenHelper.COLUMN_MAKE + "=\'" + make + "\' AND " + DatabaseOpenHelper.COLUMN_MODEL + "=\'" + model + "\'", null, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            trims.add(cursor.getString(0));
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        Log.d("DATA", "TRIMS: " + trims.toString());
        return trims;

    }

    // Delete the DB contents
    public void recreate() throws SQLException {
        dbHelper.recreate(database);
    }

    public void close() {
        dbHelper.close();
    }
}
