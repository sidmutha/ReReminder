package com.sidmutha.rereminder.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.sidmutha.rereminder.structs.MomentStruct;

/**
 * Created by sid on 16/7/15.
 */
public class AlarmHelper {
    static PendingIntent generatePIntent(Context context, int momentID, int flags, Bundle data) {
        Intent intent = new Intent(context, AlarmReceiver.class); // TODO
        intent.putExtra("data", data);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, momentID, intent, flags);
        return pIntent;
    }

    public static void generateAndSetAlarm(Context context, MomentStruct momentStruct) {
        Bundle data = new Bundle();
        data.putParcelable("momentStruct", momentStruct);
        PendingIntent pIntent = generatePIntent(context, momentStruct.rowid, PendingIntent.FLAG_CANCEL_CURRENT, data);

        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, momentStruct.moment, pIntent);
    }

    public static void cancelAlarm(Context context, MomentStruct momentStruct) {
        Intent intent = new Intent(context, AlarmReceiver.class);

        PendingIntent pIntent = PendingIntent.getBroadcast(context, momentStruct.moment, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmMgr.cancel(pIntent);
    }

    public static void cancelAlarm(Context context, int momentID) {
        Intent intent = new Intent(context, AlarmReceiver.class);

        PendingIntent pIntent = PendingIntent.getBroadcast(context, momentID, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmMgr.cancel(pIntent);
    }

    public static void modifyAndSetAlarm(Context context, MomentStruct momentStruct) {
        Bundle data = new Bundle();
        data.putParcelable("momentStruct", momentStruct);
        PendingIntent pIntent = generatePIntent(context, momentStruct.rowid, PendingIntent.FLAG_UPDATE_CURRENT, data);

        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, momentStruct.moment, pIntent);
    }


}
