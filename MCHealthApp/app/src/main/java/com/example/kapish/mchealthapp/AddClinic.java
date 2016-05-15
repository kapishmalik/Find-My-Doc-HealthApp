package com.example.kapish.mchealthapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AddClinic extends AppCompatActivity {
    String docemail;
    String clinic_name_text = null;
    String clinic_address_text = null;
    String clinic_contact_text = null;
    String clinic_fees_text = null;
    private static final String TAG_SUCCESS = "success";
    private int check;
    JSONParser jsonParserAdd = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clinic);
        docemail = getIntent().getStringExtra("docemail");
        Button clear = (Button) findViewById(R.id.clearClinic);
        Button add = (Button) findViewById(R.id.addClinic);
        final EditText clinic_name = (EditText) findViewById(R.id.clinicname);
        final EditText clinic_address = (EditText) findViewById(R.id.clinicaddress);
        final EditText clinic_contact = (EditText) findViewById(R.id.contact);
        final EditText clinic_fees = (EditText) findViewById(R.id.fees);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clinic_name.setText("");
                clinic_address.setText("");
                clinic_contact.setText("");
                clinic_fees.setText("");
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clinic_name.getText() != null && clinic_address.getText() != null && clinic_contact.getText() != null && clinic_fees.getText() != null) {
                    clinic_name_text = clinic_name.getText().toString();
                    clinic_address_text = clinic_address.getText().toString();
                    clinic_contact_text = clinic_contact.getText().toString();
                    clinic_fees_text = clinic_fees.getText().toString();
                    new Add().execute();
                }
            }
        });

    }

    class Add extends AsyncTask<String, String, String> {



        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> params = new HashMap<String,String>();
            params.put("docemail", docemail);
            params.put("clinic_name", clinic_name_text);
            params.put("clinic_address", clinic_address_text);
            params.put("clinic_contact", clinic_contact_text);
            params.put("clinic_fees", clinic_fees_text);
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParserAdd.makeHttpRequest(new ServerURL().url_Add_Clinic, "POST",params);
            try {
                if(json != null) {
                    int success = json.getInt(TAG_SUCCESS);
                    check=success;
                    if(success == 1)
                    {
                        check = 1;
                    }
                    else if(success == 0)
                    {
                        check = 0;
                        //Toast.makeText(getActivity().getApplicationContext(),"Error..",Toast.LENGTH_SHORT).show();
                    }
                    else if(success == -2)
                    {
                        check = -2;
                        //Toast.makeText(getActivity().getApplicationContext(),"Server not working..",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    check=-3;
                }
            }
            catch (JSONException e) {
                check=-1;
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done

            if(check == -1)
                Toast.makeText(getApplicationContext(), "json exception", Toast.LENGTH_SHORT).show();
            else if(check == 0)
            {
                Toast.makeText(getApplicationContext(),"not added",Toast.LENGTH_SHORT).show();
            }
            else if(check == -2)
            {
                Toast.makeText(getApplicationContext(),"server not working",Toast.LENGTH_SHORT).show();
            }
            else if(check == -3)
            {
                Toast.makeText(getApplicationContext(),"json is null",Toast.LENGTH_SHORT).show();
            }
            else if(check == 1)
            {
                Toast.makeText(getApplicationContext(),"added",Toast.LENGTH_SHORT).show();
            }
        }

    }


}