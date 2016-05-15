package com.example.kapish.mchealthapp;

import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ListViewActivity extends Activity implements OnClickListener {

    private ListView lv_android;
    private AndroidListAdapter list_adapter;
    private Button btn_calender;
    private String clinicID;

    private int isFetched;


    private static final String TAG_SUCCESS   = "success";
    private static final String TAG_DATES     = "dates";
    private static final String TAG_DATE      = "date";

    JSONArray Dates = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        clinicID = getIntent().getExtras().getString("clinicID");
        Log.i("INFO List Dates","Clinic id is "+clinicID);
        CalendarCollection.date_collection_arr=new ArrayList<CalendarCollection>();
        new FetchDates().execute();

    }



    public void getWidget(){
        btn_calender = (Button) findViewById(R.id.btn_calender);
        btn_calender.setOnClickListener(this);

        lv_android = (ListView) findViewById(R.id.lv_android);
        list_adapter=new AndroidListAdapter(ListViewActivity.this,R.layout.list_item, CalendarCollection.date_collection_arr);
        lv_android.setAdapter(list_adapter);

    }



    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_calender:
                Log.i("INFO List Dates AID",getIntent().getExtras().getString("appointmentID"));
                Log.i("INFO List Dates CID",getIntent().getExtras().getString("clinicID"));
                Log.i("INFO List Dates CNM",getIntent().getExtras().getString("clinicName"));
                Log.i("INFO List Dates PURP",getIntent().getExtras().getString("purpose"));
                Log.i("INFO List Dates isUPD",getIntent().getExtras().getString("isUpdate"));
                Log.i("INFO List Dates DocEM",getIntent().getExtras().getString("doctorEmail"));
                Log.i("INFO List Dates PatEM",getIntent().getExtras().getString("patientEmail"));
                Intent i = new Intent(ListViewActivity.this,CalendarActivity.class);
                i.putExtra("appointmentID",getIntent().getExtras().getString("appointmentID"));
                i.putExtra("clinicID",getIntent().getExtras().getString("clinicID"));
                i.putExtra("clinicName",getIntent().getExtras().getString("clinicName"));
                i.putExtra("purpose",getIntent().getExtras().getString("purpose"));
                i.putExtra("isUpdate",getIntent().getExtras().getString("isUpdate"));
                i.putExtra("doctorEmail",getIntent().getExtras().getString("doctorEmail"));
                i.putExtra("patientEmail",getIntent().getExtras().getString("patientEmail"));
                startActivity(i);

                break;

            default:
                break;
        }

    }

    /**
     * Background Async Task to fetch all Doctor's Dates from Server
     * */
    class FetchDates extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        /**
         * Fetching Dates
         * */
        protected String doInBackground(String... args) {

            // Building Parameters

            HashMap<String,String> params= new HashMap<String,String>();
            params.put("clinicid", clinicID);
            Log.i("INFO","Clinic id is"+clinicID);
            JSONParser jsonParser = new JSONParser();
            JSONObject json = jsonParser.makeHttpRequest(new ServerURL().url_fetch_dates, "POST", params);
            try {
                if(json != null)
                {
                    Log.i("INFO", json.toString());
                    isFetched = json.getInt(TAG_SUCCESS);
                    if(isFetched == 1)
                    {
                        Dates = json.getJSONArray(TAG_DATES);
                        for (int i = 0; i < Dates.length(); i++) {
                            JSONObject c = Dates.getJSONObject(i);
                            String Date = c.getString(TAG_DATE);
                            CalendarCollection.date_collection_arr.add(new CalendarCollection(Date));
                        }
                    }

                }

            }
            catch (JSONException e) {
                isFetched = -1;
                e.printStackTrace();
            }

            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // do Something
            if(isFetched == 1)
            {
                getWidget();
            }
            else if(isFetched == 0)
            {
                Toast.makeText(getApplicationContext(),"Doctor is not currently Available",Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Not able to fetch dates from server",Toast.LENGTH_LONG).show();
            }
        }

    }

}
