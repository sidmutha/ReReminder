package com.sidmutha.rereminder.other;

import com.sidmutha.rereminder.structs.MomentStruct;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by sid on 13/7/15.
 */
public class Constants {
    public static final int WHICH_TODAY = 0;
    public static final int WHICH_TOMORROW = 1;
    public static final int REMINDER_MODIFY = 2;
    public static final int REMINDER_NEW = 3;

    public static int convertToMoment(int day, int month, int year, int hour, int minute) {
        return 0;
    }

    public static Calendar getCalendar(int which) {
        Calendar outCal = new GregorianCalendar();

        switch (which) {
            case Constants.WHICH_TODAY:
                // outCal = outCal;
                break;

            case Constants.WHICH_TOMORROW:
                outCal.add(Calendar.DAY_OF_YEAR, 1);
                break;
        }

        return outCal;
    }

    public static Calendar getCalendar(int day, int month, int year) {
        return new GregorianCalendar(year, month, day);
    }

    public static int getMoment(MomentStruct m) {
        Calendar cal = new GregorianCalendar(m.year, m.month, m.day, m.hour, m.minute);
        return (int) ((int) cal.getTimeInMillis() / 1000.0); // TODO: verify
    }
}
