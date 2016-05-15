package com.example.kapish.mchealthapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ConfirmBooking extends AppCompatActivity {

    private String clinicID;
    private String appointmentDate;
    private String doctorEmail;
    private String patientEmail;
    private  String purpose;
    private  String appointmentTime;
    private String slotId;
    private String emailMessage;
    private int isFetched;
    private int isUpdated;
    private String appointmentID;
    Session session = null;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private Boolean isSaved;
    private Boolean isPatient;

    // Progress Bar
    private ProgressDialog pDialog;

    private static final String TAG_SUCCESS = "success";

    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);

        clinicID = getIntent().getExtras().getString("clinicID");
        appointmentDate = getIntent().getExtras().getString("selectedDate");
        doctorEmail = getIntent().getExtras().getString("doctorEmail");
        patientEmail = getIntent().getExtras().getString("patientEmail");
        purpose = getIntent().getExtras().getString("purpose");
        appointmentTime = getIntent().getExtras().getString("appointmentTime");
        appointmentID   = getIntent().getExtras().getString("appointmentID");
        slotId    = getIntent().getExtras().getString("slotid");
        Log.i("INFO Confirm Booking",slotId+"  "+appointmentID+"  "+appointmentTime+":00 "+appointmentDate);
        if(getIntent().getExtras().getString("isUpdate").equals("0")) {
            new ConfirmAppointments().execute();
        }
        else
        {
            new UpdateAppointments().execute();
        }
    }

    public void onClickBookAnotherAppointment(View v) {
        /*Intent i = new Intent(this, BookAppointment.class);
        startActivity(i);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_confirm_booking, menu);
        return true;
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



    class ConfirmAppointments extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(ConfirmBooking.this);
            pDialog.setMessage("Sending Request for your Appointment...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        protected String doInBackground(String... args) {

            // Building Parameters

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("clinicid", clinicID);
            params.put("appointmentDate",appointmentDate);
            params.put("appointmentTime",appointmentTime+":00");
            params.put("doctorEmail",doctorEmail);
            params.put("patientEmail",patientEmail);
            params.put("purpose",purpose);
            params.put("slotid",slotId);

            JSONObject json = jsonParser.makeHttpRequest(new ServerURL().url_book_appointments, "POST", params);

            try {
                if (json != null) {
                    isFetched = json.getInt(TAG_SUCCESS);
                    Log.i("INFO","Value fetched :"+ isFetched);
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

            pDialog.dismiss();
            if(isFetched ==1)
            {

                emailMessage="Patient ID:"+patientEmail+" requested for Appointment on "+appointmentDate+" for time "+appointmentTime
                +". Please approve or reject the apppointment";
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

                new SendEmailForApproval().execute();
            }
            if(isFetched == 0)
            {
                Toast.makeText(getApplicationContext(), "Not able to insert", Toast.LENGTH_LONG).show();
            }
            else if(isFetched == -1)
            {
                Toast.makeText(getApplicationContext(),"Server not working",Toast.LENGTH_LONG).show();
            }
        }


    }


    class UpdateAppointments extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(ConfirmBooking.this);
            pDialog.setMessage("Sending Request for your Updated Appointment...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        protected String doInBackground(String... args) {

            // Building Parameters

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("appointmentDate",appointmentDate);
            params.put("appointmentTime",appointmentTime+":00");
            params.put("appointmentID",appointmentID);
            params.put("slotid",slotId);
            JSONObject json = jsonParser.makeHttpRequest(new ServerURL().url_update_appointments, "POST", params);
            try {
                if (json != null) {
                    isUpdated= json.getInt(TAG_SUCCESS);

                }
            }


            catch (JSONException e) {
                isUpdated = -1;
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {

            pDialog.dismiss();
            if(isUpdated ==1)
            {

                emailMessage="Patient ID:"+patientEmail+" requested for Reschedule of Appointment on "+appointmentDate+" for time "+appointmentTime
                        +". Please approve or reject the apppointment";
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

                new SendEmailForApproval().execute();
            }
            if(isUpdated == 0)
            {
                Toast.makeText(getApplicationContext(), "Not able to insert", Toast.LENGTH_LONG).show();
            }
            else if(isUpdated == -1)
            {
                Toast.makeText(getApplicationContext(),"Server not working",Toast.LENGTH_LONG).show();
            }
        }


    }

    class SendEmailForApproval extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ConfirmBooking.this);
            pDialog.setMessage("Sending Email to Doctor..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("doctorandpatientappointment@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(doctorEmail));
                message.setSubject("Approval Required for Appointment on"+appointmentDate+"  at time "+appointmentTime);
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
            Toast toast =Toast.makeText(getApplicationContext(),"Mail has been sent to Doctor",Toast.LENGTH_LONG);
            toast.show();
            pref = getApplicationContext().getSharedPreferences("PatientPref", MODE_PRIVATE); // 0 - for private mode
            isSaved = pref.getBoolean("isSaved", false);
            isPatient = pref.getBoolean("isPatient", false);
            if (isSaved && isPatient) {
                Intent intent = new Intent(ConfirmBooking.this, TabsPatient.class);
                intent.putExtra("name", pref.getString("patientName", "NULL"));
                intent.putExtra("age", pref.getString("patientAge", "NULL"));
                intent.putExtra("gender", pref.getString("patientGender", "NULL"));
                intent.putExtra("email", pref.getString("patientEmail", "NULL").replaceAll("\\s+", ""));
                intent.putExtra("address", pref.getString("patientAddress", "NULL"));
                intent.putExtra("contact", pref.getString("patientContactNo", "NULL"));
                startActivity(intent);
            }

        }


    }
}
