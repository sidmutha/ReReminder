package com.sidmutha.rereminder.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.util.Log;

import com.sidmutha.rereminder.R;
import com.sidmutha.rereminder.activities.ReminderListActivity;
import com.sidmutha.rereminder.other.XGod;
import com.sidmutha.rereminder.structs.MomentStruct;
import com.sidmutha.rereminder.structs.ReminderStruct;

import java.util.List;

/**
 * Created by sid on 7/5/15.
 */
public class ReminderListAdapter extends BaseAdapter {
    Context context;
    List<ReminderStruct> reminderStructList;

    public ReminderListAdapter(Context context, List<ReminderStruct> reminderStructList) {
        super();
        this.context = context;
        this.reminderStructList = reminderStructList;
    }

    @Override
    public int getCount() {
        return reminderStructList.size();
    }

    @Override
    public Object getItem(int position) {
        return reminderStructList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View vi = null;
        final ReminderStruct rs = (ReminderStruct) getItem(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            vi = inflater.inflate(R.layout.reminderlist_item, parent, false);
            //Log.d("siddvi", "ddd");
        } else {
            vi = convertView;
        }

        TextView tv_msg = (TextView) vi.findViewById(R.id.rli_txt_msg);
        tv_msg.setText(rs.message);

        final Switch swEnabled = (Switch) vi.findViewById(R.id.rli_swch_onoff);
        swEnabled.setChecked(rs.state == 1);
        swEnabled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!swEnabled.isChecked()) {
                    rs.state = 0;
                    //swEnabled.setChecked(false);
                    XGod.disableReminder(context, rs);
                } else {
                    ((ReminderListActivity) context).startModifyEditActivity(rs, position);
                    swEnabled.setChecked(true);
                }
            }
        });
        /*swEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                rs.state = isChecked ? 1 : 0;
                if (!isChecked) {
                    XGod.disableReminder(context, rs);
                } else {

                }
                // REM: enable/disable entire reminder
            }
        });*/

        ImageButton btnDel = (ImageButton) vi.findViewById(R.id.rli_btn_del);
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rs.state = -1;
                reminderStructList.remove(rs);
                // DB: update state in db
                XGod.tempDeleteReminder(context, rs);
                notifyDataSetChanged();
            }
        });

        TextView tv_dt = (TextView) vi.findViewById(R.id.rli_txt_dt);

        List<MomentStruct> momList = rs.momentList;
        if (momList.size() > 1) {
            tv_dt.setText("Multiple");
        } else if (momList.size() > 0){ // skipping 0 (for testing?)
            MomentStruct ms = momList.get(0);
            tv_dt.setText(ms.day + "/" + ms.month + "/" + ms.year + " " + ms.hour + ":" + ms.minute);
        }

        return vi;
    }
}
