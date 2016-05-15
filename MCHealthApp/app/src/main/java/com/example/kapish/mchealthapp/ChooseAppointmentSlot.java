package com.example.kapish.mchealthapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChooseAppointmentSlot extends AppCompatActivity {

    private EditText editTextpatientName;
    private EditText editTextpatientPurpose;
    private AutoCompleteTextView completeTextViewClinicNames;
    private TextView textViewDoctorName;
    private String patientName;
    private String patientEmail;
    private String doctorName;
    private String doctorEmail;
    private String clinicID;

    private ArrayList<String> clinicNames;
    private HashMap<String, String> mapClinicDetails;
    private int isFetched;

    JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS      = "success";
    private static final String TAG_CLINIC       = "clinic";
    private static final String TAG_CLINICID     = "clinicid";
    private static final String TAG_CLINICNAME   = "clinicname";

    JSONArray clinics = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_appointment_slot);
        doctorName         = getIntent().getExtras().getString("DoctorName");
        doctorEmail        = getIntent().getExtras().getString("DoctorEmail");
        patientName        = getIntent().getExtras().getString("PatientName");
        patientEmail       = getIntent().getExtras().getString("PatientEmail");
        textViewDoctorName = (TextView) findViewById(R.id.doctorName);
        textViewDoctorName.setText("Dr. " + doctorName);
        Log.i("INFO Choose", "Patient Name" + patientName);
        Log.i("INFO Choose", "Patient Email" + patientEmail);
        Log.i("INFO Choose", "Doctor Name" + doctorName);
        Log.i("INFO Choose", "Doctor Email" + doctorEmail);
        clinicNames        = new ArrayList<String>();
        mapClinicDetails   = new HashMap<String,String>();
        editTextpatientName          = (EditText) findViewById(R.id.patientName);
        editTextpatientName.setText(patientName);
        editTextpatientPurpose       = (EditText) findViewById(R.id.purpose);
        completeTextViewClinicNames  = (AutoCompleteTextView) findViewById(R.id.clinicNames);
        new FetchClinics().execute();

    }

    @Override
    protected void onResume() {
        super.onResume();
        clinicID = null;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choose_appointment_slot, menu);
        return true;

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickChooseSlot(View v)
    {
        Intent i = new Intent(this, ListViewActivity.class);

        String patientName = editTextpatientName.getText().toString();
        String purpose     = editTextpatientPurpose.getText().toString();
        String clinicName  = completeTextViewClinicNames.getText().toString();
       // Log.i("INFO",clinicName);
        if(patientName.equals("")||purpose.equals("")||clinicName.equals(""))
        {
            if (patientName.equals("")) {
                //Toast.makeText(getApplicationContext(),
                //      "Please enter patient name. Name field cannot be empty", Toast.LENGTH_SHORT).show();
                editTextpatientName.setError("Name field cannot be empty");
            }
            if (purpose.equals("")) {
                //Toast.makeText(getApplicationContext(),
                //      "Please enter purpose. Purpose field cannot be empty", Toast.LENGTH_SHORT).show();
                editTextpatientPurpose.setError("Purpose field cannot be empty");
            }
            if (clinicName.equals("")) {
                //Toast.makeText(getApplicationContext(),
                //      "Please enter clinic name. Clinic Name field cannot be empty", Toast.LENGTH_SHORT).show();
                completeTextViewClinicNames.setError("Clinic Name field cannot be empty");
            }
        }
        else
        {

            Iterator<Map.Entry<String,String>> entries = mapClinicDetails.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, String> entry = entries.next();
                if(entry.getValue().equals(clinicName))
                {
                    clinicID = entry.getKey();
                    break;
                }

            }
            if(clinicID == null)
            {
                completeTextViewClinicNames.setError("Please enter correct Clinic Name");
            }
            else
            {
                Log.i("INFO",clinicID);
                i.putExtra("appointmentID","0");
                i.putExtra("doctorEmail",doctorEmail);
                i.putExtra("isUpdate","0");
                i.putExtra("patientEmail",patientEmail);
                i.putExtra("clinicID",clinicID);
                i.putExtra("clinicName",clinicName);
                i.putExtra("purpose",purpose);
                startActivity(i);
            }
        }
    }
    /**
     * Background Async Task to fetch all Clinic's Name from Server
     * */
    class FetchClinics extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        /**
         * Fetching Clinic Name and Id
         * */
        protected String doInBackground(String... args) {

            // Building Parameters

            HashMap<String,String>params= new HashMap<String,String>();
            params.put("email", doctorEmail);
            Log.i("INFO","Length of param is "+params.size());
            JSONObject json = jsonParser.makeHttpRequest(new ServerURL().url_fetch_clinics, "POST", params);
            try {
                if(json != null)
                {
                    Log.i("INFO", json.toString());
                    isFetched = json.getInt(TAG_SUCCESS);
                    if(isFetched == 1)
                    {
                        clinics = json.getJSONArray(TAG_CLINIC);
                        for (int i = 0; i < clinics.length(); i++) {
                            JSONObject c = clinics.getJSONObject(i);
                            String clinicId   = c.getString(TAG_CLINICID);
                            String clinicName = c.getString(TAG_CLINICNAME);
                            Log.i("Clinic Id is ",clinicId);
                            Log.i("Clinic Name is", clinicName);
                            clinicNames.add(clinicName);
                            mapClinicDetails.put(clinicId,clinicName);

                        }
                    }

                }

            }
            catch (JSONException e) {
                isFetched = -1;
                e.printStackTrace();
            }

            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done

            Log.i("INFO Choose","Is fetched value is "+isFetched);
            if(isFetched == 1)
            {

                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>
                        (ChooseAppointmentSlot.this, android.R.layout.select_dialog_item,clinicNames);
                completeTextViewClinicNames.setAdapter(adapter1);
                completeTextViewClinicNames.setThreshold(1);
            }
            else if(isFetched == 0)
            {
                Toast.makeText(getApplicationContext(),"No Clinics corresponding to Doctor"+doctorName,Toast.LENGTH_LONG).show();
            }
           else
            {
                Toast.makeText(getApplicationContext(),"Server not working. Please try again later.",Toast.LENGTH_LONG).show();
            }

        }

    }



}
