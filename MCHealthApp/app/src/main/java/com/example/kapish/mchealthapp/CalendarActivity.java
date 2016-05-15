package com.example.kapish.mchealthapp;

import java.util.GregorianCalendar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;


public class CalendarActivity extends Activity {
    public GregorianCalendar cal_month, cal_month_copy;
    private CalendarAdapter cal_adapter;
    private TextView tv_month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        cal_month = (GregorianCalendar) GregorianCalendar.getInstance();
        cal_month_copy = (GregorianCalendar) cal_month.clone();
        cal_adapter = new CalendarAdapter(this, cal_month,CalendarCollection.date_collection_arr);
        tv_month = (TextView) findViewById(R.id.tv_month);
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));
        ImageButton previous = (ImageButton) findViewById(R.id.ib_prev);

        previous.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setPreviousMonth();
                refreshCalendar();
            }
        });

        ImageButton next = (ImageButton) findViewById(R.id.Ib_next);
        next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setNextMonth();
                refreshCalendar();
            }
        });


        GridView gridview = (GridView) findViewById(R.id.gv_calendar);
        gridview.setAdapter(cal_adapter);
        gridview.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                ((CalendarAdapter) parent.getAdapter()).setSelected(v,position);
                String selectedGridDate = CalendarAdapter.day_string
                        .get(position);

                String[] separatedTime = selectedGridDate.split("-");
                String gridvalueString = separatedTime[2].replaceFirst("^0*","");
                int gridvalue = Integer.parseInt(gridvalueString);

                if ((gridvalue > 10) && (position < 8)) {
                    setPreviousMonth();
                    refreshCalendar();
                } else if ((gridvalue < 7) && (position > 28)) {
                    setNextMonth();
                    refreshCalendar();
                }
                ((CalendarAdapter) parent.getAdapter()).setSelected(v, position);
                int flag=((CalendarAdapter) parent.getAdapter()).getPositionList(selectedGridDate, CalendarActivity.this);
                if(flag==1)
                {
                    Intent i = new Intent(CalendarActivity.this, TimeSlotsActivity.class);

                    Log.i("INFO Cal Dates AID", getIntent().getExtras().getString("appointmentID"));
                    Log.i("INFO Cal Dates CID",getIntent().getExtras().getString("clinicID"));
                    Log.i("INFO Cal Dates CNM",getIntent().getExtras().getString("clinicName"));
                    Log.i("INFO Cal Dates PURP",getIntent().getExtras().getString("purpose"));
                    Log.i("INFO Cal Dates isUPD",getIntent().getExtras().getString("isUpdate"));
                    Log.i("INFO Cal Dates DocEM",getIntent().getExtras().getString("doctorEmail"));
                    Log.i("INFO Cal Dates PatEM",getIntent().getExtras().getString("patientEmail"));
                    Log.i("INFO Cal Dates SelDT",selectedGridDate);
                    i.putExtra("doctorEmail",getIntent().getExtras().getString("doctorEmail"));
                    i.putExtra("patientEmail",getIntent().getExtras().getString("patientEmail"));
                    i.putExtra("clinicID",getIntent().getExtras().getString("clinicID"));
                    i.putExtra("clinicName",getIntent().getExtras().getString("clinicName"));
                    i.putExtra("purpose",getIntent().getExtras().getString("purpose"));
                    i.putExtra("appointmentID",getIntent().getExtras().getString("appointmentID"));
                    i.putExtra("isUpdate",getIntent().getExtras().getString("isUpdate"));
                    i.putExtra("selectedDate",selectedGridDate);
                    startActivity(i);
                }
            }

        });



    }

    protected void setNextMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month
                .getActualMaximum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) + 1),
                    cal_month.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH,
                    cal_month.get(GregorianCalendar.MONTH) + 1);
        }

    }

    protected void setPreviousMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) - 1),
                    cal_month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH,
                    cal_month.get(GregorianCalendar.MONTH) - 1);
        }

    }

    public void refreshCalendar() {
        cal_adapter.refreshDays();
        cal_adapter.notifyDataSetChanged();
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));
    }

}
