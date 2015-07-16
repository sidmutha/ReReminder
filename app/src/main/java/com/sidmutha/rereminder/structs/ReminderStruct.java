package com.sidmutha.rereminder.structs;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by sid on 7/5/15.
 */
public class ReminderStruct implements Parcelable {

    public int rowid;
    public String message;
    public List<MomentStruct> momentList;
    public int state;

    public ReminderStruct(int rowid, String message, List<MomentStruct> momentList, int state) {
        this.rowid = rowid;
        this.message = message;
        this.momentList = momentList;
        this.state = state;
    }

    public ReminderStruct(int rowid, String message, int state) {
        this.rowid = rowid;
        this.message = message;
        this.state = state;
    }

    private ReminderStruct(Parcel source) {
        this.rowid = source.readInt();
        this.message = source.readString();
        this.state = source.readInt();
        this.momentList = new LinkedList<MomentStruct>();
        source.readTypedList(this.momentList, MomentStruct.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(rowid);
        dest.writeString(message);
        dest.writeInt(state);
        dest.writeTypedList(momentList);
    }

    public static final Parcelable.Creator<ReminderStruct> CREATOR
            = new Parcelable.Creator<ReminderStruct>() {

        @Override
        public ReminderStruct createFromParcel(Parcel source) {
            ReminderStruct r = new ReminderStruct(source);
            return r;
        }

        @Override
        public ReminderStruct[] newArray(int size) {
            return new ReminderStruct[0];
        }
    };

    public ReminderStruct createCopy() {
        ReminderStruct r = new ReminderStruct(rowid, message, state);
        for (MomentStruct m : momentList) {
            r.momentList.add(m);
        }
        return r;
    }
}