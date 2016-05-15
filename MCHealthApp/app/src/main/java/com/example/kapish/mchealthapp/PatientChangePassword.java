package com.example.kapish.mchealthapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class PatientChangePassword extends AppCompatActivity {

    private String   userEmail;
    private String   tableName;
    private String   userPwd;
    private EditText editTextUserPwd;
    private EditText editTextUserConfirmPwd;
    private Button   buttonChange;

    // Progress Bar
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";
    private int isUpdated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_change_password);
        userEmail               = getIntent().getExtras().getString("email");
        tableName               = getIntent().getExtras().getString("table");
        editTextUserPwd         = (EditText) findViewById(R.id.pwd);
        editTextUserConfirmPwd  = (EditText) findViewById(R.id.confirmpwd);
        buttonChange            = (Button)   findViewById(R.id.changepwd);

        buttonChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = editTextUserPwd.getText().toString();
                String confirmPwd = editTextUserConfirmPwd.getText().toString();
                if (pwd.trim().length() == 0) {
                    editTextUserPwd.setError("Password can not be blank");
                } else if (confirmPwd.trim().length() == 0) {
                    editTextUserPwd.setError("Password can not be blank");
                } else {
                    if (pwd.equals(confirmPwd)) {
                        userPwd = pwd;
                        new ChangePassword().execute();
                    } else {
                        editTextUserConfirmPwd.setError("Re-Type Password Correctly");
                    }
                }
            }
        });



    }

    /**
     * Background Async Task to Change Password
     * */
    class ChangePassword extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PatientChangePassword.this);
            pDialog.setMessage("Changing Password...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Changing Doctor Name
         * */
        protected String doInBackground(String... args) {

            // Building Parameters

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("email",userEmail);
            params.put("table",tableName);
            params.put("pwd",userPwd);
            JSONObject json = jsonParser.makeHttpRequest(new ServerURL().url_change_pwd, "POST", params);
            try {
                if (json != null) {
                    isUpdated = json.getInt(TAG_SUCCESS);


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
            // dismiss the dialog once done
            pDialog.dismiss();
            if(isUpdated == 1)
            {
                Toast.makeText(getApplicationContext(),"Password changed successfully.",Toast.LENGTH_LONG).show();
            }
            else if(isUpdated == 0)
            {
                Toast.makeText(getApplicationContext(),"Email Id is not correct.",Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Server not working. Please try again later.",Toast.LENGTH_LONG).show();
            }


        }

    }


}
