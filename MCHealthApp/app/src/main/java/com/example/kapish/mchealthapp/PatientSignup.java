package com.example.kapish.mchealthapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Calendar;
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


public class PatientSignup extends AppCompatActivity {

    private String patientEmailId;
    private String patientName;
    private String patientContactNo;
    private String patientDOB;
    private String patientPassword;
    private String patientAddress;
    private String patientGender;
    private int patientBirthYear;
    private int currentYear;
    private int patientAge;

    //fields for sending Message
    private String emailSubject;
    private String emailMessage;
    Session session = null;
    private int emailVerificationCode;
    // Progress Bar
    private ProgressDialog pDialog;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private Boolean isSaved;
    private Boolean isPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_signup);

        //Initialize UI elements
        final EditText emailEditText = (EditText) findViewById(R.id.email);
        final EditText nameEditText = (EditText) findViewById(R.id.name);
        final EditText contactEditText = (EditText) findViewById(R.id.contact);
        final EditText addressEditText = (EditText) findViewById(R.id.address);
        final EditText passwordEditText = (EditText) findViewById(R.id.password);
        final EditText dobEditText = (EditText) findViewById(R.id.dob);
        Button signUpButton = (Button) findViewById(R.id.sign_up_button);
        Button clearButton = (Button) findViewById(R.id.clear);
        RadioGroup genderRadioGroup = (RadioGroup) findViewById(R.id.radioSex);
        patientGender = ((RadioButton) findViewById(genderRadioGroup.getCheckedRadioButtonId())).getText().toString();

        final Calendar myCalendar = Calendar.getInstance();
        currentYear = myCalendar.get(Calendar.YEAR);
        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                patientGender = ((RadioButton) findViewById(checkedId)).getText().toString();
            }
        });
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub

                dobEditText.setText(dayOfMonth + " / " + (monthOfYear + 1) + " / "
                        + year);
                patientBirthYear = year;
                patientAge = currentYear - patientBirthYear;
                dobEditText.setError(null);

            }

        };
        dobEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                                 @Override
                                                 public void onFocusChange(View v, boolean hasFocus) {
                                                     if (hasFocus) {
                                                         new DatePickerDialog(PatientSignup.this, date, myCalendar
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
                dobEditText.setText(str);
                emailEditText.setText(str);
                nameEditText.setText(str);
                contactEditText.setText(str);
                addressEditText.setText(str);
                passwordEditText.setText(str);
                dobEditText.clearFocus();
                emailEditText.clearFocus();
                contactEditText.clearFocus();
                addressEditText.clearFocus();
                passwordEditText.clearFocus();
                nameEditText.clearFocus();
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((nameEditText.getText().toString().trim().length() == 0)) {
                    nameEditText.setError("Name can not be blank");
                } else if ((emailEditText.getText().toString().trim().length() == 0)) {
                    emailEditText.setError("Email Id can not be blank");
                } else if ((dobEditText.getText().toString().trim().length() == 0)) {
                    dobEditText.setError("Date of Birth can not be blank");
                } else if ((contactEditText.getText().toString().trim().length() == 0)) {
                    contactEditText.setError("Contact No can not be blank");
                } else if ((addressEditText.getText().toString().trim().length() == 0)) {
                    addressEditText.setError("Address can not be blank");
                } else if ((passwordEditText.getText().toString().trim().length() == 0)) {
                    passwordEditText.setError("Password can not be blank");
                } else {

                    patientEmailId = emailEditText.getText().toString().toLowerCase();
                    patientPassword = passwordEditText.getText().toString();
                    patientDOB = dobEditText.getText().toString();
                    patientContactNo = contactEditText.getText().toString();
                    patientAddress = addressEditText.getText().toString();
                    patientName = nameEditText.getText().toString();
                    String emailRegex = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                    Boolean emailFormat = patientEmailId.matches(emailRegex);
                    if (!emailFormat) {
                        emailEditText.setError("Email id is not in correct format");
                    } else if (patientPassword.length() <= 4) {
                        passwordEditText.setError("Password Length should be more than 4");
                    } else {
                        // do something

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

                }
            }
        });
    }

    class SendEmailVerificationCode extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PatientSignup.this);
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
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(patientEmailId));
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
            Toast toast =Toast.makeText(getApplicationContext(),"Please check your email for Email Verification Code",Toast.LENGTH_LONG);
            toast.show();
            Intent intent = new Intent(PatientSignup.this, PatientEmailVerification.class);
            intent.putExtra("name",patientName);
            intent.putExtra("email",patientEmailId);
            intent.putExtra("gender",patientGender);
            intent.putExtra("dob",patientDOB);
            intent.putExtra("contact",patientContactNo);
            intent.putExtra("age",patientAge);
            intent.putExtra("address",patientAddress);
            intent.putExtra("password",patientPassword);
            intent.putExtra("key",emailVerificationCode);
            startActivity(intent);
        }


    }
}