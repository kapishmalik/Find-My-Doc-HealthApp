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
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Calendar;
import java.util.HashMap;


public class PatientDifferentSignup extends AppCompatActivity  implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    // fields for Google Plus Login
    private static final int RC_SIGN_IN = 0;
    private GoogleApiClient mGoogleApiClient;
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private ConnectionResult mConnectionResult;
    private SignInButton GoogleSignIn;

    private String patientName;
    private String patientAge;
    private String patientGender;
    private String patientEmail;
    private String patientAddress;
    private String patientContact;
    private String patientPassword;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private Boolean isSaved;
    private Boolean isPatient;


    private ViewTarget t1;
    private ViewTarget t2;
    private ViewTarget t3;
    private int counter;
    ShowcaseView showcaseView;
    SharedPreferences settings;

    // field required for interacting with Server
    private int isRegistered;
    // Progress Bar
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_different_signup);
        GoogleSignIn       = (SignInButton) findViewById(R.id.google_sign_in_button);
        Button signInButton = (Button)       findViewById(R.id.sign_in_button);
        Button signUpButton = (Button)       findViewById(R.id.sign_up_button);
        counter =0;

        t1 = new ViewTarget(R.id.google_sign_in_button,this);
        t2 = new ViewTarget(R.id.sign_up_button,this);
        t3 = new ViewTarget(R.id.sign_in_button,this);

        SharedPreferences settings = getApplicationContext().getSharedPreferences("Pref", MODE_PRIVATE);
        if (settings.getBoolean("my_first_time_patientdifferentsignup", true)) {
            showcaseView = new ShowcaseView.Builder(PatientDifferentSignup.this)
                    .setTarget(com.github.amlcurran.showcaseview.targets.Target.NONE)
                    .setContentTitle("Patient Mode.")
                    .setContentText("Welcome Patient. \n Register/Sign to App.")
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
                                    showcaseView.setContentText("Click here to SignUp as Patient.");
                                    break;
                                case 2:
                                    showcaseView.setShowcase(t3,true);
                                    showcaseView.setContentTitle("Doctor SignIn");
                                    showcaseView.setContentText("Click here to SignIn as Patient.");
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
            settings.edit().putBoolean("my_first_time_patientdifferentsignup", false).commit();
        }



        GoogleSignIn.setOnClickListener(this);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientDifferentSignup.this, Login.class);
                intent.putExtra("userType", "patient");
                intent.putExtra("registeration","0");
                startActivity(intent);
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientDifferentSignup.this, PatientSignup.class);
                intent.putExtra("userType", "patient");
                startActivity(intent);
            }
        });


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();

   }






    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
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
            Intent intent = new Intent(PatientDifferentSignup.this, TabsPatient.class);
            intent.putExtra("name",pref.getString("patientName","NULL"));
            intent.putExtra("age",pref.getString("patientAge", "NULL"));
            intent.putExtra("gender",pref.getString("patientGender","NULL"));
            intent.putExtra("email",pref.getString("patientEmail","NULL").replaceAll("\\s+",""));
            intent.putExtra("address",pref.getString("patientAddress","NULL"));
            intent.putExtra("contact", pref.getString("patientContactNo","NULL"));
            startActivity(intent);
        }
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
    protected void onActivityResult(final int requestCode, final int responseCode,
                                    final Intent intent) {


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
                patientName = currentPerson.getDisplayName();
                patientEmail = Plus.AccountApi.getAccountName(mGoogleApiClient);
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
                patientAge=Integer.toString(age);
                if(genderInt == 0)
                {
                    patientGender="Male";
                }
                else if(genderInt == 1)
                {
                    patientGender="Female";
                }
                else
                {
                    patientGender="Other";
                }

                Log.i("INFO", patientName);
                Log.i("INFO", patientEmail);
                Log.i("INFO",patientGender);
                Log.i("INFO",patientAge);
                signOutFromGplus();
                new PatientSignIn().execute();

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
     * Revoking access from google
     * */
    private void revokeGplusAccess(){
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Log.i("INFO", "User access revoked!");
                            mGoogleApiClient.connect();

                        }

                    });
        }
    }


    /**
     * Background Async Task to Register/SignIn Patient on Server
     * */
    class PatientSignIn extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PatientDifferentSignup.this);
            pDialog.setMessage("Logging to your Account..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * SignIn Patient
         * */
        protected String doInBackground(String... args) {

            // Building Parameters
            HashMap<String,String> params= new HashMap<String,String>();
            params.put("email", patientEmail);
            params.put("name", patientName);
            params.put("age", patientAge);
            params.put("gender",patientGender);
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(new ServerURL().url_google_fb_signin, "POST", params);
            try {
                if(json != null) {
                    int success = json.getInt(TAG_SUCCESS);
                    isRegistered=success;
                    if(success == 1)
                    {
                        patientAddress="";
                        patientContact="";
                        patientPassword="";
                    }
                    else if(success == 0)
                    {
                        if(json.getString("address").equals("null"))
                        {
                            patientAddress="";
                        }
                        else
                        {
                            patientAddress=json.getString("address");
                        }
                        if(json.getString("contact").equals("null"))
                        {
                            patientContact="";
                        }
                        else
                        {
                            patientContact=json.getString("contact");
                        }
                        if(json.getString("password").equals("null"))
                        {
                            patientPassword="";
                        }
                        else
                        {
                            patientPassword=json.getString("password");
                        }
                        patientAge = json.getString("age");

                    }
                }
                else
                {
                    isRegistered=-1;
                }
            }
            catch (JSONException e) {
                isRegistered=-1;
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
            if(isRegistered==1)
            {
                Intent intent = new Intent(PatientDifferentSignup.this, TabsPatient.class);
                intent.putExtra("name",patientName);
                intent.putExtra("age",patientAge);
                intent.putExtra("gender",patientGender);
                intent.putExtra("email",patientEmail);
                intent.putExtra("address",patientAddress);
                intent.putExtra("contact",patientContact);
                startActivity(intent);
            }
            else if(isRegistered == 0)
            {
                Intent intent = new Intent(PatientDifferentSignup.this, TabsPatient.class);
                intent.putExtra("name",patientName);
                intent.putExtra("age",patientAge);
                intent.putExtra("gender",patientGender);
                intent.putExtra("email",patientEmail);
                intent.putExtra("address",patientAddress);
                intent.putExtra("contact",patientContact);
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
