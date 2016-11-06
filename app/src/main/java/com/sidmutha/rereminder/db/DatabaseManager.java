package com.sidmutha.rereminder.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sidmutha.rereminder.structs.MomentStruct;
import com.sidmutha.rereminder.structs.ReminderStruct;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by sid on 7/5/15.
 */
public class DatabaseManager {
    static LocalDBHelper dbHelper = null;
    static String TAG = "ReReminder";
    public static List<ReminderStruct> getReminderList(Context context) {
        if (dbHelper == null)
            dbHelper = new LocalDBHelper(context);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(LocalDBHelper.TABLE_REMINDERS, null, null, null, null, null, null);
        c.moveToPosition(-1);

        List<ReminderStruct> remList = new LinkedList<ReminderStruct>();
        while (c.moveToNext()) {
            int rem_rowid = c.getInt(c.getColumnIndex(LocalDBHelper.COLUMN_ROWID));
            String message = c.getString(c.getColumnIndex(LocalDBHelper.COLUMN_MESSAGE));
            int state = c.getInt(c.getColumnIndex(LocalDBHelper.COLUMN_STATE));
            if (state == -1) { // temp deleted, will be visible only in history
                continue;
            }
            Cursor t = db.rawQuery("select * from " + LocalDBHelper.TABLE_TIMES + " where "
                    + LocalDBHelper.COLUMN_RMID + "=?", new String[]{Integer.toString(rem_rowid)});
            t.moveToPosition(-1);

            List<MomentStruct> momList = new LinkedList<MomentStruct>();
            while (t.moveToNext()) {
                int moment = t.getInt(t.getColumnIndex(LocalDBHelper.COLUMN_TIME));
                boolean enabled = t.getInt(t.getColumnIndex(LocalDBHelper.COLUMN_ENABLED)) != 0;
                int mom_rowid = t.getInt(t.getColumnIndex(LocalDBHelper.COLUMN_ROWID));
                MomentStruct m = new MomentStruct(mom_rowid, rem_rowid, moment, enabled);
                momList.add(m);
            }

            t.close();
            ReminderStruct r = new ReminderStruct(rem_rowid, message, momList, state);
            remList.add(r);
        }
        c.close();
        db.close();

        return remList;
    }

    public static ReminderStruct getRemStruct (Context context, int rmid) {
        if (dbHelper == null)
            dbHelper = new LocalDBHelper(context);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from " + LocalDBHelper.TABLE_REMINDERS + " where "
                + LocalDBHelper.COLUMN_ROWID + "=?", new String[]{Integer.toString(rmid)});
        c.moveToPosition(0);

        int rem_rowid = c.getInt(c.getColumnIndex(LocalDBHelper.COLUMN_ROWID));
        String message = c.getString(c.getColumnIndex(LocalDBHelper.COLUMN_MESSAGE));
        int state = c.getInt(c.getColumnIndex(LocalDBHelper.COLUMN_STATE));
        /*if (state == -1) { // temp deleted, will be visible only in history
            continue;
        }*/
        Cursor t = db.rawQuery("select * from " + LocalDBHelper.TABLE_TIMES + " where "
                + LocalDBHelper.COLUMN_RMID + "=?", new String[]{Integer.toString(rem_rowid)});
        t.moveToPosition(-1);

        List<MomentStruct> momList = new LinkedList<MomentStruct>();
        int num_enabled = 0;
        Log.d(TAG, t.getCount() + "");
        while (t.moveToNext()) {
            int moment = t.getInt(t.getColumnIndex(LocalDBHelper.COLUMN_TIME));
            boolean enabled = t.getInt(t.getColumnIndex(LocalDBHelper.COLUMN_ENABLED)) != 0;
            if (enabled)
                num_enabled++;
            int mom_rowid = t.getInt(t.getColumnIndex(LocalDBHelper.COLUMN_ROWID));
            Log.d(TAG, "moment: " + moment + " enabled: " + enabled + " mom_rowid: " + mom_rowid);

            MomentStruct m = new MomentStruct(mom_rowid, rem_rowid, moment, enabled);
            momList.add(m);
        }

        t.close();
        c.close();
        db.close();
        ReminderStruct r;
        r = new ReminderStruct(rem_rowid, message, momList, state);
        r.num_enabled = num_enabled;
        return r;
    }

    public static void setReminderState(Context context, int rmid, int state) {
        if (dbHelper == null)
            dbHelper = new LocalDBHelper(context);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LocalDBHelper.COLUMN_STATE, state);

        db.update(LocalDBHelper.TABLE_REMINDERS, values, LocalDBHelper.COLUMN_ROWID
                + "=?", new String[]{Integer.toString(rmid)});
        db.close();
    }

    public static String getReminderMessage(Context context, int rmid) {
        if (dbHelper == null)
            dbHelper = new LocalDBHelper(context);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from " + LocalDBHelper.TABLE_REMINDERS + " where "
                + LocalDBHelper.COLUMN_ROWID + "=?", new String[]{Integer.toString(rmid)});
        c.moveToPosition(0);
        String message = c.getString(c.getColumnIndex(LocalDBHelper.COLUMN_MESSAGE));
        db.close();
        return message;
    }

    // add data from a MomentStruct to TABLE_TIMES *and* update the rowid field of the MomentStruct
    public static void addToTblTimes(Context context, MomentStruct momentStruct) {
        if (dbHelper == null)
            dbHelper = new LocalDBHelper(context);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LocalDBHelper.COLUMN_RMID, momentStruct.rmid);
        values.put(LocalDBHelper.COLUMN_TIME, momentStruct.moment);
        values.put(LocalDBHelper.COLUMN_ENABLED, momentStruct.enabled);

        int rowid = (int) db.insertOrThrow(LocalDBHelper.TABLE_TIMES, null, values);
        db.close();
        momentStruct.rowid = rowid; // updating rowid
    }

    // update row in TABLE_TIMES according to MomentStruct
    public static void updateTblTimes(Context context, MomentStruct momentStruct) {
        if (dbHelper == null)
            dbHelper = new LocalDBHelper(context);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LocalDBHelper.COLUMN_RMID, momentStruct.rmid);
        values.put(LocalDBHelper.COLUMN_TIME, momentStruct.moment);
        values.put(LocalDBHelper.COLUMN_ENABLED, momentStruct.enabled); // TODO: boolean?

        db.update(LocalDBHelper.TABLE_TIMES, values, LocalDBHelper.COLUMN_ROWID
                + "=?", new String[]{Integer.toString(momentStruct.rowid)});
        db.close();
    }

    // permanent delete Moment
    public static void deleteFromTblTimes(Context context, Integer rowid) {
        if (dbHelper == null)
            dbHelper = new LocalDBHelper(context);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(LocalDBHelper.TABLE_TIMES, LocalDBHelper.COLUMN_ROWID + "=?",
                new String[]{Integer.toString(rowid)});
        db.close();

    }

    public static void deleteFromTblTimes(Context context, MomentStruct momentStruct) {
        deleteFromTblTimes(context, momentStruct.rowid);
    }

    public static void setMomentEnabled(Context context, int momentID, boolean enabled) {
        if (dbHelper == null)
            dbHelper = new LocalDBHelper(context);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LocalDBHelper.COLUMN_ROWID, momentID);
        values.put(LocalDBHelper.COLUMN_ENABLED, enabled); // TODO: boolean?

        db.update(LocalDBHelper.TABLE_TIMES, values, LocalDBHelper.COLUMN_ROWID
                + "=?", new String[]{Integer.toString(momentID)});
        db.close();
    }

    /**
     * **********************************************************************************************
     */

    // add row to TABLE_REMINDERS from a ReminderStruct and return the row id
    public static int addToTblReminders(Context context, ReminderStruct reminderStruct) {
        Log.d(TAG, "addToTblReminders called");
        if (dbHelper == null)
            dbHelper = new LocalDBHelper(context);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LocalDBHelper.COLUMN_MESSAGE, reminderStruct.message);
        values.put(LocalDBHelper.COLUMN_STATE, reminderStruct.state);

        int rowid = (int) db.insertOrThrow(LocalDBHelper.TABLE_REMINDERS, null, values);
        db.close();
        return rowid;
    }

    // update row in TABLE_REMINDERS according to ReminderStruct
    public static void updateTblReminders(Context context, ReminderStruct reminderStruct) {
        if (dbHelper == null)
            dbHelper = new LocalDBHelper(context);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LocalDBHelper.COLUMN_MESSAGE, reminderStruct.message);
        values.put(LocalDBHelper.COLUMN_STATE, reminderStruct.state);

        db.update(LocalDBHelper.TABLE_REMINDERS, values, LocalDBHelper.COLUMN_ROWID
                + "=?", new String[]{Integer.toString(reminderStruct.rowid)});
        db.close();
    }

    // permanent delete Reminder
    public static void deleteFromTblReminders(Context context, ReminderStruct reminderStruct) {
        if (dbHelper == null)
            dbHelper = new LocalDBHelper(context);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(LocalDBHelper.TABLE_REMINDERS, LocalDBHelper.COLUMN_ROWID + "=?",
                new String[]{Integer.toString(reminderStruct.rowid)});
        db.close();
    }

    // add entire ReminderStruct with the MomentStruct to the DB; *DOES NOT ADD MOMENT STRUCTS*
    public static int addToDbReminder(Context context, ReminderStruct reminderStruct) {
        int rowid = addToTblReminders(context, reminderStruct);
        for (MomentStruct m : reminderStruct.momentList) {
            m.rmid = rowid;
            //addToTblTimes(context, m);
        }
        return rowid;
    }

    // update entire ReminderStruct with the MomentStruct in the DB
    public static void updateInDbReminder(Context context, ReminderStruct reminderStruct) {
        updateTblReminders(context, reminderStruct);
        for (MomentStruct m : reminderStruct.momentList) {
            updateTblTimes(context, m);
        }
    }

    // delete entire ReminderStruct with the MomentStruct from the DB
    public static void deleteFromDbReminder(Context context, ReminderStruct reminderStruct) {
        for (MomentStruct m : reminderStruct.momentList) {
            deleteFromTblTimes(context, m);
        }
        deleteFromTblReminders(context, reminderStruct);
    }

}
