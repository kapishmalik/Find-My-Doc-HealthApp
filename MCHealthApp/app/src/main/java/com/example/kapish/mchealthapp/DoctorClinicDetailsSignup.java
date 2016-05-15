package com.example.kapish.mchealthapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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


public class DoctorClinicDetailsSignup extends AppCompatActivity {

    private EditText nameEditText;
    private EditText addressEditText;
    private EditText contactEditText;
    private EditText feesEditText;
    private Button   clearButton;
    private Button   signUpButton;
    GPSTracker gpsAddressFinder;

    private String clinicName;
    private String clinicAddress;
    private String clinicContact;
    private String clinicFees;

    //fields for sending Message
    private String emailSubject;
    private String emailMessage;
    Session session = null;
    private int emailVerificationCode;

    private int isValidUser;
    // Progress Bar
    private ProgressDialog pDialog;

    private static final String TAG_SUCCESS = "success";

    SharedPreferences pref1;
    private Boolean isSaved1;
    private Boolean isDoctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_clinic_details_signup);
        nameEditText    = (EditText) findViewById(R.id.clinicname);
        addressEditText = (EditText) findViewById(R.id.clinicaddress);
        contactEditText = (EditText) findViewById(R.id.contact);
        feesEditText    = (EditText) findViewById(R.id.fees);
        clearButton     = (Button)   findViewById(R.id.clear);
        signUpButton    = (Button)   findViewById(R.id.sign_up_button);
        gpsAddressFinder= new GPSTracker(DoctorClinicDetailsSignup.this);
        addressEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if(gpsAddressFinder.canGetLocation()){

                        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
                        String locality=gpsAddressFinder.getLocality(gcd);
                        Log.i("INFO", locality);
                        addressEditText.setText(locality);
                        addressEditText.setError(null);
                    }else{
                        gpsAddressFinder.showSettingsAlert();
                    }

                }
            }
                });
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence str = "";
                nameEditText.setText(str);
                addressEditText.setText(str);
                contactEditText.setText(str);
                feesEditText.setText(str);
                nameEditText.clearFocus();
                addressEditText.clearFocus();
                contactEditText.clearFocus();
                feesEditText.clearFocus();
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((nameEditText.getText().toString().trim().length() == 0)) {
                    nameEditText.setError("Clinic Name can not be blank");
                }
                else if ((addressEditText.getText().toString().trim().length() == 0)) {
                    addressEditText.setError("Clinic Address can not be blank");
                }
                else if ((contactEditText.getText().toString().trim().length() == 0)) {
                    contactEditText.setError("Contact No. can not be blank");
                }
                else if ((feesEditText.getText().toString().trim().length() == 0)) {
                    feesEditText.setError("Fees can not be blank");
                }
                else
                {
                    clinicName=nameEditText.getText().toString();
                    clinicAddress=addressEditText.getText().toString();
                    clinicContact=contactEditText.getText().toString();
                    clinicFees=feesEditText.getText().toString();
                    //do Something
                    if(getIntent().getExtras().getString("fromActivity")!=null) {

                        if (getIntent().getExtras().getString("fromActivity").equals("1")) {
                            Log.i("INFO", "From SignUp Page. Requires Authentication");
                            emailSubject = "Email from EAppointment App Email Verification";
                            Random random=new Random();
                            emailVerificationCode=random.nextInt(9000)+1000;
                            emailMessage="Email Verification Code is "+emailVerificationCode+" .";
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

                            new SendEmailVerificationCode().execute();

                        }
                        else
                        {
                            Log.i("INFO","From Google Plus Page. No need of Authentication");
                            new RegisterDoctor().execute();

                        }
                    }
                    else
                    {
                        //do Something
                        Log.i("INFO","Intent info value is null");
                    }

                }
            }
        });


    }

    @Override
    protected void onResume()
    {
        super.onResume();
        pref1 = getApplicationContext().getSharedPreferences("DoctorPref", MODE_PRIVATE);
        isSaved1 = pref1.getBoolean("isSaved", false);
        isDoctor = pref1.getBoolean("isDoctor", false);
        if (isSaved1 && isDoctor) {
            Intent intent = new Intent(DoctorClinicDetailsSignup.this, TabsDoc.class);
            intent.putExtra("name",pref1.getString("docName","NULL"));
            intent.putExtra("age",pref1.getString("docAge","0"));
            intent.putExtra("gender",pref1.getString("docGender","NULL"));
            intent.putExtra("email",pref1.getString("docEmail","NULL"));
            intent.putExtra("qualification",pref1.getString("docQualification","NULL"));
            intent.putExtra("speciality",pref1.getString("docSpeciality","NULL"));
            intent.putExtra("MCINo",pref1.getString("docMCINo","NULL"));
            intent.putExtra("YrsOfExp",pref1.getString("docYrsOfExp","0"));
            startActivity(intent);
        }

    }

    class SendEmailVerificationCode extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DoctorClinicDetailsSignup.this);
            pDialog.setMessage("Verifying Email..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("doctorandpatientappointment@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(getIntent().getExtras().getString("email")));
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
            Toast toast = Toast.makeText(getApplicationContext(), "Please check your email for Email Verification Code", Toast.LENGTH_LONG);
            toast.show();
            Intent intent = new Intent(DoctorClinicDetailsSignup.this, DoctorEmailVerification.class);
            intent.putExtra("name",getIntent().getExtras().getString("name"));
            intent.putExtra("email",getIntent().getExtras().getString("email"));
            intent.putExtra("password",getIntent().getExtras().getString("password"));
            intent.putExtra("gender",getIntent().getExtras().getString("gender"));
            intent.putExtra("YrsOfExp",getIntent().getExtras().getString("YrsOfExp"));
            intent.putExtra("age",getIntent().getExtras().getString("age"));
            intent.putExtra("fromActivity",getIntent().getExtras().getString("fromActivity"));
            intent.putExtra("ugDegree",getIntent().getExtras().getString("ugDegree"));
            intent.putExtra("pgDegree",getIntent().getExtras().getString("pgDegree"));
            intent.putExtra("otherDegree",getIntent().getExtras().getString("otherDegree"));
            intent.putExtra("speciality",getIntent().getExtras().getString("speciality"));
            intent.putExtra("MCINo",getIntent().getExtras().getString("MCINo"));
            intent.putExtra("clinicname",clinicName);
            intent.putExtra("cliniccontact",clinicContact);
            intent.putExtra("clinicaddress",clinicAddress);
            intent.putExtra("clinicfees",clinicFees);
            intent.putExtra("key", emailVerificationCode);
            startActivity(intent);
        }

    }

    /**
     * Background Async Task to Insert Doctor Details into Database
     * */
    class RegisterDoctor extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DoctorClinicDetailsSignup.this);
            pDialog.setMessage("Signing Up..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Inserting Details
         * */
        protected String doInBackground(String... args) {

            // Building Parameters
            HashMap<String,String> params= new HashMap<String,String>();
            params.put("email", getIntent().getExtras().getString("email").toLowerCase());
            params.put("pwd", getIntent().getExtras().getString("password"));
            params.put("name",getIntent().getExtras().getString("name"));
            params.put("gender",getIntent().getExtras().getString("gender"));
            params.put("age",getIntent().getExtras().getString("age"));
            params.put("YrsOfExp",getIntent().getExtras().getString("YrsOfExp"));
            params.put("ugDegree",getIntent().getExtras().getString("ugDegree"));
            params.put("pgDegree",getIntent().getExtras().getString("pgDegree"));
            params.put("otherDegree",getIntent().getExtras().getString("otherDegree"));
            params.put("speciality",getIntent().getExtras().getString("speciality"));
            params.put("MCINo",getIntent().getExtras().getString("MCINo"));
            params.put("clinicname",clinicName);
            params.put("cliniccontact",clinicContact);
            params.put("clinicaddress",clinicAddress);
            params.put("clinicfees",clinicFees);
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONParser jsonParser = new JSONParser();
            JSONObject json = jsonParser.makeHttpRequest(new ServerURL().url_Register_Doctor, "POST", params);
            try {
                if(json != null) {
                    int success = json.getInt(TAG_SUCCESS);
                    isValidUser = success;
                }
                else
                {
                    isValidUser=-1;
                }
            }
            catch (JSONException e) {
                isValidUser=-1;
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
            if(isValidUser==1)
            {
                Intent intent = new Intent(DoctorClinicDetailsSignup.this, TabsDoc.class);
                intent.putExtra("name","Dr. "+getIntent().getExtras().getString("name"));
                intent.putExtra("age",getIntent().getExtras().getString("age"));
                intent.putExtra("gender",getIntent().getExtras().getString("gender"));
                intent.putExtra("email",getIntent().getExtras().getString("email"));
                intent.putExtra("qualification",getIntent().getExtras().getString("ugDegree")+","
                        +getIntent().getExtras().getString("pgDegree")+","
                       +getIntent().getExtras().getString("otherDegree"));
                intent.putExtra("speciality",getIntent().getExtras().getString("speciality"));
                intent.putExtra("MCINo",getIntent().getExtras().getString("MCINo"));
                intent.putExtra("YrsOfExp", getIntent().getExtras().getString("YrsOfExp"));
                startActivity(intent);
            }
            else
            {
                Context context = getApplicationContext();
                CharSequence text = "Server not working.Please try again later.";
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }

    }
}

