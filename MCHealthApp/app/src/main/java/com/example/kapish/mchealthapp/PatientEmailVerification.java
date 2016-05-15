package com.example.kapish.mchealthapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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


public class PatientEmailVerification extends AppCompatActivity {

    private String emailVerificationCode;
    private String userEnteredKey;
    private String patientName;
    private String patientEmail;
    private String patientAge;
    private String patientAddress;
    private String patientContact;
    private String patientGender;
    private String patientPassword;

    SharedPreferences pref;
    private Boolean isSaved;
    private Boolean isPatient;

    private int isValidUser;
    // Progress Bar
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_email_verification);
        final EditText codeEditText = (EditText) findViewById(R.id.key);
                Button confirmButton= (Button)   findViewById(R.id.confirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailVerificationCode = Integer.toString(getIntent().getExtras().getInt("key"));
                patientName = getIntent().getExtras().getString("name");
                patientEmail = getIntent().getExtras().getString("email");
                patientGender = getIntent().getExtras().getString("gender");
                patientAge = Integer.toString(getIntent().getExtras().getInt("age"));
                patientAddress = getIntent().getExtras().getString("address");
                patientContact = getIntent().getExtras().getString("contact");
                patientPassword = getIntent().getExtras().getString("password");
                userEnteredKey = codeEditText.getText().toString();
                Log.i("User Enetered Key ", userEnteredKey);
                Log.i("Verification Code", "Verification Code is " + emailVerificationCode);

                if (codeEditText.getText().toString().trim().length() == 0) {
                    codeEditText.setError("Email verification code can not be blank");
                } else {
                    if (userEnteredKey.equals(emailVerificationCode)) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Correct Verification Code", Toast.LENGTH_LONG);
                     /*   Log.i("Name", patientName);
                        Log.i("Email", patientEmail);
                        Log.i("Gender", patientGender);
                        Log.i("Age", "Your age is " + patientAge);
                        Log.i("Address", patientAddress);
                        Log.i("Contact", patientContact);
                        Log.i("Password", patientPassword);
                        toast.show();*/
                        new RegisterPatient().execute();
                    } else {
                        codeEditText.setError("Please enter correct email verification code");
                        Toast toast = Toast.makeText(getApplicationContext(), "Please enter correct verification code", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }

        });
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        pref = getApplicationContext().getSharedPreferences("PatientPref", MODE_PRIVATE); // 0 - for private mode
        isSaved = pref.getBoolean("isSaved", false);
        isPatient =pref.getBoolean("isPatient",false);
        if(isSaved && isPatient)
        {
            Intent intent = new Intent(PatientEmailVerification.this, TabsPatient.class);
            intent.putExtra("name",pref.getString("patientName","NULL"));
            intent.putExtra("age",pref.getString("patientAge", "NULL"));
            intent.putExtra("gender",pref.getString("patientGender","NULL"));
            intent.putExtra("email",pref.getString("patientEmail","NULL").replaceAll("\\s+",""));
            intent.putExtra("address",pref.getString("patientAddress","NULL"));
            intent.putExtra("contact", pref.getString("patientContactNo","NULL"));
            startActivity(intent);
        }
    }
    /**
     * Background Async Task to Insert User into Database
     * */
    class RegisterPatient extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PatientEmailVerification.this);
            pDialog.setMessage("Registering Patient..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Validating User
         * */
        protected String doInBackground(String... args) {

            // Building Parameters
            HashMap<String,String> params= new HashMap<String,String>();
            params.put("email", patientEmail);
            params.put("pwd", patientPassword);
            params.put("name",patientName);
            params.put("contact",patientContact);
            params.put("address",patientAddress);
            params.put("gender",patientGender);
            params.put("age",patientAge);
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(new ServerURL().url_Register_Patient, "POST", params);
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
                Intent intent = new Intent(PatientEmailVerification.this, TabsPatient.class);
                intent.putExtra("name",patientName);
                intent.putExtra("age",patientAge);
                intent.putExtra("gender",patientGender);
                intent.putExtra("email",patientEmail);
                intent.putExtra("address",patientAddress);
                intent.putExtra("contact",patientContact);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"Your account is created successfully.",Toast.LENGTH_SHORT).show();
            }
            else if(isValidUser==0)
            {
                Context context = getApplicationContext();
                CharSequence text = "Same Email Id has already been registered with our app.";
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
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
