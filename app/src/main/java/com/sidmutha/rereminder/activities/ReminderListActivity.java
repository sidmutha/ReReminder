package com.sidmutha.rereminder.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.sidmutha.rereminder.R;
import com.sidmutha.rereminder.adapters.ReminderListAdapter;
import com.sidmutha.rereminder.db.DatabaseManager;
import com.sidmutha.rereminder.other.Constants;
import com.sidmutha.rereminder.structs.MomentStruct;
import com.sidmutha.rereminder.structs.ReminderStruct;

import java.util.List;


public class ReminderListActivity extends ActionBarActivity {

    int currentPosition;
    List<ReminderStruct> reminderStructList;
    ReminderListAdapter rlAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_list);
        Context appContext = getApplicationContext();
        reminderStructList = DatabaseManager.getReminderList(appContext);
        Intent testIntent = new Intent(this, ReminderEditActivity.class);
        //testIntent.putExtra("reminderStruct", new ReminderStruct(1, "Siddhant", 1));
        //testIntent.putExtra("t", new MomentStruct(1, 1, 1, 1, 1, 1, true, 1, 1));
        //startActivity(testIntent);
        rlAdapter = new ReminderListAdapter(this, reminderStructList);

        ListView lv = (ListView) findViewById(R.id.rmlist_listview);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ReminderStruct reminderStruct = (ReminderStruct) parent.getItemAtPosition(position);
                startModifyEditActivity(reminderStruct, position);
            }
        });
        lv.setAdapter(rlAdapter);

        Button btnAdd = (Button) findViewById(R.id.rmlist_btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ReminderEditActivity.class);
                ReminderStruct reminderStruct = new ReminderStruct(-1, null, 1);
                Bundle b1 = new Bundle();
                b1.putInt("requestCode", Constants.REMINDER_NEW);
                Bundle b2 = new Bundle();
                b2.putParcelable("reminderStruct", reminderStruct);
                //intent.putExtra("requestCode", Constants.REMINDER_MODIFY);
                intent.putExtra("bundle1", b1); // workaround for some weird error
                intent.putExtra("bundle2", b2); // workaround for some weird error
                startActivityForResult(intent, Constants.REMINDER_NEW);
            }
        });

    }

    public void startModifyEditActivity(ReminderStruct reminderStruct, int position) {
        currentPosition = position;
        Intent intent = new Intent(getApplicationContext(), ReminderEditActivity.class);
        Bundle b1 = new Bundle();
        b1.putInt("requestCode", Constants.REMINDER_MODIFY);
        Bundle b2 = new Bundle();
        b2.putParcelable("reminderStruct", reminderStruct);
        //intent.putExtra("requestCode", Constants.REMINDER_MODIFY);
        intent.putExtra("bundle1", b1); // workaround for some weird error
        intent.putExtra("bundle2", b2); // workaround for some weird error
        //intent.putExtra("reminderStruct", reminderStruct);

        startActivityForResult(intent, Constants.REMINDER_MODIFY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                ReminderStruct rms;
                switch (requestCode) {
                    case Constants.REMINDER_MODIFY:
                        rms = data.getParcelableExtra("reminderStruct");
                        reminderStructList.set(currentPosition, rms);
                        rlAdapter.notifyDataSetChanged();
                        break;

                    case Constants.REMINDER_NEW:
                        rms = data.getParcelableExtra("reminderStruct");
                        reminderStructList.add(rms);
                        rlAdapter.notifyDataSetChanged();
                        break;
                }
            case RESULT_CANCELED:

                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reminder_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.rmlist_action_history) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
