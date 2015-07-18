package com.sidmutha.rereminder.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.sidmutha.rereminder.db.DatabaseManager;
import com.sidmutha.rereminder.structs.MomentStruct;
import com.sidmutha.rereminder.structs.ReminderStruct;

/**
 * Created by sid on 16/7/15.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "reminder!", Toast.LENGTH_SHORT).show();
        Bundle data = intent.getBundleExtra("data");
        MomentStruct momentStruct = data.getParcelable("momentStruct");
        //ReminderStruct reminderStruct = DatabaseManager.getReminder(context, momentStruct.rmid); // TODO: lookup db for msg
        String message = DatabaseManager.getReminderMessage(context, momentStruct.rmid);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(android.R.drawable.sym_def_app_icon)
                        .setContentTitle(message)
                        .setContentText(momentStruct.moment + "");

        NotificationManager notificationManager
                = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(momentStruct.rowid, builder.build());

        DatabaseManager.setMomentEnabled(context, momentStruct.rowid, false);
    }
}
