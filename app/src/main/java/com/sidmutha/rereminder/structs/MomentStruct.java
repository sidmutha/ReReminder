package com.sidmutha.rereminder.structs;

import android.os.Parcel;
import android.os.Parcelable;


import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by sid on 7/5/15.
 */
public class MomentStruct implements Parcelable {
    public int moment;
    public int year, month, day, hour, minute;
    public boolean enabled;
    public int rowid, rmid;

    public MomentStruct(int moment, int year, int month, int day, int hour, int minute, boolean enabled, int rowid, int rmid) {
        this.moment = moment;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.enabled = enabled;
        this.rowid = rowid;
        this.rmid = rmid;
    }

    public MomentStruct createCopy() {
        return new MomentStruct(moment, year, month, day, hour, minute, enabled, rowid, rmid);
    }

    public boolean equals(MomentStruct o) {
        return ((moment == o.moment) && (year == o.year) && (month == o.month) &&
                (day == o.day) && (hour == o.hour) && (minute == o.minute) &&
                (enabled == o.enabled) && (rowid == o.rowid) && (rmid == o.rmid));
    }

    public MomentStruct(int rowid, int rmid, int moment, boolean enabled) {
        this.rowid = rowid;
        this.rmid = rmid;
        this.moment = moment;
        this.enabled = enabled;
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTimeInMillis(moment * 1000L); // TODO: need to verify

        this.year = cal.get(Calendar.YEAR);
        this.month = cal.get(Calendar.MONTH);
        this.day = cal.get(Calendar.DAY_OF_MONTH);
        this.hour = cal.get(Calendar.HOUR_OF_DAY);
        this.minute = cal.get(Calendar.MINUTE);
    }

    public MomentStruct(int rowid, int rmid, int year, int month, int day, int hour, int minute, boolean enabled) {
        this.rowid = rowid;
        this.rmid = rmid;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.enabled = enabled;
    }

    private MomentStruct(Parcel source) {
        this.year = source.readInt();
        this.month = source.readInt();
        this.day = source.readInt();
        this.hour = source.readInt();
        this.minute = source.readInt();
        this.moment = source.readInt();
        int e = source.readInt();
        this.enabled = (e != 0);
        this.rowid = source.readInt();
        this.rmid = source.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(year);
        dest.writeInt(month);
        dest.writeInt(day);
        dest.writeInt(hour);
        dest.writeInt(minute);
        dest.writeInt(moment);
        int e = (enabled ? 1 : 0);
        dest.writeInt(e);
        dest.writeInt(rowid);
        dest.writeInt(rmid);
    }

    public static final Parcelable.Creator<MomentStruct> CREATOR
            = new Parcelable.Creator<MomentStruct>() {

        @Override
        public MomentStruct createFromParcel(Parcel source) {
            MomentStruct m = new MomentStruct(source);
            return m;
        }

        @Override
        public MomentStruct[] newArray(int size) {
            return new MomentStruct[size];
        }
    };
}
