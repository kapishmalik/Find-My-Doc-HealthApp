package com.example.kapish.mchealthapp;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

public class Login extends AppCompatActivity {
    private String emailId;
    private String pwd;
    private String userType;
    private EditText email;
    private EditText password;
    private Button login;


    SharedPreferences pref;
    private Boolean isSaved;
    private Boolean isPatient;

    SharedPreferences pref1;
    private Boolean isSaved1;
    private Boolean isDoctor;



    //is set 1 if email id and pwd are correctly validated on server
    private int isValidUser;
    // Progress Bar
    private ProgressDialog pDialog;
    private static final String TAG_SUCCESS = "success";

    private String userName;
    private String userGender;
    private String userAge;
    private String userEmail;
    private String userContact;
    private String userAddress;
    private String userUGDegree;
    private String userPGDegree;
    private String userOtherDegree;
    private String userSpeciality;
    private String userMCINo;
    private String userYrsOfExp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //Initialize
        isValidUser=-2;
        userType=getIntent().getExtras().getString("userType");


        //Initialize UI elements
        email = (EditText) findViewById(R.id.email);
        password= (EditText) findViewById(R.id.password);
        login=(Button)findViewById(R.id.sign_in_button);
        final TextView forgotPwdTextView= (TextView) findViewById(R.id.ForgotPwdTextView);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((email.getText().toString().trim().length() == 0)) {
                    email.setError("Email Id can not be blank");
                } else if ((password.getText().toString().trim().length() == 0)) {
                    password.setError("Password can not be blank");
                } else {
                    emailId = email.getText().toString();
                    pwd = password.getText().toString();
                    Log.i("INFO Login",emailId);
                    Log.i("INFO Login",pwd);
                    String emailRegex = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                    Boolean emailFormat = emailId.matches(emailRegex);
                    if (!emailFormat) {
                        email.setError("Email id is not in correct format");
                    } else if (pwd.length() <= 4) {
                        password.setError("Password Length should be more than 4");
                    } else {

                        new ValidateUser().execute();
                    }

                }
            }
        });

        forgotPwdTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, RecoverPassword.class);
                intent.putExtra("userType",userType);
                startActivity(intent);

            }

        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        pref = getApplicationContext().getSharedPreferences("PatientPref", MODE_PRIVATE); // 0 - for private mode
        isSaved = pref.getBoolean("isSaved", false);
        isPatient = pref.getBoolean("isPatient", false);
        if (isSaved && isPatient) {
                Intent intent = new Intent(Login.this, TabsPatient.class);
                intent.putExtra("name", pref.getString("patientName", "NULL"));
                intent.putExtra("age", pref.getString("patientAge", "NULL"));
                intent.putExtra("gender", pref.getString("patientGender", "NULL"));
                intent.putExtra("email", pref.getString("patientEmail", "NULL").replaceAll("\\s+", ""));
                intent.putExtra("address", pref.getString("patientAddress", "NULL"));
                intent.putExtra("contact", pref.getString("patientContactNo", "NULL"));
                startActivity(intent);
            }
        pref1 = getApplicationContext().getSharedPreferences("DoctorPref", MODE_PRIVATE);
        isSaved1 = pref1.getBoolean("isSaved", false);
        isDoctor = pref1.getBoolean("isDoctor", false);
        if (isSaved1 && isDoctor) {
            Intent intent = new Intent(Login.this, TabsDoc.class);
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
     * Background Async Task to Validate User from Server
     * */
    class ValidateUser extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Logging to your Account..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            Log.i("INFO Login 2nd Time", emailId);
            Log.i("INFO Login 2nd Time",pwd);
        }

        /**
         * Validating User
         * */
        protected String doInBackground(String... args) {

            // Building Parameters
            HashMap<String,String> params= new HashMap<String,String>();
            params.put("email", emailId);
            params.put("pwd", pwd);
            params.put("table",userType);
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONParser jsonParser = new JSONParser();
            JSONObject json = jsonParser.makeHttpRequest(new ServerURL().url_Validate_User, "POST", params);
            try {
                if(json != null) {
                    int success = json.getInt(TAG_SUCCESS);
                    isValidUser = success;
                    if(isValidUser == 1)
                    {
                        if(userType.equals("doctor"))
                        {
                            userName       =  json.getString("name");
                            userEmail      =  emailId;
                            userAge        =  json.getString("age");
                            userGender     =  json.getString("gender");
                            userMCINo      =  json.getString("MCINo");
                            userUGDegree   =  json.getString("ugDegree");
                            userPGDegree   =  json.getString("pgDegree");
                            userOtherDegree=  json.getString("otherDegree");
                            userSpeciality =  json.getString("speciality");
                            userYrsOfExp   =  json.getString("YrsOfExp");


                        }
                        else
                        {

                            userName     = json.getString("name");
                            userEmail    = emailId;
                            userAge      = json.getString("age");
                            userGender   = json.getString("gender");
                            userContact  = json.getString("contact");
                            userAddress  = json.getString("address");

                        }
                    }
                }
                else
                {
                    isValidUser=-2;
                }
            }
            catch (JSONException e) {
                isValidUser=-2;
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

                if(userType.equals("doctor"))
                {
                    Log.i("INFO", userName + " " + userAge + " " + userUGDegree + " " + userPGDegree + " " + userOtherDegree
                            + " " + userSpeciality + " " + userMCINo + " " + userGender + " Experience: " + userYrsOfExp + " " + userEmail);

                    Intent intent = new Intent(Login.this, TabsDoc.class);
                    intent.putExtra("name","Dr. "+userName);
                    intent.putExtra("age",userAge);
                    intent.putExtra("gender",userGender);
                    intent.putExtra("email",userEmail);
                    intent.putExtra("qualification",userUGDegree+","+userPGDegree+","+userOtherDegree);
                    intent.putExtra("speciality",userSpeciality);
                    intent.putExtra("MCINo",userMCINo);
                    intent.putExtra("YrsOfExp",userYrsOfExp);
                    startActivity(intent);

                }
                else
                {
                    Log.i("INFO",userName+" "+userAge+" "+userAddress+" "+userContact+
                            " "+userGender+" "+userEmail);
                    Intent intent = new Intent(Login.this, TabsPatient.class);
                    intent.putExtra("name",userName);
                    intent.putExtra("age",userAge);
                    intent.putExtra("gender",userGender);
                    intent.putExtra("email",userEmail);
                    intent.putExtra("address",userAddress);
                    intent.putExtra("contact",userContact);
                    startActivity(intent);
                }
            }
            else if(isValidUser==0)
            {
                Context context = getApplicationContext();
                CharSequence text = "Please enter correct password.";
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                password.setError("Please enter correct password");
            }
            else if(isValidUser==-1)
            {
                Context context = getApplicationContext();
                CharSequence text = "This Email Id is not present in our Database.";
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                email.setError("This Email Id is not present in our Database.");
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
