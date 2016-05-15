package com.example.kapish.mchealthapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewAppointmentStatus extends Fragment {
    //ListView l;
    List<Appointments> patient_appointments = new ArrayList<Appointments>();
    MyListAdapter<Appointments> adapter;
    ListView list;
    private String patientName;
    private String patientEmail;
    JSONParser jsonParser1 = new JSONParser();
    JSONArray appointment = null;
    JSONParser jsonParser;
    private int isFetched;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_APPOINTMENTS = "appointments";
    private static final String TAG_CLINICID = "clinicid";
    private static final String TAG_APPOINTMENTID = "appointmentid";
    private static final String TAG_DOCTORNAME = "name";
    private static final String TAG_DOCTOREMAIL= "email";
    private static final String TAG_APPOINTMENTDATE = "appointmentdate";
    private static final String TAG_APPOINTMENTTIME = "appointmenttime";
    private static final String TAG_APPOINTMENTSTATUSCODE = "appointmentstatuscode";
    private static final String TAG_PURPOSE = "purpose";
    private static final String TAG_ADDRESS = "address";

    private static int appointmentidtodelete = 0;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    void check(String patientName, String patientEmail) {
        this.patientName = patientName;
        this.patientEmail = patientEmail;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_view_appointment_status, container, false);
        list = (ListView) v.findViewById(R.id.Appointments);
        new PatientAppointments().execute();
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

    private class MyListAdapter<A> extends ArrayAdapter<Appointments> {

        public MyListAdapter() {
            super(getActivity(), R.layout.appointment_list, patient_appointments);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View itemView = convertView;
          if (itemView == null) {
                itemView = getActivity().getLayoutInflater().inflate(R.layout.appointment_list, parent, false);
            }

            final Appointments currentAppointment = patient_appointments.get(position);


            TextView date = (TextView) itemView.findViewById(R.id.item_appoint_date);
            date.setText(currentAppointment.getDate());

            ImageView imageView = (ImageView) itemView.findViewById(R.id.item_icon);
            imageView.setImageResource(currentAppointment.getIconID());

            TextView doctors_name = (TextView) itemView.findViewById(R.id.item_txtDoctorName);
            doctors_name.setText(currentAppointment.getDoctor_name());

            TextView doctors_clinic = (TextView) itemView.findViewById(R.id.item_txtclinic);
            doctors_clinic.setText(currentAppointment.getClinic());

            TextView doctors_purpose = (TextView) itemView.findViewById(R.id.item_txtPurpose);
            doctors_purpose.setText(currentAppointment.getPurpose());

            TextView doctors_calendar = (TextView) itemView.findViewById(R.id.item_txtcalendar);
            doctors_calendar.setText(currentAppointment.getCalendar());

            TextView doctors_status = (TextView) itemView.findViewById(R.id.item_txtstatus);
            doctors_status.setText(currentAppointment.getStatus());

            int pos = position;
            Button reschedule = (Button) itemView.findViewById(R.id.btnReschedule);
            reschedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                    alertDialog.setTitle("Reschedule");
                    alertDialog.setMessage("Do you want to reschedule the appointment?");
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(getActivity().getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getActivity(), ListViewActivity.class);
                            Log.i("INFO",String.valueOf(currentAppointment.getClinicid()));

                            i.putExtra("appointmentID",String.valueOf(currentAppointment.getAppointmentId()));
                            i.putExtra("clinicID",String.valueOf(currentAppointment.getClinicid()));
                            i.putExtra("clinicName",currentAppointment.getClinic());
                            i.putExtra("purpose",currentAppointment.getPurpose());
                            i.putExtra("isUpdate","1"); // appointment need to be updated
                            i.putExtra("doctorEmail",currentAppointment.getDoctor_email());
                            i.putExtra("patientEmail", patientEmail);
                            startActivity(i);
                        }

                    });
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.show();
                }
            });

            Button cancel = (Button) itemView.findViewById(R.id.buttonCancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                View v;

                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                    alertDialog.setTitle("Cancel");
                    alertDialog.setMessage("Do you want to cancel the appointment?");
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            patient_appointments.remove(currentAppointment);
                            appointmentidtodelete = currentAppointment.getAppointmentId();
                            new DeleteAppointment().execute();
                            //l = (ListView) v.findViewById(R.id.Appointments);
                            //l.setAdapter(adapter);
                        }

                    });
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.show();
                }
            });
            return itemView;
        }
    }


        class PatientAppointments extends AsyncTask<String, String, String> {
            View v;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                 }

            protected String doInBackground(String... args) {

                HashMap<String, String> params = new HashMap<String, String>();
                params.put("patientemail",patientEmail);
                JSONObject json = jsonParser1.makeHttpRequest(new ServerURL().url_search_appointments, "POST", params);
                try {
                    if (json != null) {
                        Log.i("INFODD", json.toString());
                        isFetched = json.getInt(TAG_SUCCESS);
                        if (isFetched == 1) {
                            appointment = json.getJSONArray(TAG_APPOINTMENTS);
                            for (int i = 0; i < appointment.length(); i++) {
                                JSONObject c = appointment.getJSONObject(i);
                                int appointmentid = c.getInt(TAG_APPOINTMENTID);
                                String name = c.getString(TAG_DOCTORNAME);
                                String email =c.getString(TAG_DOCTOREMAIL);
                                int clinicid = c.getInt(TAG_CLINICID);
                                String appointmentdate = c.getString(TAG_APPOINTMENTDATE);
                                String appointmenttime = c.getString(TAG_APPOINTMENTTIME);
                                String purpose = c.getString(TAG_PURPOSE);
                                String address = c.getString(TAG_ADDRESS);
                                int appointmentstatuscode = c.getInt(TAG_APPOINTMENTSTATUSCODE);
                                String appointmentStatus = null;
                                Log.i("Appointment Id is ", String.valueOf(appointmentid));
                                Log.i("Name is ", name);
                                Log.i("Clinic id is : ", String.valueOf(clinicid));
                                Log.i("appointmentdate : ", appointmentdate);
                                Log.i("appointmenttime : ", appointmenttime);
                                Log.i("purpose : ", purpose);
                                Log.i("address : ", address);
                                Log.i("Doctor email fetched:",TAG_DOCTOREMAIL);
                                if (appointmentstatuscode == 0) {
                                    appointmentStatus = "Initiated";
                                } else if (appointmentstatuscode == 1) {
                                    appointmentStatus = "Approved";
                                }
                                else
                                {
                                    appointmentStatus = "Rejected";
                                }
                                patient_appointments.add(new Appointments(appointmentid, R.mipmap.dr3,clinicid, appointmentdate, "Dr " + name,email, address, purpose, appointmenttime, appointmentStatus));
                            }
                        }
                    }

                } catch (JSONException e) {
                    isFetched = -1;
                    e.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(String file_url) {
                if (isFetched == 1) {
                    adapter = new MyListAdapter();
                    //list = (ListView) v.findViewById(R.id.Appointments);
                    list.setAdapter(adapter);
                }
                else if(isFetched == -1){
                    Toast.makeText(getActivity().getApplicationContext(),"Server not working.Please try after sometime!",Toast.LENGTH_SHORT).show();
                }

            }
        }

        class DeleteAppointment extends AsyncTask<String, String, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            protected String doInBackground(String... args) {
                jsonParser = new JSONParser();
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("appointmentid", appointmentidtodelete + "");
                JSONObject json = jsonParser.makeHttpRequest(new ServerURL().url_delete_appointment, "POST", params);
                try {
                    if (json != null) {
                        Log.i("INFO", json.toString());
                        isFetched = json.getInt(TAG_SUCCESS);
                        if (isFetched == 1) {

                        }
                    }

                } catch (JSONException e) {
                    isFetched = -1;
                    e.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(String file_url) {
                if (isFetched == 1) {
                    adapter = new MyListAdapter();
                    //list = (ListView) v.findViewById(R.id.Appointments);
                    list.setAdapter(adapter);
                }
                else if(isFetched == -1){
                    Toast.makeText(getActivity().getApplicationContext(),"Server not working.Please try after sometime!",Toast.LENGTH_SHORT).show();
                }
            }
        }



}
