package com.sidmutha.rereminder.other;

import android.content.Context;

import com.sidmutha.rereminder.alarm.AlarmHelper;
import com.sidmutha.rereminder.db.DatabaseManager;
import com.sidmutha.rereminder.structs.MomentStruct;
import com.sidmutha.rereminder.structs.ReminderStruct;

/**
 * Created by sid on 16/7/15.
 * Methods to add, update, cancel Moments/Reminders and also set alarms accordingly
 * alarm: system alarm (pending intent and stuff)
 */
public class XGod {
    // add new moment to db and set alarm
    public static void addNewAlarm(Context context, MomentStruct momentStruct) {
        DatabaseManager.addToTblTimes(context, momentStruct);
        if (momentStruct.enabled)
            AlarmHelper.generateAndSetAlarm(context, momentStruct);
    }

    // update moment in db and also reshedule or cancel alarm
    public static void updateAlarm(Context context, MomentStruct momentStruct) {
        DatabaseManager.updateTblTimes(context, momentStruct);
        if (momentStruct.enabled) {
            AlarmHelper.modifyAndSetAlarm(context, momentStruct);
        } else {
            AlarmHelper.cancelAlarm(context, momentStruct);
        }
    }

    // change enabled to false in DB *and* the momentStruct
    public static void cancelAlarm(Context context, MomentStruct momentStruct) {
        momentStruct.enabled = false; // set enabled in momentStruct to false
        DatabaseManager.updateTblTimes(context, momentStruct);
        AlarmHelper.cancelAlarm(context, momentStruct);
    }

    // simply cancel in db and alarm; no momentStruct needed
    public static void cancelAlarm(Context context, int momentID) {
        DatabaseManager.setMomentEnabled(context, momentID, false);
        AlarmHelper.cancelAlarm(context, momentID);
    }

    // add entire reminder along with moments to db and also set alarms
    public static void addNewAlarmReminder(Context context, ReminderStruct reminderStruct) {
        DatabaseManager.addToDbReminder(context, reminderStruct);
        for (MomentStruct m : reminderStruct.momentList) {
            addNewAlarm(context, m);
        }
    }

    // delete moment from db and cancel corresponding alarm
    public static void deleteId(Context context, Integer rid) {
        DatabaseManager.deleteFromTblTimes(context, rid);
        AlarmHelper.cancelAlarm(context, rid);
    }

    // disable all the moments in a reminder in db along with their alarms
    public static void disableMomentsReminder(Context context, ReminderStruct reminderStruct) {
        for (MomentStruct m : reminderStruct.momentList) {
            cancelAlarm(context, m); // cancel alarm and disable in db
        }
    }

    // disable the reminder; set state to 0 and disable all moments
    public static void disableReminder(Context context, ReminderStruct reminderStruct) {
        reminderStruct.state = 0;
        disableMomentsReminder(context, reminderStruct); // cancel and disable moments
        DatabaseManager.updateTblReminders(context, reminderStruct); // update (disable) in db
    }

    // disable the reminder; set state to -1 and disable all moments
    public static void tempDeleteReminder(Context context, ReminderStruct reminderStruct) {
        reminderStruct.state = -1;
        disableMomentsReminder(context, reminderStruct); // cancel and disable moments
        DatabaseManager.updateTblReminders(context, reminderStruct); // update (disable) in db
    }
}
