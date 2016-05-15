package com.example.kapish.mchealthapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class TimeSlotsActivity extends AppCompatActivity {

    TableLayout mTlayout;
    TableRow tr;

    JSONParser jsonParserDates = new JSONParser();
    JSONParser jsonParserSlots = new JSONParser();

    private String clinicID;
    private String appointmentDate;
    private String doctorEmail;
    private String patientEmail;
    private  String purpose;
    private  String sId;

    private int isFetched;
    private int isFetchedTime;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_SLOTS = "slots";
    private static final String TAG_starttime = "startTime";
    private static final String TAG_endtime = "endTime";
    private static final String TAG_SLOTID = "slotId";
    private static final String TAG_APPOINTMENTS = "appointments";
    private static final String TAG_APPOINTMENT_TIME = "time";

    ArrayList<String> startTime = new ArrayList<>();
    ArrayList<String> endTime = new ArrayList<>();
    ArrayList<String> slotId = new ArrayList<>();

    ArrayList<String> appointmentsTime = new ArrayList<>();


    JSONArray Slots = null;
    JSONArray AppointmentTimes = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_slots);
        clinicID = getIntent().getExtras().getString("clinicID");
        appointmentDate = getIntent().getExtras().getString("selectedDate");
        doctorEmail = getIntent().getExtras().getString("doctorEmail");
        patientEmail = getIntent().getExtras().getString("patientEmail");
        purpose = getIntent().getExtras().getString("purpose");

        new FetchSlots().execute();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    class FetchSlots extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        protected String doInBackground(String... args) {

            // Building Parameters

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("clinicid", clinicID);
            JSONObject jsonDates = jsonParserDates.makeHttpRequest(new ServerURL().url_fetch_slots, "POST", params);

            try {
                if (jsonDates != null) {
                    isFetched = jsonDates.getInt(TAG_SUCCESS);
                    if (isFetched == 1) {
                        Slots = jsonDates.getJSONArray(TAG_SLOTS);
                        for (int i = 0; i < Slots.length(); i++) {
                            JSONObject c = Slots.getJSONObject(i);
                            slotId.add(c.getString(TAG_SLOTID));
                            startTime.add(c.getString(TAG_starttime));
                            endTime.add(c.getString(TAG_endtime));
                        }
                    }
                }
            }

            catch (JSONException e) {
                isFetched = -1;
                e.printStackTrace();
            }

            params.put("appointmentDate", appointmentDate);
            JSONObject jsonSlots = jsonParserSlots.makeHttpRequest(new ServerURL().url_fetch_appointments, "POST", params);

            try {
                if (jsonSlots != null) {
                    isFetchedTime = jsonSlots.getInt(TAG_SUCCESS);
                    if (isFetchedTime == 1) {
                        AppointmentTimes = jsonSlots.getJSONArray(TAG_APPOINTMENTS);

                        for (int i = 0; i < AppointmentTimes.length(); i++) {
                            JSONObject c = AppointmentTimes.getJSONObject(i);
                            appointmentsTime.add(c.getString(TAG_APPOINTMENT_TIME));
                        }

                    }
                }
            }

            catch (JSONException e) {
                isFetchedTime=-1;
                e.printStackTrace();
            }


            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // do Something

            if(isFetched == 1)
            {
                for(int i=0;i<startTime.size();i++) {
                    String[] startTimes = startTime.get(i).split(":");
                    String[] endTimes = endTime.get(i).split(":");
                    int start = Integer.parseInt(startTimes[0]);
                    int start1 = Integer.parseInt(startTimes[1]);
                    int end = Integer.parseInt(endTimes[0]);
                    int end1 = Integer.parseInt(endTimes[1]);
                    double add1 = Math.ceil(start1 / 15);
                    double add2 = Math.floor(end1/15);
                    start1 =((int) (add1*15))%60;
                    end1   =((int) (add2*15)%60);
                    mTlayout = (TableLayout) findViewById(R.id.table_layout);

                    int j = 0;

                    while ((start != end) || (start1 != end1)) {
                        if (j % 3 == 0) {
                            tr = new TableRow(getApplicationContext());
                            mTlayout.addView(tr);
                        }
                        final Button b = new Button(getApplicationContext());

                        if (start1 == 0)
                            b.setText("" + start + ":" + start1 + start1);
                        else
                            b.setText("" + start + ":" + start1);


                        start1 = (start1 + 15) % 60;
                        if (start1 == 0)
                        {
                            start++;
                        }

                        b.setId(Integer.parseInt(slotId.get(i)));

                        b.setWidth(150);
                        b.setHeight(20);
                        tr.addView(b);
                        b.setBackgroundResource(R.drawable.button);

                        android.widget.TableRow.LayoutParams p = new android.widget.TableRow.LayoutParams();
                        p.rightMargin = DisplayHelper.dpToPixel(15, getApplicationContext());
                        p.bottomMargin = DisplayHelper.dpToPixel(10, getApplicationContext());
                        b.setLayoutParams(p);

                        j++;

                        if(isFetchedTime==-1)
                        {
                            Toast.makeText(getApplicationContext(),"Not able to fetch slots from server",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            String time = (String) b.getText();
                            String[] time2 = time.split(":");
                            String h2 = time2[0];
                            String m2 = time2[1];
                            String h1, m1;

                            for (int k = 0; k < appointmentsTime.size(); k++) {
                                String[] time1 = appointmentsTime.get(k).split(":");
                                h1 = time1[0];
                                m1 = time1[1];

                                if ((h1.equals(h2)) && (m1.equals(m2))) {
                                    b.setEnabled(false);
                                    b.setBackgroundResource(R.drawable.disabled_button);
                                }
                            }
                            // sId=slotId.get(i).toString();
                            if (b.isEnabled()) {
                                b.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Intent intent = new Intent(TimeSlotsActivity.this,ConfirmBooking.class);
                                        Log.i("INFO Slots AID", getIntent().getExtras().getString("appointmentID"));
                                        Log.i("INFO Slots CID",getIntent().getExtras().getString("clinicID"));
                                        Log.i("INFO Slots CNM",getIntent().getExtras().getString("clinicName"));
                                        Log.i("INFO Slots PURP",getIntent().getExtras().getString("purpose"));
                                        Log.i("INFO Slots isUPD",getIntent().getExtras().getString("isUpdate"));
                                        Log.i("INFO Slots DocEM",getIntent().getExtras().getString("doctorEmail"));
                                        Log.i("INFO Slots PatEM",getIntent().getExtras().getString("patientEmail"));
                                        Log.i("INFO Slots SelDT",getIntent().getExtras().getString("selectedDate"));
                                        Log.i("INFO Slots slotid",String.valueOf(b.getId()));
                                        Log.i("INFO Slots Time Choosen",b.getText().toString());

                                        intent.putExtra("doctorEmail", doctorEmail);
                                        intent.putExtra("patientEmail",patientEmail);
                                        intent.putExtra("clinicID",clinicID);
                                        intent.putExtra("purpose",purpose);
                                        intent.putExtra("selectedDate", appointmentDate);
                                        intent.putExtra("appointmentTime", b.getText().toString());
                                        intent.putExtra("appointmentID", getIntent().getExtras().getString("appointmentID"));
                                        intent.putExtra("isUpdate", getIntent().getExtras().getString("isUpdate"));
                                        intent.putExtra("slotid",String.valueOf(b.getId()));
                                        startActivity(intent);
                                    }
                                });
                            }
                        }
                    }
                }
            }
            else if(isFetched == 0)
            {
                Toast.makeText(getApplicationContext(), "No Slots are available ", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Not able to fetch dates from server",Toast.LENGTH_LONG).show();
            }
        }

    }

}


class DisplayHelper
{
    private static Float scale;
    public static int dpToPixel(int dp, Context context) {
        if (scale == null)
            scale = context.getResources().getDisplayMetrics().density;
        return (int) ((float) dp * scale);
    }
}