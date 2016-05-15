package com.example.kapish.mchealthapp;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ApprovalList extends Fragment  {
        ListView l;
        List<AppointmentsDoc> my_list=new ArrayList<AppointmentsDoc>();
        MyListAdapter<AppointmentsDoc> adapter;
        List<String> clinics=new ArrayList<String>();
        String docEmail ;
        String docName;
        String clinicName ;
        String patientEmail;
        private static final String TAG_SUCCESS = "success";
        private int check;
        private int check1;
        JSONArray arr;
        JSONArray cArr;
        Spinner spinner;
        String appointmentDate;
        String appointmenttime;

    // Progress Bar
    private ProgressDialog pDialog;

    private String emailSubject;
    private String emailMessage;
    Session session = null;


    void check(String docEmail,String docName)
        {
            this.docEmail=docEmail;
            this.docName =docName;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View v=inflater.inflate(R.layout.fragment_approval_list__doc, container, false);
            l= (ListView) v.findViewById(R.id.Appointments);
            adapter=new MyListAdapter<>();
            l.setAdapter(adapter);
            spinner = (Spinner) v.findViewById(R.id.spinnerClinics2);
            new DoctorClinics().execute();
            //l.setOnItemClickListener(this);

            //my_list.add(new AppointmentsDoc("1", "12/08/2015", "Anil Kumar", "12:15 am", "Approved"));
            //my_list.add(new AppointmentsDoc("2","16/12/2015","Meetika","02:30 pm","Approved"));
            //my_list.add(new AppointmentsDoc("3","20/10/2015","Karan Khanna","4:15 pm","Rejected"));
            //my_list.add(new AppointmentsDoc("4","14/11/2015","Preeti","03:00 pm","Pending"));
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



        private class MyListAdapter<A> extends ArrayAdapter<AppointmentsDoc> {

            public MyListAdapter() {
                super(getActivity(), R.layout.layout_appointments_doc_list_item, my_list);
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View itemView = convertView;
                if (itemView == null) {
                    itemView = getActivity().getLayoutInflater().inflate(R.layout.layout_appointments_doc_list_item, parent, false);
                }

                final AppointmentsDoc currentAppointment = my_list.get(position);


                TextView clinicname = (TextView) itemView.findViewById(R.id.textViewIDText);
                clinicname.setText(currentAppointment.getIconID());

                TextView pat_name = (TextView) itemView.findViewById(R.id.textViewPatNameText);
                pat_name.setText(currentAppointment.getPat_name());

                TextView date = (TextView) itemView.findViewById(R.id.textViewSlotDate);
                date.setText(currentAppointment.getDate());

                TextView time = (TextView) itemView.findViewById(R.id.textViewSlotTime);
                time.setText(currentAppointment.getSlot());

                TextView purpose = (TextView) itemView.findViewById(R.id.textViewPurposeText);
                purpose.setText(currentAppointment.getStatus());


                final int pos = position;
                Button approve = (Button)itemView.findViewById(R.id.buttonApprove);
                approve.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        appointmentDate = my_list.get(pos).getDate();
                        appointmenttime = my_list.get(pos).getSlot();
                        patientEmail = currentAppointment.getPat_name();
                        new Approve().execute();
                        my_list.remove(pos);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getActivity().getApplicationContext(), "Approved! for "+currentAppointment.getPat_name(), Toast.LENGTH_SHORT).show();
                    }
                });

                Button reject = (Button)itemView.findViewById(R.id.buttonReject);
                reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        appointmentDate = my_list.get(pos).getDate();
                        appointmenttime = my_list.get(pos).getSlot();
                        patientEmail = currentAppointment.getPat_name();
                        new Reject().execute();
                        my_list.remove(pos);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getActivity().getApplicationContext(), "Rejected!"+currentAppointment.getPat_name(), Toast.LENGTH_SHORT).show();
                    }
                });
                return itemView;
            }
        }
    class DoctorAppointments extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("email", docEmail);
            params.put("clinic_name", clinicName);
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONParser jsonParser1 = new JSONParser();
            JSONObject json1 = jsonParser1.makeHttpRequest(new ServerURL().url_Doctor_Appointments, "POST", params);
            try {
                if (json1 != null) {
                    int success = json1.getInt(TAG_SUCCESS);
                    check1 = success;
                    if (success == 1) {
                        check1 = 1;
                        List<AppointmentsDoc> temp = new ArrayList<AppointmentsDoc>();
                        arr = json1.getJSONArray("appointments");
                        if(arr!=null) {

                            for (int i = 0; i < arr.length(); i++) {  // **line 2**
                                JSONObject childJSONObject = arr.getJSONObject(i);
                                String date = childJSONObject.getString("appointmentdate");
                                String time = childJSONObject.getString("appointmenttime");
                                String purpose = childJSONObject.getString("purpose");
                                String clinicname = childJSONObject.getString("clinicname");
                                String patname = childJSONObject.getString("patname");
                                temp.add(new AppointmentsDoc(clinicname,date,patname,time,purpose));
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
            // dismiss the dialog once done
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

               // Toast.makeText(getActivity().getApplicationContext(), "good", Toast.LENGTH_SHORT);
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
                        } else {

                            clinicName = spinner.getItemAtPosition(i).toString();
                            Log.i("INFO Doctor Approval", clinicName+"  "+"I am called to get names");
                            new DoctorAppointments().execute();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }


                });
              //  Toast.makeText(getActivity().getApplicationContext(),"good",Toast.LENGTH_SHORT).show();
            }
        }

    }

    class Approve extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> params = new HashMap<String,String>();
            params.put("appointmentdate",appointmentDate );
            params.put("appointmentslot",appointmenttime);

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONParser jsonParser2 = new JSONParser();
            JSONObject json = jsonParser2.makeHttpRequest(new ServerURL().url_Approve, "POST",params);
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
                Toast.makeText(getActivity().getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
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
                emailSubject = "Appointment Approval from  "+docName;
                emailMessage="Your Appointment has been Approved for date "+appointmentDate+" at time"+appointmenttime;
                Properties props = new Properties();
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.socketFactory.port", "465");
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.port", "465");

                session = Session.getDefaultInstance(props, new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("doctorandpatientappointment@gmail.com", "topper1234");
                    }
                });

                new SendEmail().execute();

            }
        }

    }


    class Reject extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> params = new HashMap<String,String>();
            params.put("appointmentdate",appointmentDate );
            params.put("appointmentslot",appointmenttime);

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONParser jsonParser2 = new JSONParser();
            JSONObject json = jsonParser2.makeHttpRequest(new ServerURL().url_Reject, "POST",params);
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
                Toast.makeText(getActivity().getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
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

                emailSubject = "Appointment Rejected from  "+docName;
                emailMessage="Your Appointment has been Rejected for date "+appointmentDate+" at time"+appointmenttime+" .Please reschedule your appointment again.";
                Properties props = new Properties();
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.socketFactory.port", "465");
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.port", "465");

                session = Session.getDefaultInstance(props, new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("doctorandpatientappointment@gmail.com", "topper1234");
                    }
                });

                new SendEmail().execute();

                // Toast.makeText(getActivity().getApplicationContext(),"good",Toast.LENGTH_SHORT).show();
            }
        }

    }


    class SendEmail extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Sending Email to Patient..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("doctorandpatientappointment@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(patientEmail));
                message.setSubject(emailSubject);
                message.setContent(emailMessage, "text/html; charset=utf-8");
                Transport.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            pDialog.dismiss();

        }


    }
}







