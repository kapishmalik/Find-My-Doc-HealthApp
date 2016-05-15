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
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

public class DoctorEmailVerification extends AppCompatActivity {

    private String emailVerificationCode;
    private String userEnteredKey;
    private EditText codeEditText;
    private Button confirmButton;
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
        setContentView(R.layout.activity_doctor_email_verification);
         codeEditText  = (EditText) findViewById(R.id.key);
        confirmButton  = (Button)   findViewById(R.id.confirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEnteredKey = codeEditText.getText().toString();
                emailVerificationCode = Integer.toString(getIntent().getExtras().getInt("key"));
                Log.i("User Enetered Key ", userEnteredKey);
                Log.i("Verification Code", "Verification Code is " + emailVerificationCode);

                if (codeEditText.getText().toString().trim().length() == 0) {
                    codeEditText.setError("Email verification code can not be blank");
                } else {
                    if (userEnteredKey.equals(emailVerificationCode)) {
                        //do Something
                        new RegisterDoctor().execute();

                    } else {
                        codeEditText.setError("Please enter correct Email Verification Code");
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
        pref1 = getApplicationContext().getSharedPreferences("DoctorPref", MODE_PRIVATE);
        isSaved1 = pref1.getBoolean("isSaved", false);
        isDoctor = pref1.getBoolean("isDoctor", false);
        if (isSaved1 && isDoctor) {
            Intent intent = new Intent(DoctorEmailVerification.this, TabsDoc.class);
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
            pDialog = new ProgressDialog(DoctorEmailVerification.this);
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
            params.put("clinicname",getIntent().getExtras().getString("clinicname"));
            params.put("cliniccontact",getIntent().getExtras().getString("cliniccontact"));
            params.put("clinicaddress",getIntent().getExtras().getString("clinicaddress"));
            params.put("clinicfees",getIntent().getExtras().getString("clinicfees"));
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
                Intent intent = new Intent(DoctorEmailVerification.this, TabsDoc.class);
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

