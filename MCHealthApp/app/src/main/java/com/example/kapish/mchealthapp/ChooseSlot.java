package com.example.kapish.mchealthapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ChooseSlot extends Fragment implements ListView.OnItemClickListener,TimePickerDialog.OnTimeSetListener {
    ListView l;
    List<String> my_list1=new ArrayList<String>();
    List<String> my_list2=new ArrayList<String>();
    List<String> clinics=new ArrayList<String>();
    MyListAdapter<String> adapter;
    String docEmail ;
    String clinicName = null;
    String time_fetched1;
    String time_fetched2;
    private static final String TAG_SUCCESS = "success";
    private int check;
    private int check1;
    private ProgressDialog pDialog;
    JSONArray arr;
    JSONArray cArr;
    Fragment my_fragment = this;
    Spinner spinner;
    String dateToAdd="";
    int starttime_toadd;
    int endtime_toadd;
    private int hr1;
    private int min1;
    private int hr2;
    private int min2;

    int i=1;
    JSONParser jsonParserAdd = new JSONParser();
    Calendar myCalendar = null;
    int set = 0;

    void check(String docEmail)
    {
        this.docEmail=docEmail;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //new DoctorDates().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.choose_slot, container, false);
        hr1=0;
        hr2=0;
        min1=0;
        min2=0;
        spinner = (Spinner) v.findViewById(R.id.spinnerClinics1);
        l = (ListView) v.findViewById(R.id.TimeList);
        l.setOnItemClickListener(this);
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        final TimePickerDialog.OnTimeSetListener listener = this;
        new DoctorClinics().execute();
        fab.setOnClickListener(new View.OnClickListener() {
            Calendar c = Calendar.getInstance();

            @Override
            public void onClick(View view) {
                //for (i = 1; i <= 2; i++) {
                    set =1;
                    TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), listener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), true);
                   timePickerDialog.setTitle("Choose Slot Start Time");
                timePickerDialog.show();
               // }
                // starttime_toadd = (timePickerDialog.getCurrentMinute() * 60 + picker.getCurrentHour());

            }
        });
        /**my_list.add(new DatesDoc("Clinic1", "11 / 01 / 11"));
         my_list.add(new DatesDoc("Clinic2", "11 / 01 / 11"));
         my_list.add(new DatesDoc("Clinic3", "11 / 01 / 11"));
         my_list.add(new DatesDoc("Clinic4", "11 / 01 / 11"));**/

        return v;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();


    }



    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(getActivity(),"Clicked on item in list",Toast.LENGTH_LONG).show();
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        int hr = hourOfDay;
        int min = minute;
              //  Toast.makeText(getContext(),"Time"+hr+":"+min,Toast.LENGTH_SHORT).show();
        if(set==1) {

            if ((hr / 10 == 0) && (min / 10 == 0))
                time_fetched1 = "0" + hr + ":0" + min + ":00";
            else if ((hr / 10 == 0) && (min / 10 > 0))
                time_fetched1 = "0" + hr + ":" + min + ":00";
            else if (min / 10 == 0)
                time_fetched1 = hr + ":0" + min + ":00";
            else
                time_fetched1 = hr + ":" + min + ":00";
            String[] timefetch = time_fetched1.split(":");
            hr1=Integer.parseInt(timefetch[0]);
            min1=Integer.parseInt(timefetch[1]);
            Log.i("INFO Time Slot",time_fetched1+" "+timefetch[0]+"  "+timefetch[1]);
            set = 2;
            TimePickerDialog.OnTimeSetListener listener = this;
            Calendar c = Calendar.getInstance();
            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), listener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), true);
            timePickerDialog.setTitle("Choose Slot End Time");
            timePickerDialog.show();
           // Toast.makeText(getActivity(), time_fetched1, Toast.LENGTH_SHORT).show();
        }
        else {
            if ((hr / 10 == 0) && (min / 10 == 0))
                time_fetched2 = "0" + hr + ":0" + min + ":00";
            else if ((hr / 10 == 0) && (min / 10 > 0))
                time_fetched2 = "0" + hr + ":" + min + ":00";
            else if (min / 10 == 0)
                time_fetched2 = hr + ":0" + min + ":00";
            else
                time_fetched2 = hr + ":" + min + ":00";
           // Toast.makeText(getContext(), time_fetched2, Toast.LENGTH_SHORT).show();
            String[] timefetch = time_fetched2.split(":");
            hr2=Integer.parseInt(timefetch[0]);
            min2=Integer.parseInt(timefetch[1]);
            Log.i("INFO Time Slot",time_fetched2+" "+timefetch[0]+"  "+timefetch[1]);
            Log.i("INFO Choose Slot",hr1+"   "+hr2+"  "+min1+"   "+min2);
            if(clinicName == null)
            {
                Toast.makeText(getActivity().getApplicationContext(),"Choose your Clinic first.",Toast.LENGTH_LONG).show();
            }
            else if(time_fetched1 == null || time_fetched2 == null)
            {
                Toast.makeText(getActivity().getApplicationContext(),"Start Time or End Time not properly Choosen.Try adding again.",Toast.LENGTH_LONG).show();
            }
            else if((hr2 >hr1) ||((hr2 == hr1)&&(min2>min1)))  {
                new DoctorSlots().execute();
            }
            else
            {
                Toast.makeText(getActivity().getApplicationContext(),"End Time should be greater than Start Time.",Toast.LENGTH_LONG).show();
            }
        }


    }


    private class MyListAdapter<A> extends ArrayAdapter<String> {
        public MyListAdapter() {
            super(getActivity(), R.layout.layout_manage_slots, my_list1);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View itemView = convertView;
            if (itemView == null) {
                itemView = getActivity().getLayoutInflater().inflate(R.layout.layout_manage_slots, parent, false);
            }

            String start = my_list1.get(position);
            String end = my_list2.get(position);

            TextView pat = (TextView) itemView.findViewById(R.id.textViewStart);
            pat.setText(start);

            TextView date = (TextView) itemView.findViewById(R.id.textViewEnd);
            date.setText(end);

            return itemView;
        }
    }

    class DoctorDates extends AsyncTask<String, String, String> {

        
        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("email", docEmail);
            params.put("clinic_name", clinicName);

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONParser jsonParser1 = new JSONParser();
            JSONObject json1 = jsonParser1.makeHttpRequest(new ServerURL().url_slot_manage, "POST", params);
            try {
                if (json1 != null) {
                    int success = json1.getInt(TAG_SUCCESS);
                    check1 = success;
                    if (success == 1) {
                        check1 = 1;
                        List<String> temp1 = new ArrayList<String>();
                        List<String> temp2 = new ArrayList<String>();
                        arr = json1.getJSONArray("slot");
                        if(arr!=null) {

                            for (int i = 0; i < arr.length(); i++) {  // **line 2**
                                JSONObject childJSONObject = arr.getJSONObject(i);
                                String start = childJSONObject.getString("start");
                                String end = childJSONObject.getString("end");
                                temp1.add(start);
                                temp2.add(end);
                            }
                            my_list1 = temp1;
                            my_list2 = temp2;
                        }
                        else
                        {
                            check1 = 4;
                        }

                    } else if (success == 0) {
                        check1 = 0;
                        //Toast.makeText(getContext(),"Error..",Toast.LENGTH_SHORT).show();
                    } else if (success == -2) {
                        check1 = -2;
                        //Toast.makeText(getContext(),"Server not working..",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    check1 = -3;
                }
            } catch (JSONException e) {
                check1 = -1;
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            if (check1 == -1)
                Toast.makeText(getActivity().getApplicationContext(), "json exception", Toast.LENGTH_SHORT).show();
            else if (check1 == 0) {
                Toast.makeText(getActivity().getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
            }else if(check1 ==4){
                Toast.makeText(getActivity().getApplicationContext(), "No Dates added.", Toast.LENGTH_SHORT).show();
            }
            else if (check1 == -2) {
                Toast.makeText(getActivity().getApplicationContext(), "server not working", Toast.LENGTH_SHORT).show();
            } else if (check1 == -3) {
                Toast.makeText(getActivity().getApplicationContext(), "json is null", Toast.LENGTH_SHORT).show();
            } else if (check1 == 1) {
                adapter = new MyListAdapter<>();
                l.setAdapter(adapter);

               // Toast.makeText(getActivity().getApplicationContext(), "good", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class DoctorClinics extends AsyncTask<String, String, String> {

        
        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> params = new HashMap<String,String>();
            params.put("email", docEmail);

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONParser jsonParser = new JSONParser();
            JSONObject json = jsonParser.makeHttpRequest(new ServerURL().url_Doctor_Clinics, "POST",params);
            try {
                if(json != null) {
                    int success = json.getInt(TAG_SUCCESS);
                    check=success;
                    if(success == 1)
                    {
                        check = 1;
                        List<String> temp = new ArrayList<String>();
                        temp.add("Choose Clinic");
                        cArr = json.getJSONArray("clinics");
                        if(cArr!=null) {
                            for (int i = 0; i < cArr.length(); i++) {  // **line 2**
                                JSONObject childJSONObject = cArr.getJSONObject(i);
                                String c = childJSONObject.getString("clinicsarr");
                                temp.add(c);
                            }
                        }
                        else
                        {
                            check=4;
                        }
                        clinics = temp;
                    }
                    else if(success == 0)
                    {
                        check = 0;
                        //Toast.makeText(getContext(),"Error..",Toast.LENGTH_SHORT).show();
                    }
                    else if(success == -2)
                    {
                        check = -2;
                        //Toast.makeText(getContext(),"Server not working..",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    check=-3;
                }
            }
            catch (JSONException e) {
                check=-1;
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
                        if(check == -1)
                Toast.makeText(getActivity().getApplicationContext(),"json exception",Toast.LENGTH_SHORT).show();
            else if(check == 0)
            {
                Toast.makeText(getActivity().getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
            }
            else if(check == 4)
            {
                Toast.makeText(getActivity().getApplicationContext(),"No Clinics added.",Toast.LENGTH_SHORT).show();
            }
            else if(check == -2)
            {
                Toast.makeText(getActivity().getApplicationContext(),"server not working",Toast.LENGTH_SHORT).show();
            }
            else if(check == -3)
            {
                Toast.makeText(getActivity().getApplicationContext(),"json is null",Toast.LENGTH_SHORT).show();
            }
            else if(check == 1)
            {
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_spinner_item, clinics);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(dataAdapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase("Choose Clinic")) {
                        } else {

                            clinicName = spinner.getItemAtPosition(i).toString();
                            new DoctorDates().execute();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }


                });
               // Toast.makeText(getContext(),"good",Toast.LENGTH_SHORT).show();
            }
        }

    }


    class DoctorSlots extends AsyncTask<String, String, String> {



        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("email", docEmail);
            params.put("clinic_name", clinicName);
            Log.v("starttime",time_fetched1);
            params.put("starttime", time_fetched1);
            params.put("endtime", time_fetched2);

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONParser jsonParser2 = new JSONParser();
            JSONObject json2 = jsonParser2.makeHttpRequest(new ServerURL().url_slot_update, "POST", params);
            try {
                if (json2 != null) {
                    int success = 0;
                        success = json2.getInt(TAG_SUCCESS);

                    check1 = success;
                    if (success == 1) {
                        check1 = 1;
                     /*   List<String> temp1 = new ArrayList<String>();
                        List<String> temp2 = new ArrayList<String>();
                        arr = json2.getJSONArray("slot");
                        if(arr!=null) {

                            for (int i = 0; i < arr.length(); i++) {  // **line 2**
                                JSONObject childJSONObject = arr.getJSONObject(i);
                                String start = childJSONObject.getString("start");
                                String end = childJSONObject.getString("end");
                                temp1.add(start);
                                temp2.add(end);
                            }
                            my_list1 = temp1;
                            my_list2 = temp2;
                        }
                        else
                        {
                            check1 = 4;
                        }*/

                    } else if (success == 0) {
                        check1 = 0;
                        //Toast.makeText(getContext(),"Error..",Toast.LENGTH_SHORT).show();
                    } else if (success == -2) {
                        check1 = -2;
                        //Toast.makeText(getContext(),"Server not working..",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    check1 = -3;
                }
            } catch (JSONException e) {
                check1 = -1;
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {

            if (check1 == -1)
                Toast.makeText(getActivity().getApplicationContext(), "json exception", Toast.LENGTH_SHORT).show();
            else if (check1 == 0) {
                Toast.makeText(getActivity().getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
            }else if(check ==4){
                Toast.makeText(getActivity().getApplicationContext(), "No new slot added.", Toast.LENGTH_SHORT).show();
            }
            else if (check1 == -2) {
                Toast.makeText(getActivity().getApplicationContext(), "server not working", Toast.LENGTH_SHORT).show();
            } else if (check1 == -3) {
                Toast.makeText(getActivity().getApplicationContext(), "json is null", Toast.LENGTH_SHORT).show();
            } else if (check1 == 1) {
              //  adapter = new MyListAdapter<>();
               // l.setAdapter(adapter);

               Toast.makeText(getActivity().getApplicationContext(), "Slots Added Successfully.", Toast.LENGTH_SHORT).show();
            }
        }
    }


/*
    class UpdateTime extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("connecting..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> params = new HashMap<String,String>();
            params.put("email", patientEmail);
            params.put("clinic_name", clinicName);
            params.put("mydate", dateToAdd);
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParserAdd.makeHttpRequest(new ServerURL().url_Add_Time, "POST",params);
            try {
                if(json != null) {
                    int success = json.getInt(TAG_SUCCESS);
                    check=success;
                    if(success == 1)
                    {
                        check = 1;
                    }
                    else if(success == 0)
                    {
                        check = 0;
                        //Toast.makeText(getContext(),"Error..",Toast.LENGTH_SHORT).show();
                    }
                    else if(success == -2)
                    {
                        check = -2;
                        //Toast.makeText(getContext(),"Server not working..",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    check=-3;
                }
            }
            catch (JSONException e) {
                check=-1;
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
            if(check == -1)
                Toast.makeText(getContext(),"json exception",Toast.LENGTH_SHORT).show();
            else if(check == 0)
            {
                Toast.makeText(getContext(),"not added",Toast.LENGTH_SHORT).show();
            }
            else if(check == -2)
            {
                Toast.makeText(getContext(),"server not working",Toast.LENGTH_SHORT).show();
            }
            else if(check == -3)
            {
                Toast.makeText(getContext(),"json is null",Toast.LENGTH_SHORT).show();
            }
            else if(check == 1)
            {
                Toast.makeText(getContext(),"added",Toast.LENGTH_SHORT).show();
            }
        }

    }
*/

}






