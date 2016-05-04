package com.ssj.prototype.prototype.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by shadbolt on 5/1/2016.
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {

    public static final String PROGRAM_TABLE_NAME = "vehicles";

    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_MAKE = "make";
    public static final String COLUMN_MODEL = "model";
    public static final String COLUMN_TRIM = "trim";

    private static final String DATABASE_NAME = "program.db";
    private static final int DATABASE_VERSION = 1;

    private static final String PROGRAM_TABLE_CREATE = "CREATE TABLE " + PROGRAM_TABLE_NAME
            + " (" +COLUMN_YEAR + " INTEGER,"
            +COLUMN_MAKE + " TEXT NOT NULL,"
            +COLUMN_MODEL + " TEXT NOT NULL,"
            +COLUMN_TRIM + " TEXT NOT NULL"
            +");";

    private static final String PROGRAM_TABLE_DROP = "DROP TABLE IF EXISTS " + PROGRAM_TABLE_NAME;

    DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PROGRAM_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DatabaseOpenHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL(PROGRAM_TABLE_DROP);
        onCreate(db);
    }
}
