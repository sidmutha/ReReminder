package com.sidmutha.rereminder.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.sidmutha.rereminder.R;
import com.sidmutha.rereminder.db.LocalDBHelper;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*
        LocalDBHelper dbHelper = new LocalDBHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues remCV = new ContentValues();
        remCV.put(LocalDBHelper.COLUMN_MESSAGE, "fist rem");
        remCV.put(LocalDBHelper.COLUMN_TIMESTAMP, 1436335448);
        remCV.put(LocalDBHelper.COLUMN_STATE, 1);
        long rmid = db.insertOrThrow(LocalDBHelper.TABLE_REMINDERS, null, remCV);
        Log.d("db1",  Long.toString(rmid));

        ContentValues momCV = new ContentValues();
        momCV.put(LocalDBHelper.COLUMN_RMID, rmid);
        momCV.put(LocalDBHelper.COLUMN_TIME, 1436335448);
        momCV.put(LocalDBHelper.COLUMN_ENABLED, 1);
        long rid = db.insertOrThrow(LocalDBHelper.TABLE_TIMES, null, momCV);
        Log.d("db2",  Long.toString(rid));
*/
        startActivity(new Intent(this, ReminderListActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
