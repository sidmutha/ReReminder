package com.sidmutha.rereminder.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.sidmutha.rereminder.R;

import java.util.Calendar;
import java.util.GregorianCalendar;


public class DateTimePickerActivity extends ActionBarActivity {

    Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_time_picker);
        final Intent incomingIntent = getIntent();
        int moment = incomingIntent.getIntExtra("moment", 0);
        cal = GregorianCalendar.getInstance();
        cal.setTimeInMillis(moment * 1000); // TODO: need to verify

        //final DatePicker dp = (DatePicker) findViewById(R.id.dtp_datePicker);
        final CalendarView calV = (CalendarView) findViewById(R.id.dtp_calendar);
        final TimePicker tp = (TimePicker) findViewById(R.id.dtp_timePicker);

        // set the current values of calV and tp according to incoming moment value
        //calV.setDate(moment * 1000);
        tp.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
        tp.setCurrentMinute(cal.get(Calendar.MINUTE));

        Button btnDone = (Button) findViewById(R.id.dtp_btn_done);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int date = (int) (calV.getDate() / 1000); // TODO: verify
                int hour = tp.getCurrentHour();
                int minute = tp.getCurrentMinute();

                int moment = getMoment(date, hour, minute);
                Log.d("sidd-m", moment + "");
                Intent returnIntent = new Intent();
                returnIntent.putExtra("moment", moment);
                setResult(1, returnIntent);
                finish();
            }
        });

        Button btnCancel = (Button) findViewById(R.id.dtp_btn_cancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                finish();

            }
        });


    }

    static int getMoment(int date, int hour, int minute) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(date * 1000L);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        return (int) (cal.getTimeInMillis() / 1000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_date_time_picker, menu);
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
