package com.example.kapish.mchealthapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;


import org.json.JSONException;
import org.json.JSONObject;


import java.util.Calendar;
import java.util.HashMap;


public class DoctorDifferentSignup extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    // fields for Google Plus Login
    private static final int RC_SIGN_IN = 0;
    private GoogleApiClient mGoogleApiClient;
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private ConnectionResult mConnectionResult;
    private SignInButton GoogleSignIn;

    private String doctorName;
    private String doctorEmail;
    private String doctorGender;
    private String doctorAge;
    private String doctorPassword;
    private String doctorYrsOfExp;
    private String doctorUGDegree;
    private String doctorPGDegree;
    private String doctorOtherDegree;
    private String doctorSpeciality;
    private String doctorMCINo;

    // field required for interacting with Server
    private int isAlreadyRegistered;
    // Progress Bar
    private ProgressDialog pDialog;
    private static final String TAG_SUCCESS = "success";

    SharedPreferences pref1;
    private Boolean isSaved1;
    private Boolean isDoctor;

    private ViewTarget t1;
    private ViewTarget t2;
    private ViewTarget t3;
    ShowcaseView showcaseView;
    SharedPreferences settings;
    int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_different_signup);
        GoogleSignIn       = (SignInButton) findViewById(R.id.google_sign_in_button);
        Button signUpButton = (Button)       findViewById(R.id.sign_up_button);
        Button signInButton = (Button)       findViewById(R.id.sign_in_button);
        GoogleSignIn.setOnClickListener(this);

        t1 = new ViewTarget(R.id.google_sign_in_button,this);
        t2 = new ViewTarget(R.id.sign_up_button,this);
        t3 = new ViewTarget(R.id.sign_in_button,this);

        SharedPreferences settings = getApplicationContext().getSharedPreferences("Pref", MODE_PRIVATE);
        if (settings.getBoolean("my_first_time_drdifferentsignup", true)) {
            showcaseView = new ShowcaseView.Builder(DoctorDifferentSignup.this)
                    .setTarget(com.github.amlcurran.showcaseview.targets.Target.NONE)
                    .setContentTitle("Doctor Mode.")
                    .setContentText("Welcome Dr. \n Register/Sign to App.")
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch(counter)
                            {
                                case 0:
                                    showcaseView.setShowcase(t1,true);
                                    showcaseView.setContentTitle("Google SignIn");
                                    showcaseView.setContentText("Click here to Sign In using your Google Account.");
                                    break;
                                case 1:
                                    showcaseView.setShowcase(t2, true);
                                    showcaseView.setContentTitle("Doctor SignUp");
                                    showcaseView.setContentText("Click here to SignUp as Doctor.");
                                    break;
                                case 2:
                                    showcaseView.setShowcase(t3,true);
                                    showcaseView.setContentTitle("Doctor SignIn");
                                    showcaseView.setContentText("Click here to SignIn as Doctor.");
                                    break;
                                case 3:
                                    showcaseView.hide();
                                    break;

                            }
                            counter++;
                        }
                    })
                    .setStyle(R.style.AppTheme1)
                    .build();
            settings.edit().putBoolean("my_first_time_drdifferentsignup", false).commit();
        }



        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorDifferentSignup.this, DoctorGeneralInfoSignup.class);
                startActivity(intent);
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorDifferentSignup.this, Login.class);
                intent.putExtra("userType", "doctor");
                intent.putExtra("registeration","0");
                startActivity(intent);
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
    }

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
    @Override
    public void onClick(View v) {
        signInWithGplus();

    }
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }

    }


    @Override
    protected void onResume()
    {
        super.onResume();
        pref1 = getApplicationContext().getSharedPreferences("DoctorPref", MODE_PRIVATE);
        isSaved1 = pref1.getBoolean("isSaved", false);
        isDoctor = pref1.getBoolean("isDoctor", false);
        if (isSaved1 && isDoctor) {
            Intent intent = new Intent(DoctorDifferentSignup.this, TabsDoc.class);
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

    @Override
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnected(Bundle arg0) {
        mSignInClicked = false;
        // Get patient information
        getProfileInformation();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();

    }
    /**
     * Sign-in into google
     * */
    private void signInWithGplus() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }
    }

    /**
     * Method to resolve any signin errors
     * */
    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }



    private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                doctorName = currentPerson.getDisplayName();
                doctorEmail = Plus.AccountApi.getAccountName(mGoogleApiClient);
                int genderInt = currentPerson.getGender();
                int age;
                if (currentPerson.hasBirthday())
                {
                    String Birthday = currentPerson.getBirthday();
                    String delimiter = "-";
                    String[] temp = Birthday.split(delimiter);
                    int birthYear=Integer.parseInt(temp[0]);
                    final Calendar myCalendar = Calendar.getInstance();
                    int currentYear = myCalendar.get(Calendar.YEAR);
                    age=currentYear-birthYear;

                }
                else
                {
                    age=0;
                }
                doctorAge=Integer.toString(age);
                if(genderInt == 0)
                {
                    doctorGender="Male";
                }
                else if(genderInt == 1)
                {
                    doctorGender="Female";
                }
                else
                {
                    doctorGender="Other";
                }

                Log.i("INFO", doctorName);
                Log.i("INFO", doctorEmail);
                Log.i("INFO",doctorAge);
                Log.i("INFO",doctorGender);
                doctorPassword="null";
                doctorYrsOfExp="0";
                new DoctorGoogleFBSignIn().execute();
                signOutFromGplus();


            } else {
                Toast.makeText(getApplicationContext(),
                        "Unable to Login. Unable to fetch Patient Information.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void signOutFromGplus() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();

        }
    }

    /**
     * Background Async Task to SignIn Doctor on Server
     * */
    class DoctorGoogleFBSignIn extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DoctorDifferentSignup.this);
            pDialog.setMessage("Verifying Doctor..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Verifying and SignIn Doctor
         * */
        protected String doInBackground(String... args) {

            // Building Parameters
            HashMap<String,String> params= new HashMap<String,String>();
            params.put("email", doctorEmail);
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONParser jsonParser = new JSONParser();
            JSONObject json = jsonParser.makeHttpRequest(new ServerURL().url_doctor_google_fb_signin, "POST", params);
            try {
                if(json != null) {
                    int success = json.getInt(TAG_SUCCESS);
                    isAlreadyRegistered=success;
                    if(success==1)
                    {
                        doctorPassword=json.getString("password");
                        if(doctorPassword.equals("null"))
                        {
                            doctorPassword=" ";
                        }
                        doctorYrsOfExp    = json.getString("YrsOfExp");
                        doctorMCINo       = json.getString("MCINo");
                        doctorUGDegree    = json.getString("ugDegree");
                        doctorPGDegree    = json.getString("pgDegree");
                        doctorOtherDegree = json.getString("otherDegree");
                        doctorSpeciality  = json.getString("speciality");
                    }
                  }

                else
                {
                    isAlreadyRegistered=-1;
                }
            }
            catch (JSONException e) {
                isAlreadyRegistered=-1;
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
            if(isAlreadyRegistered==1)
            {
                Intent intent = new Intent(DoctorDifferentSignup.this, TabsDoc.class);
                intent.putExtra("name","Dr. "+doctorName);
                intent.putExtra("age",doctorAge);
                intent.putExtra("gender",doctorGender);
                intent.putExtra("email",doctorEmail);
                intent.putExtra("qualification",doctorUGDegree+","+doctorPGDegree+","+doctorOtherDegree);
                intent.putExtra("speciality",doctorSpeciality);
                intent.putExtra("MCINo",doctorMCINo);
                intent.putExtra("YrsOfExp", doctorYrsOfExp);
                startActivity(intent);

            }
            else if(isAlreadyRegistered == 0)
            {
                Intent intent = new Intent(DoctorDifferentSignup.this, DoctorQualificationSignup.class);
                intent.putExtra("name", doctorName);
                intent.putExtra("email", doctorEmail);
                intent.putExtra("password", doctorPassword);
                intent.putExtra("gender", doctorGender);
                intent.putExtra("YrsOfExp", doctorYrsOfExp);
                intent.putExtra("age", doctorAge);
                intent.putExtra("fromActivity", "0");
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
