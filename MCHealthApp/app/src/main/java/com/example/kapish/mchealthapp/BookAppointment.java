package com.example.kapish.mchealthapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;



public class BookAppointment extends Fragment {

    AutoCompleteTextView autoCompleteTextViewDoctorName;
    Button bookAppointment;
    private String patientName;
    private String patientEmail;
    private ArrayList<String> doctorName;
    private HashMap<String, String> mapDoctorDetails;
    private int isFetched;

    JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_DOCTORS = "doctor";
    private static final String TAG_NAME = "name";
    private static final String TAG_EMAIL = "email";
    JSONArray doctors = null;


    void check(String patientName,String patientEmail)
    {
        this.patientName =patientName;
        this.patientEmail=patientEmail;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_book_appointment, container, false);


        doctorName = new ArrayList<String>();
        mapDoctorDetails = new HashMap<String, String>();


        bookAppointment=(Button) v.findViewById(R.id.book);
        autoCompleteTextViewDoctorName = (AutoCompleteTextView) v.findViewById(R.id.doctorUsername);
        new FetchDoctors().execute();
        bookAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),ChooseAppointmentSlot.class);


                String userName  = autoCompleteTextViewDoctorName.getText().toString();
                String userEmail = null;
                int flag=0;

                if(userName.equals(""))
                {
                    //Toast.makeText(getApplicationContext(),
                    //      "Please enter doctor name. Name field cannot be empty", Toast.LENGTH_SHORT).show();
                    autoCompleteTextViewDoctorName.setError("Name field cannot be empty");
                }
                else
                {
                    Iterator<Map.Entry<String,String>> entries = mapDoctorDetails.entrySet().iterator();
                    while (entries.hasNext()) {
                        Map.Entry<String, String> entry = entries.next();
                        Log.i("INFO",userName);
                        Log.i("Doctor name",entry.getValue());
                        if(entry.getValue().equals(userName))
                        {
                            flag=1;
                            userEmail = entry.getKey();
                            break;
                        }

                    }
                    if(flag == 1) {
                        i.putExtra("DoctorEmail",userEmail);
                        i.putExtra("PatientEmail", patientEmail);
                        i.putExtra("PatientName",patientName);
                        i.putExtra("DoctorName",userName);
                        startActivity(i);
                    }
                    else
                    {
                        autoCompleteTextViewDoctorName.setError("Enter Valid Doctor Name");
                    }
                }
            }
        });
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }






    /**
     * Background Async Task to fetch all Doctor's Name from Server
     * */
    class FetchDoctors extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        /**
         * Fetching Doctor Name
         * */
        protected String doInBackground(String... args) {

            // Building Parameters

            HashMap<String,String>params= new HashMap<String,String>();

            JSONObject json = jsonParser.makeHttpRequest(new ServerURL().url_fetch_doctors, "POST", params);
            try {
                if(json != null)
                {
                    Log.i("INFO",json.toString());
                    isFetched = json.getInt(TAG_SUCCESS);
                    if(isFetched == 1)
                    {
                        doctors = json.getJSONArray(TAG_DOCTORS);
                        for (int i = 0; i < doctors.length(); i++) {
                            JSONObject c = doctors.getJSONObject(i);
                            String name = c.getString(TAG_NAME);
                            String email= c.getString(TAG_EMAIL);
                            Log.i("Doctor Name is ",name);
                            Log.i("Doctor Email is",email);
                            doctorName.add(name);
                            mapDoctorDetails.put(email,name);

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
            // dismiss the dialog once done

            if(isFetched == 1)
            {
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>
                        (getActivity(), android.R.layout.select_dialog_item, doctorName);
                autoCompleteTextViewDoctorName.setAdapter(adapter1);
            }
            else if(isFetched == 0)
            {
                Toast.makeText(getActivity().getApplicationContext(),"No Doctor Registered with us till now",Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getActivity().getApplicationContext(),"Server not working. Please try again later.",Toast.LENGTH_LONG).show();
            }


        }

    }


}
