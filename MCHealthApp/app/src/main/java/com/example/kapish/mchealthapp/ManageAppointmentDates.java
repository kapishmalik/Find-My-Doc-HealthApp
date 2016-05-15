package com.example.kapish.mchealthapp;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ManageAppointmentDates extends Fragment implements ListView.OnItemClickListener,DatePickerDialog.OnDateSetListener {
    ListView l;
    List<DatesDoc> my_list=new ArrayList<DatesDoc>();
    List<String> clinics=new ArrayList<String>();
    MyListAdapter<DatesDoc> adapter;
    String docEmail ;
    String clinicName ;
    private static final String TAG_SUCCESS = "success";
    private int check;
    private int check1;
    private ProgressDialog pDialog;
    FloatingActionButton fab;
    JSONArray arr;
    JSONArray cArr;
    Fragment my_fragment = this;
    Spinner spinner;
    String dateToAdd="";
    Calendar myCalendar = null;
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
        View v=inflater.inflate(R.layout.fragment_manage_appointment_dates, container, false);
        spinner = (Spinner) v.findViewById(R.id.spinnerClinics);
        l = (ListView) v.findViewById(R.id.DatesList);
        l.setOnItemClickListener(this);
        fab = (FloatingActionButton) v.findViewById(R.id.fab);
        final DatePickerDialog.OnDateSetListener listener = this;
        new DoctorClinics().execute();
        fab.setOnClickListener(new View.OnClickListener() {
            Calendar c = Calendar.getInstance();
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), listener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

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
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        myCalendar = Calendar.getInstance();

        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateLabel();
        if(clinicName == null)
        {
            Toast.makeText(getActivity().getApplicationContext(),"Please choose clinic first.",Toast.LENGTH_LONG).show();
        }
        else {
            new UpdateDates().execute();
        }
    }

    private void updateLabel() {

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        dateToAdd = sdf.format(myCalendar.getTime());
    }


    private class MyListAdapter<A> extends ArrayAdapter<DatesDoc> {
        public MyListAdapter() {
            super(getActivity(), R.layout.layout_manage_dates_list_item_doc, my_list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View itemView = convertView;
            if (itemView == null) {
                itemView = getActivity().getLayoutInflater().inflate(R.layout.layout_manage_dates_list_item_doc, parent, false);
            }

            DatesDoc docdates = my_list.get(position);


            TextView pat = (TextView) itemView.findViewById(R.id.textViewClinic);
            pat.setText(docdates.getClinic_name());

            TextView date = (TextView) itemView.findViewById(R.id.textViewDate);
            date.setText(docdates.getDate().toString());

            return itemView;
        }
    }

    class DoctorDates extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("email",docEmail);
            params.put("clinic_name", clinicName);
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONParser jsonParser1 = new JSONParser();
            JSONObject json1 = jsonParser1.makeHttpRequest(new ServerURL().url_Doctor_Dates, "POST", params);
            try {
                if (json1 != null) {
                    int success = json1.getInt(TAG_SUCCESS);
                    check1 = success;
                    if (success == 1) {
                        check1 = 1;
                        List<DatesDoc> temp = new ArrayList<DatesDoc>();
                        arr = json1.getJSONArray("dates");
                        if(arr!=null) {

                            for (int i = 0; i < arr.length(); i++) {  // **line 2**
                                JSONObject childJSONObject = arr.getJSONObject(i);
                                String date = childJSONObject.getString("datesarr");
                                temp.add(new DatesDoc(clinicName, date));
                            }
                            my_list = temp;
                        }
                        else
                        {
                            check1 = 4;
                        }

                    } else if (success == 0) {
                        check1 = 0;
                        //Toast.makeText(getActivity().getApplicationContext(),"Error..",Toast.LENGTH_SHORT).show();
                    } else if (success == -2) {
                        check1 = -2;
                        //Toast.makeText(getActivity().getApplicationContext(),"Server not working..",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity().getApplicationContext(), "No Dates added.", Toast.LENGTH_SHORT).show();
            }
            else if (check1 == -2) {
                Toast.makeText(getActivity().getApplicationContext(), "server not working", Toast.LENGTH_SHORT).show();
            } else if (check1 == -3) {
                Toast.makeText(getActivity().getApplicationContext(), "json is null", Toast.LENGTH_SHORT).show();
            } else if (check1 == 1) {
                adapter = new MyListAdapter<>();
                l.setAdapter(adapter);

             //   Toast.makeText(getActivity().getApplicationContext(), "good", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class DoctorClinics extends AsyncTask<String, String, String> {



            @Override
            protected String doInBackground(String... strings) {
                HashMap<String,String> params = new HashMap<String,String>();
                params.put("email",docEmail);

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
                            //Toast.makeText(getActivity().getApplicationContext(),"Error..",Toast.LENGTH_SHORT).show();
                        }
                        else if(success == -2)
                        {
                            check = -2;
                            //Toast.makeText(getActivity().getApplicationContext(),"Server not working..",Toast.LENGTH_SHORT).show();
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
                           //do something
                            } else {

                                clinicName = spinner.getItemAtPosition(i).toString();
                                new DoctorDates().execute();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }


                    });
               //     Toast.makeText(getActivity().getApplicationContext(),"good",Toast.LENGTH_SHORT).show();
                }
            }

    }

    class UpdateDates extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> params = new HashMap<String,String>();
            params.put("email",docEmail);
            params.put("clinic_name", clinicName);
            params.put("mydate", dateToAdd);
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONParser jsonParserAdd = new JSONParser();
            JSONObject json = jsonParserAdd.makeHttpRequest(new ServerURL().url_Add_Date, "POST",params);
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
                        //Toast.makeText(getActivity().getApplicationContext(),"Error..",Toast.LENGTH_SHORT).show();
                    }
                    else if(success == -2)
                    {
                        check = -2;
                        //Toast.makeText(getActivity().getApplicationContext(),"Server not working..",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity().getApplicationContext(),"not added",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity().getApplicationContext(),"Date added successfully.",Toast.LENGTH_SHORT).show();
            }
        }

    }


}






