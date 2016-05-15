package com.example.kapish.mchealthapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ManageClinics extends Fragment  {
    ListView l;
    List<String> clinics=new ArrayList<String>();
    MyListAdapter<String> adapter;
    String docEmail;

    JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";
    private int check;
    JSONArray cArr;

    void check(String docEmail)
    {
        this.docEmail=docEmail;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_manage_clinics, container, false);
        l= (ListView) v.findViewById(R.id.ClinicsList);
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity().getApplicationContext(),AddClinic.class);
                i.putExtra("docemail",docEmail);
                startActivity(i);
            }
        });
        new DoctorClinics().execute();
        //l.setOnItemClickListener(this);

        return v;
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



    private class MyListAdapter<A> extends ArrayAdapter<String> {

        public MyListAdapter() {
            super(getActivity(), R.layout.layout_manage_clinic_list_item_doc, clinics);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View itemView = convertView;
            if (itemView == null) {
                itemView = getActivity().getLayoutInflater().inflate(R.layout.layout_manage_clinic_list_item_doc, parent, false);
            }

            final String currentClinic = clinics.get(position);


            TextView clinic = (TextView) itemView.findViewById(R.id.textViewClinicName);
            clinic.setText(currentClinic);


            return itemView;
        }
    }

    class DoctorClinics extends AsyncTask<String, String, String> {



        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> params = new HashMap<String,String>();
            params.put("email",docEmail);

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(new ServerURL().url_Doctor_Clinics, "POST",params);
            try {
                if(json != null) {
                    int success = json.getInt(TAG_SUCCESS);
                    check=success;
                    if(success == 1)
                    {
                        check = 1;
                        List<String> temp = new ArrayList<String>();
                        cArr = json.getJSONArray("clinics");
                        if(cArr!=null) {
                            for (int i = 0; i < cArr.length(); i++) {  // **line 2**
                                JSONObject childJSONObject = cArr.getJSONObject(i);
                                String c = childJSONObject.getString("clinicsarr");
                                temp.add(c);
                            }
                            clinics = temp;
                        }
                        else
                        {
                            check=4;
                        }

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
                adapter=new MyListAdapter<>();
                l.setAdapter(adapter);
            //    Toast.makeText(getActivity().getApplicationContext(),"good",Toast.LENGTH_SHORT).show();
            }
        }

    }
}
