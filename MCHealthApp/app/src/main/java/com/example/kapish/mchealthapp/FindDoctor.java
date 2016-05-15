package com.example.kapish.mchealthapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import android.widget.Toast;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class FindDoctor extends Fragment {

    Button btnSearch;
    Button buttonclr;
    AutoCompleteTextView txt_speciality, txt_location;
    JSONParser jsonParser;
    private HashMap<String, List<String>> mapDoctorDetails;
    private int isFetchedDoctor;
    private ProgressDialog pDialog;
    GPSTracker gpsAddressFinder;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_DOCTOR = "doctor";
    private static final String TAG_DOCTOREMAIL = "email";
    private static final String TAG_DOCTORNAME = "name";
    private static final String TAG_SPECIALITY = "speciality";
    private static final String TAG_FEES = "fees";
    String[] specialityList = {"Dermatologist", "Dentist", "General Physician", "Gynaecologist", "Homeopath", "Neurologist","Cardiology",
            "Pediatrics","ENT","Gastroenterology","Orthopedics","Neurosurgery","Radiology"};
    String[] locationList = {"GovindPuri", "Kalkaji", "Nehru Place", "Lajpat Nagar", "Saket Nagar", "Okhla"};
    private String speciality;
    private String location;




    public String getLocation() {
        return location;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_find_doctor, container, false);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.select_dialog_item, specialityList);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>
                (getActivity(), android.R.layout.select_dialog_item, locationList);
        txt_speciality = (AutoCompleteTextView) v.findViewById(R.id.autoCompleteTextView1);
        txt_speciality.setThreshold(1);//will start working from first character
        txt_speciality.setAdapter(adapter);
        txt_speciality.setTextColor(Color.parseColor("#21ACB1"));

        txt_location = (AutoCompleteTextView) v.findViewById(R.id.autoCompleteTextView2);
        txt_location.setThreshold(1);//will start working from first character
        txt_location.setAdapter(adapter1);
        txt_location.setTextColor(Color.parseColor("#21ACB1"));

        gpsAddressFinder = new GPSTracker(getActivity());
        txt_location.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (gpsAddressFinder.canGetLocation()) {
                        Geocoder gcd = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
                        String locality = gpsAddressFinder.getLocation(gcd);
                        Log.i("INFO", locality);
                        txt_location.setText(locality);
                        txt_location.setError(null);
                    } else {
                        gpsAddressFinder.showSettingsAlert();
                    }
                }
            }
        });

        btnSearch = (Button) v.findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //doctornames = new ArrayList<String>();
                mapDoctorDetails = new HashMap<String,List<String>>();
                setSpeciality(txt_speciality.getText().toString());
                setLocation(txt_location.getText().toString());
                if (getSpeciality().equals("")) {
                    txt_speciality.setError("Speciality field cannot be empty");
                } else if (getLocation().equals("")) {
                    txt_location.setError("Location field cannot be empty");
                } else {
                    new DoctorResults().execute();
                }
            }
        });
        buttonclr = (Button) v.findViewById(R.id.buttonclr);
        buttonclr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_speciality.setText("");
                txt_location.setText("");
            }
        });

        return v;

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }
    class DoctorResults extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Fetching Doctor's Names...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            jsonParser= new JSONParser();
            JSONArray doctor = null;
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("speciality", getSpeciality());
            params.put("location", getLocation());
            Log.v("speciality", getSpeciality());
            Log.v("location", getLocation());
            JSONObject json = jsonParser.makeHttpRequest(new ServerURL().url_search_doctor, "POST", params);
            try {
                if (json != null) {
                    Log.i("INFO", json.toString());
                    isFetchedDoctor = json.getInt(TAG_SUCCESS);
                    if (isFetchedDoctor == 1) {

                        doctor = json.getJSONArray(TAG_DOCTOR);
                        for (int i = 0; i < doctor.length(); i++) {
                            JSONObject c = doctor.getJSONObject(i);
                            String email = c.getString(TAG_DOCTOREMAIL);
                            String name = c.getString(TAG_DOCTORNAME);
                            String speciality = c.getString(TAG_SPECIALITY);
                            int fees = c.getInt(TAG_FEES);
                            Log.i("Email : ",email);
                            Log.i("Name is ", name);
                            Log.i("Speciality : ",speciality);
                            Log.i("Fees is", String.valueOf(fees));
                            List<String> values = new ArrayList<String>();
                            values.add(name);
                            values.add(speciality);
                            values.add(fees + "");
                            mapDoctorDetails.put(email, values);
                        }
                        Log.v("size of : ", String.valueOf(mapDoctorDetails.size()));
                    }
                }
            } catch (JSONException e) {
                isFetchedDoctor = -1;
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if(isFetchedDoctor == 1)
            {
                Intent i = new Intent(getActivity(), SearchResults.class);
                Log.i("size : ", String.valueOf(mapDoctorDetails.size()));
                String email=null;
                String name = null;
                int fees = 0;
                String speciality = null;
                Iterator<Map.Entry<String, List<String>>> entries = mapDoctorDetails.entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry<String,  List<String>> entry = entries.next();
                    Log.i("Doctor name", entry.getKey());
                    email = entry.getKey();
                    name = entry.getValue().get(0);
                    speciality = entry.getValue().get(1);
                    fees = Integer.parseInt(entry.getValue().get(2));
                }
                i.putExtra("doctors", mapDoctorDetails);
                //  i.putExtra("speciality", getSpeciality());
                startActivity(i);
            }
            else if(isFetchedDoctor == 0){
                Toast.makeText(getActivity().getApplicationContext(), "No Doctors Found,Please try some other speciality or locality!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getActivity().getApplicationContext(), "Server is not working. Please try again later.", Toast.LENGTH_SHORT).show();
            }


        }
    }


}
