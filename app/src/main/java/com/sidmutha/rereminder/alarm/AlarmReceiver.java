package com.sidmutha.rereminder.alarm;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.sidmutha.rereminder.R;
import com.sidmutha.rereminder.db.DatabaseManager;
import com.sidmutha.rereminder.structs.MomentStruct;
import com.sidmutha.rereminder.structs.ReminderStruct;

/**
 * Created by sid on 16/7/15.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context, "reminder!", Toast.LENGTH_SHORT).show();
        Bundle data = intent.getBundleExtra("data");
        MomentStruct momentStruct = data.getParcelable("momentStruct");
        ReminderStruct reminderStruct = DatabaseManager.getRemStruct(context, momentStruct.rmid);
        int num_enabled = reminderStruct.num_enabled;
        String contentText;
        if (num_enabled == 1) { // last reminder
            contentText = "No more reminders left";
            DatabaseManager.setReminderState(context, momentStruct.rmid, 0); // disable reminder
        } else if (num_enabled == 2) {
            contentText = "1 reminder left";
        } else {
            contentText = (num_enabled - 1) + " reminders left";
        }
        DatabaseManager.setMomentEnabled(context, momentStruct.rowid, false);

        String message = reminderStruct.message;
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.rereminderbw)
                        .setContentTitle(message)
                        .setTicker("ReReminder: " + message)
                        .setVibrate(new long[] {0, 1000})
                        .setLights(Color.RED, 1000, 1000)
                        .setContentText(contentText)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        NotificationManager notificationManager
                = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(momentStruct.rowid, builder.build());

    }
}
