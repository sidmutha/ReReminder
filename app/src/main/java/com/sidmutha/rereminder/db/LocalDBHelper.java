package com.sidmutha.rereminder.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocalDBHelper extends SQLiteOpenHelper {

    // reminder table
    public static final String TABLE_REMINDERS = "reminder";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_MESSAGE = "message";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_ROWID = "_rowid_";
    public static final String COLUMN_STATE = "state"; // 1: enabled; 0: disabled; -1: temporary deleted (history)
    //public static final String COLUMN_MARK = "mark";

    private static final String DATABASE_NAME = "rereminder.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String RMTABLE_CREATE = "create table "
            + TABLE_REMINDERS
            + "("
            + COLUMN_ROWID + " integer primary key autoincrement, "
            + COLUMN_MESSAGE + " text, "
            + COLUMN_STATE + " integer, "
            + COLUMN_TIMESTAMP + " integer "
            + ");";

    public static final String TABLE_TIMES = "times";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_RMID = "rmid";
    public static final String COLUMN_ENABLED = "enabled";

    // time table
    // TODO: add enabled field to each time
    private static final String TTABLE_CREATE = "create table "
            + TABLE_TIMES
            + "("
            + COLUMN_ROWID + " integer primary key autoincrement, "
            + COLUMN_TIME + " integer, "
            + COLUMN_ENABLED + " integer, "
            + COLUMN_RMID + " integer, "
            + "foreign key (" + COLUMN_RMID + ") references " + TABLE_REMINDERS + "(" + COLUMN_ROWID + ")"
            + ");";

    public LocalDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        // TODO
        database.setForeignKeyConstraintsEnabled(true);
        database.execSQL(RMTABLE_CREATE);
        database.execSQL(TTABLE_CREATE);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Log.w(XSQLiteHelper.class.getName(),
        // "Upgrading database from version " + oldVersion + " to "
        // + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIMES);
        onCreate(db);
    }

}