package com.sidmutha.rereminder.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.sidmutha.rereminder.R;
import com.sidmutha.rereminder.adapters.MomentListAdapter;
import com.sidmutha.rereminder.other.Constants;
import com.sidmutha.rereminder.other.XGod;
import com.sidmutha.rereminder.structs.MomentStruct;
import com.sidmutha.rereminder.structs.ReminderStruct;

import java.util.ArrayList;
import java.util.List;


public class ReminderEditActivity extends ActionBarActivity {

    int requestCode;
    ReminderStruct incReminderStruct;
    ReminderStruct incReminderStructCopy = null;
    List<MomentStruct> addList, deleteList, updateList;
    private ArrayList<Integer> deleteIdList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminderedit);

        final Intent incomingIntent = getIntent();
        //MomentStruct m = incomingIntent.getParcelableExtra("t");

        Bundle incBundle1 = incomingIntent.getBundleExtra("bundle1");
        requestCode = incBundle1.getInt("requestCode");
        //int requestCode = incomingIntent.getIntExtra("requestCode", -1);
        Bundle incBundle2 = incomingIntent.getBundleExtra("bundle2");
        incReminderStruct = incBundle2.getParcelable("reminderStruct");
        incReminderStructCopy = incReminderStruct.createCopy();
        if (requestCode == Constants.REMINDER_NEW) {
            // ensuring the presence of an initial entry in the momentlist
            incReminderStruct.momentList.add(new MomentStruct(-1, -1, -1, -1, -1, -1, true, -1, -1));
        }


        final EditText et_msg = (EditText) findViewById(R.id.rmed_message);
        et_msg.setText(incReminderStruct.message);

        deleteIdList = new ArrayList<>();
        final MomentListAdapter mlAdapter = new MomentListAdapter(this, incReminderStruct.momentList, deleteIdList);
        ListView momLV = (ListView) findViewById(R.id.rmed_listview);
        momLV.setAdapter(mlAdapter);

        final Switch sw = (Switch) findViewById(R.id.rmed_sw_enabled);
        boolean enabled = (incReminderStruct.state == 1);
        sw.setChecked(enabled);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //incReminderStruct.state = isChecked ? 1 : 0;
            }
        });

        Button btnAddMoment = (Button) findViewById(R.id.rmed_btn_add);
        btnAddMoment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incReminderStruct.momentList.add(new MomentStruct(-1, -1, -1, -1, -1, -1, true, -1, -1));
                mlAdapter.notifyDataSetChanged();
            }
        });

        Button btnSave = (Button) findViewById(R.id.rmed_btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sw.isChecked()) { // TODO: add new, enable old, disable old, disable new
                    // tough!
                }


                EditText etMessage = (EditText) findViewById(R.id.rmed_message);
                Editable message = etMessage.getText();
                if (message.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please set a message", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    incReminderStruct.message = message.toString();
                }


                if (incReminderStruct.momentList.size() == 0) {
                    Toast.makeText(getApplicationContext(), "Please set at least one time", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (MomentStruct m : incReminderStruct.momentList) {
                    if (m.day == -1 || m.hour == -1) {
                        Toast.makeText(getApplicationContext(), "Please set proper time(s)", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    m.moment = Constants.getMoment(m);
                }
                if (requestCode == Constants.REMINDER_NEW) {
                    XGod.addNewAlarmReminder(getApplicationContext(), incReminderStruct); // add to db and set
                } else {
                    doGodsWork();
                }


                Intent returnIntent = new Intent();
                returnIntent.putExtra("reminderStruct", incReminderStruct);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        Button btnCancel = (Button) findViewById(R.id.rmed_btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        // TODO: account for the back button
    }

    private void doGodsWork() {
        //addList = new LinkedList<>();
        //deleteList = new LinkedList<>();
        //updateList = new LinkedList<>();

        List<MomentStruct> origMomList = incReminderStructCopy.momentList;
        List<MomentStruct> newMomList = incReminderStruct.momentList;
        Context context = getApplicationContext();

        for (MomentStruct m1 : newMomList) {
            if (m1.rowid == -1) {
                // add new
                XGod.addNewAlarm(context, m1);
                continue;
            }

            for (MomentStruct m2 : origMomList) {
                if (m1.rowid == m2.rowid) {
                    // update or no-change
                    if (m1.moment != m2.moment || m1.enabled != m2.enabled) {
                        // update (cancel if only 'enabled' is different)
                        XGod.updateAlarm(context, m1);
                        break;
                    } else {
                        // nochange
                        break; // move on to next entry
                    }
                }
            }
        }

        for (Integer rid : deleteIdList) {
            XGod.deleteId(context, rid);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reminder_edit, menu);
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
