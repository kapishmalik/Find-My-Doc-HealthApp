package com.example.kapish.mchealthapp;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Calendar;
import java.util.HashMap;

public class DoctorGeneralInfoSignup extends AppCompatActivity {


    private Button NextButton;
    private Button clearButton;
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText expEditText;
    private EditText dobEditText;
    private EditText pwdEditText;
    private String doctorName;
    private String doctorEmail;
    private String doctorYrsOfExp;
    private String doctorPassword;
    private String doctorGender;
    private String doctorAge;
    private int isAlreadyRegistered;
    private int currentYear;
    private int dobYear;
    private int age;
    private int counter;


    // Progress Bar
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_general_info_signup);

        counter =0;

        //Initialize UI Element
        nameEditText = (EditText) findViewById(R.id.name);
        emailEditText = (EditText) findViewById(R.id.email);
        expEditText = (EditText) findViewById(R.id.yearsOfExp);
        dobEditText = (EditText) findViewById(R.id.dob);
        pwdEditText = (EditText) findViewById(R.id.password);
        NextButton = (Button) findViewById(R.id.save_and_next);
        clearButton = (Button) findViewById(R.id.clear);
        RadioGroup genderRadioGroup = (RadioGroup) findViewById(R.id.radioSex);
        doctorGender = ((RadioButton) findViewById(genderRadioGroup.getCheckedRadioButtonId())).getText().toString();

        final Calendar myCalendar = Calendar.getInstance();
        currentYear = myCalendar.get(Calendar.YEAR);
        //t1 = new ViewTarget(R.id.clear,this);
        //t2 = new ViewTarget(R.id.save_and_next,this);
        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                doctorGender = ((RadioButton) findViewById(checkedId)).getText().toString();
            }
        });
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub

                dobEditText.setText(dayOfMonth + " / " + (monthOfYear + 1) + " / "
                        + year);
                dobYear = year;
                age = currentYear - dobYear;
                dobEditText.setError(null);

            }

        };
        dobEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                                 @Override
                                                 public void onFocusChange(View v, boolean hasFocus) {
                                                     if (hasFocus) {
                                                         new DatePickerDialog(DoctorGeneralInfoSignup.this, date, myCalendar
                                                                 .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                                                 myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                                                     }
                                                 }
                                             }
        );

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence str = "";
                nameEditText.setText(str);
                expEditText.setText(str);
                emailEditText.setText(str);
                dobEditText.setText(str);
                pwdEditText.setText(str);
                nameEditText.clearFocus();
                expEditText.clearFocus();
                emailEditText.clearFocus();
                dobEditText.clearFocus();
                pwdEditText.clearFocus();

            }
        });

        NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("INFO", "Next Button pressed");
                if ((nameEditText.getText().toString().trim().length() == 0)) {
                    nameEditText.setError("Name can not be blank");
                } else if ((emailEditText.getText().toString().trim().length() == 0)) {
                    emailEditText.setError("Email Id can not be blank");
                } else if ((dobEditText.getText().toString().trim().length() == 0)) {
                    dobEditText.setError("Date of Birth can not be blank");
                } else if ((expEditText.getText().toString().trim().length() == 0)) {
                    expEditText.setError("Years of Experience can not be blank");
                } else if ((pwdEditText.getText().toString().trim().length() == 0)) {
                    pwdEditText.setError("Password can not be blank");
                } else {

                    doctorEmail = emailEditText.getText().toString();
                    doctorPassword = pwdEditText.getText().toString();
                    doctorName = nameEditText.getText().toString();
                    doctorYrsOfExp = expEditText.getText().toString();
                    doctorAge = Integer.toString(age);
                    String emailRegex = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                    Boolean emailFormat = doctorEmail.matches(emailRegex);
                    if (!emailFormat) {
                        emailEditText.setError("Email id is not in correct format");
                    } else if (doctorPassword.length() <= 4) {
                        pwdEditText.setError("Password Length should be more than 4");
                    } else {

                        Log.i("INFO", doctorName);
                        Log.i("INFO", doctorEmail);
                        Log.i("INFO", doctorYrsOfExp);
                        Log.i("INFO", doctorGender);
                        Log.i("INFO", "Your age is " + doctorAge);
                        Log.i("INFO", doctorPassword);
                         new CheckDoctorEmail().execute();
                    }

                }
            }
        });

       /* if (settings.getBoolean("my_first_time", true)) {
            showcaseView = new ShowcaseView.Builder(DoctorGeneralInfoSignup.this)
                    .setTarget(com.github.amlcurran.showcaseview.targets.Target.NONE)
                    .setContentTitle("Book Your Appointments\n with Best Doctors in City \n Few Clicks Away.")
                    .setContentText("\n\nDevelopers:\nAvni Malhan\nKapish Malik\nKarishma Tirthani\nMeetika Anand\nNeeti Arora")
                    .setOnClickListener(DoctorGeneralInfoSignup.this)
                    .setStyle(R.style.AppTheme1)
                    .build();
            settings.edit().putBoolean("my_first_time", false).commit();
        }*/
    }


    /**
     * Background Async Task to Verify Doctor
     * */
    class CheckDoctorEmail extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DoctorGeneralInfoSignup.this);
            pDialog.setMessage("Verifiying Doctor..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Validating Doctor
         * */
        protected String doInBackground(String... args) {

            // Building Parameters

            HashMap<String,String> params= new HashMap<String,String>();
            params.put("email", doctorEmail);

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(new ServerURL().url_Validate_Doctor_Email, "POST", params);
            try {
                if(json != null) {
                    int success = json.getInt(TAG_SUCCESS);
                    isAlreadyRegistered = success;
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
                Context context = getApplicationContext();
                CharSequence text = "Doctor with this email id is already registered.";
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                emailEditText.setError("Doctor with this email id is already registered.");
            }
            else if(isAlreadyRegistered==0)
            {
                Intent intent = new Intent(DoctorGeneralInfoSignup.this, DoctorQualificationSignup.class);
                intent.putExtra("name", doctorName);
                intent.putExtra("email", doctorEmail);
                intent.putExtra("password", doctorPassword);
                intent.putExtra("gender", doctorGender);
                intent.putExtra("YrsOfExp", doctorYrsOfExp);
                intent.putExtra("age", doctorAge);
                intent.putExtra("fromActivity", "1");
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
