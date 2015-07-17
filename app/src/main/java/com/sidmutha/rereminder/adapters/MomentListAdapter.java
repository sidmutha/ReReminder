package com.sidmutha.rereminder.adapters;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.sidmutha.rereminder.R;
import com.sidmutha.rereminder.other.Constants;
import com.sidmutha.rereminder.structs.MomentStruct;

import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by sid on 8/5/15.
 */
public class MomentListAdapter extends BaseAdapter {

    List<Integer> deleteIdList;
    List<MomentStruct> momentStructList;
    Context context;
    LinkedList<String> spDateList = new LinkedList<>(Arrays.asList(new String[]{"Day", "Today", "Tomorrow", "Pick a Date"}));

    public MomentListAdapter(Context context, List<MomentStruct> momentStructList, List<Integer> deleteIdList) {
        super();
        this.momentStructList = momentStructList;
        this.deleteIdList = deleteIdList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return momentStructList.size();
    }

    @Override
    public Object getItem(int position) {
        return momentStructList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = null;
        // TODO: set an initial item in the momentStructList for new reminder
        final MomentStruct ms = (MomentStruct) getItem(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            vi = inflater.inflate(R.layout.momentlist_item, parent, false);

        } else {
            vi = convertView;
        }

        vi.setTag(ms);

        Switch swOnOff = (Switch) vi.findViewById(R.id.mli_swch_onoff);
        swOnOff.setChecked(ms.enabled);
        swOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ms.enabled = isChecked;
            }
        });

        ImageButton btnDelete = (ImageButton) vi.findViewById(R.id.mli_btn_del);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ms.rowid != -1)
                    deleteIdList.add(ms.rowid);
                momentStructList.remove(ms);
                notifyDataSetChanged();
            }
        });

        Calendar finalCal = Constants.getCalendar(Constants.WHICH_TODAY);

        TextView tvDate = (TextView) vi.findViewById(R.id.mli_txt_date);
        String tvDateText;

        if (ms.day != -1)
            tvDateText = ms.day + "/" + (ms.month + 1) + "/" + ms.year;
        else
            tvDateText = "Select Date";

        tvDate.setText(tvDateText);
        tvDate.setOnClickListener(new DateOCL(tvDate, finalCal, ms));


        TextView tvTime = (TextView) vi.findViewById(R.id.mli_txt_time);
        String tvTimeText;

        if (ms.hour != -1)
            tvTimeText = ms.hour + ":" + ms.minute;
        else
            tvTimeText = "Select Time";

        tvTime.setText(tvTimeText);
        tvTime.setOnClickListener(new TimeOCL(tvTime, finalCal, ms));

        

        /*
        final Spinner spDate = (Spinner) vi.findViewById(R.id.mli_spn_date);

        if (ms.moment != -1) {
            spDateList.set(0, ms.day + "/" + ms.month + "/" + ms.year); // TODO: beautify date
        }
        spDate.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, spDateList));

        spDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Calendar cal = null;
                switch (position) {
                    case 0:
                        return;

                    case 1: // Today
                        cal = Constants.getCalendar(Constants.WHICH_TODAY);
                        break;

                    case 2: // Tomorrow
                        cal = Constants.getCalendar(Constants.WHICH_TOMORROW);
                        break;

                    case 3: // Pick a Date
                        cal = Constants.getCalendar(Constants.WHICH_TODAY);
                        final Calendar finalCal = cal;
                        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {


                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                finalCal.set(Calendar.YEAR, year);
                                finalCal.set(Calendar.MONTH, monthOfYear);
                                finalCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                // updateLabel();
                                notify();
                            }
                        };

                        DatePickerDialog dpDialog
                                = new DatePickerDialog(context,
                                dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                                cal.get(Calendar.DAY_OF_MONTH));
                        dpDialog.show();
                        Toast.makeText(context, finalCal.get(Calendar.DAY_OF_MONTH) + "/"
                                + finalCal.get(Calendar.MONTH) + "/"
                                + finalCal.get(Calendar.YEAR), Toast.LENGTH_SHORT).show();


                        break;
                }
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String dispDate = cal.get(Calendar.DAY_OF_MONTH) + "/"
                                    + cal.get(Calendar.MONTH) + "/"
                                    + cal.get(Calendar.YEAR);
                spDateList.set(0, dispDate);
                ((BaseAdapter) parent.getAdapter()).notifyDataSetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
*/
        return vi;
    }

    class DateOCL implements View.OnClickListener {

        TextView tvDate;
        Calendar finalCal;
        MomentStruct momentStruct;
        int _day, _month, _year;

        public DateOCL(TextView tvDate, Calendar finalCal, MomentStruct momentStruct) {
            this.tvDate = tvDate;
            this.finalCal = finalCal;
            this.momentStruct = momentStruct;
        }

        public void onClick(View v) {
            DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {


                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    _day = dayOfMonth;
                    _month = monthOfYear;
                    _year = year;
                    finalCal.set(Calendar.YEAR, year);
                    finalCal.set(Calendar.MONTH, monthOfYear);
                    finalCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateTV();

                }
            };

            DatePickerDialog dpDialog
                    = new DatePickerDialog(context,
                    dateSetListener, finalCal.get(Calendar.YEAR), finalCal.get(Calendar.MONTH),
                    finalCal.get(Calendar.DAY_OF_MONTH));
            dpDialog.show();

        }

        private void updateTV() {
            tvDate.setText(_day + "/" + _month + "/" + _year);
            momentStruct.day = _day;
            momentStruct.month = _month;
            momentStruct.year = _year;
        }
    }

    private class TimeOCL implements View.OnClickListener {

        TextView tvTime;
        Calendar finalCal;
        MomentStruct momentStruct;
        int _hour, _minute;

        public TimeOCL(TextView tvTime, Calendar finalCal, MomentStruct momentStruct) {
            this.finalCal = finalCal;
            this.tvTime = tvTime;
            this.momentStruct = momentStruct;
        }

        @Override
        public void onClick(View v) {
            TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    _hour = hourOfDay;
                    _minute = minute;

                    finalCal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    finalCal.set(Calendar.MINUTE, minute);
                    updateTV();
                }
            };

            TimePickerDialog tpDialog = new TimePickerDialog(context, timeSetListener,
                    finalCal.get(Calendar.HOUR_OF_DAY), finalCal.get(Calendar.MINUTE), false);
            tpDialog.show();
        }

        private void updateTV() {
            tvTime.setText(_hour + ":" + _minute);
            momentStruct.hour = _hour;
            momentStruct.minute = _minute;
        }
    }
}















