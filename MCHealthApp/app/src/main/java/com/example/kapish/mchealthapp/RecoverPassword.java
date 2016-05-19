package com.example.kapish.mchealthapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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

public class RecoverPassword extends AppCompatActivity {

    private String emailId;
    private String userType;
    private int isValidUser;
    private int verificationCode;
    //fields for sending Message
    private String emailSubject;
    private String emailMessage;

    private LinearLayout layoutCode;
    private LinearLayout layoutEmail;
    Session session = null;


    // Progress Bar
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);
        //Initialize
        userType=getIntent().getExtras().getString("userType");
        isValidUser=-1;
        //Initialize UI elements
        final EditText emailEditText    = (EditText) findViewById(R.id.email);
                Button recoverPwdButton = (Button)   findViewById(R.id.recoverPwd);

        final EditText editTextVerificationCode = (EditText) findViewById(R.id.verificationcode);
        Button   buttonChangePwd          = (Button)   findViewById(R.id.changepwd);
        layoutCode                        = (LinearLayout) findViewById(R.id.layoutCode);
        layoutEmail                       = (LinearLayout) findViewById(R.id.layoutEmail);
        buttonChangePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextVerificationCode.getText().toString().trim().length() == 0)
                {
                    editTextVerificationCode.setError("Verification Code can not be blank.");
                }
                else {
                    int code = Integer.parseInt(editTextVerificationCode.getText().toString());
                    if(code == verificationCode)
                    {
                        Intent intent = new Intent(RecoverPassword.this, PatientChangePassword.class);
                        intent.putExtra("email", emailId);
                        intent.putExtra("table", userType);
                        startActivity(intent);
                    }
                    else
                    {
                        editTextVerificationCode.setError("Enter correct verification code");
                    }
                }
            }});
        recoverPwdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((emailEditText.getText().toString().trim().length() == 0)) {
                    emailEditText.setError("Email Id can not be blank");
                } else {
                    emailId = emailEditText.getText().toString();
                    String emailRegex = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                    Boolean emailFormat = emailId.matches(emailRegex);
                    if (!emailFormat) {
                        emailEditText.setError("Email id is not in correct format");
                    } else {
                        new FetchPwd().execute();

                        emailSubject="Email from EAppointment App Password Recovery";
                        Properties props = new Properties();
                        props.put("mail.smtp.host", "smtp.gmail.com");
                        props.put("mail.smtp.socketFactory.port", "465");
                        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                        props.put("mail.smtp.auth", "true");
                        props.put("mail.smtp.port", "465");

                        session = Session.getDefaultInstance(props, new Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication("doctorandpatientappointment@gmail.com", "XXXXXX");
                            }
                        });
                    }

                }
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        layoutEmail.setVisibility(View.VISIBLE);
        layoutCode.setVisibility(View.GONE);
    }
    /**
     * Background Async Task to Fetch Password from Server
     * */
    class FetchPwd extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RecoverPassword.this);
            pDialog.setMessage("Validating User..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Validating User and Fetching Password
         * */
        protected String doInBackground(String... args) {

            // Building Parameters
            HashMap<String,String> params= new HashMap<String,String>();
            params.put("email", emailId);
            params.put("table",userType);
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(new ServerURL().url_Recover_Pwd, "POST", params);
            try {
                if(json != null) {
                    int success = json.getInt(TAG_SUCCESS);
                    isValidUser = success;
                    Log.i("INFO","Started checking");
                    if(isValidUser==1)
                    {
                        if(json.getString(TAG_PASSWORD).equals("null"))
                        {
                            Log.i("INFO",json.getString(TAG_PASSWORD));
                            emailMessage="You Signed In using Google Plus. Please Login through same. ";
                        }
                        else
                        {
                            Random random  = new Random();
                            verificationCode= random.nextInt(9000)+1000;
                            emailMessage="Your one time password is"+verificationCode;
                        }
                    }
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
                //do something

                if(emailMessage.equals("You Signed In using Google Plus. Please Login through same. ")) {
                    Context context = getApplicationContext();
                    CharSequence text = "You Signed In using Google Plus. Please Login through same.";
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else
                {
                    new SendPassword().execute();

                }
            }
            else if(isValidUser==0)
            {
                Context context = getApplicationContext();
                CharSequence text = "This Email Id is not present in our Database.";
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

    class SendPassword extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RecoverPassword.this);
            pDialog.setMessage("Sending Email..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {

            try{
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("doctorandpatientappointment@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailId));
                message.setSubject(emailSubject);
                message.setContent(emailMessage, "text/html; charset=utf-8");
                Transport.send(message);
            } catch(MessagingException e) {
                e.printStackTrace();
            } catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            pDialog.dismiss();
            layoutCode.setVisibility(View.VISIBLE);
            layoutEmail.setVisibility(View.GONE);
            Context context = getApplicationContext();
            CharSequence text = "Please check your email for verification code.";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

}
