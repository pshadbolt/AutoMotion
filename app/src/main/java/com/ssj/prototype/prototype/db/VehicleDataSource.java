package com.ssj.prototype.prototype.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

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

    public void insertValue(int year, String make, String model, String trim) {
        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.COLUMN_YEAR, year);
        values.put(DatabaseOpenHelper.COLUMN_MAKE, make);
        values.put(DatabaseOpenHelper.COLUMN_MODEL, model);
        values.put(DatabaseOpenHelper.COLUMN_TRIM, trim);

        long insertId = database.insert(DatabaseOpenHelper.PROGRAM_TABLE_NAME, null, values);
    }

    public String getAllEntries() {
        Cursor cursor = database.query(DatabaseOpenHelper.PROGRAM_TABLE_NAME,
                allColumns, null, null, null, null, null);

        String output="";
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            output+=" YEAR: "+cursor.getLong(0);
            output+=" MAKE: "+cursor.getString(1);
            output+=" MODEL: "+cursor.getString(2);
            output+=" TRIM: "+cursor.getString(3);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();

        return output;
    }

    public void close() {
        dbHelper.close();
    }
}
