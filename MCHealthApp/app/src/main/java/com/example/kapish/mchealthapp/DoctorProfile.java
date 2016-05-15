package com.example.kapish.mchealthapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kapish.mchealthapp.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class DoctorProfile extends AppCompatActivity {
    static String docname, speciality, docemail;
    TextView txtDoctor, txtSpeciality, txtYrsofexp, txtclinic, txtemail, txtmob_no, txteducation;
    Button btnBook, btnView;
    JSONParser jsonParser = new JSONParser();

    SharedPreferences pref;

    private ArrayList<String> doctornames;
    private static HashMap<String, String> mapDoctorDetails;
    private int isFetched;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_DoctorName = "name";
    private static final String TAG_DoctorSpeciality = "speciality";
    private static final String TAG_DoctorAddress = "address";
    private static final String TAG_DoctorContactno = "contactno";
    private static final String TAG_DoctorUgdegree = "ugdegree";
    private static final String TAG_DoctorPgdegree = "pgdegree";
    private static final String TAG_DoctorOtherdegree = "otherdegree";
    private static final String TAG_DoctorYrsofexp = "yrsofexp";
    static String address, contactno, ugdegree, pgdegree, otherdegree;
    static int yrsofexp;
    String patientName, patientEmail;
    // Progress Bar
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);
       // new DoctorDetails().execute();

        pref = getApplicationContext().getSharedPreferences("PatientPref", Context.MODE_PRIVATE);

        docemail = getIntent().getStringExtra("Doctorid");
        txtemail = (TextView) findViewById(R.id.txtemail);
        txtemail.setText(docemail);
        btnBook = (Button) findViewById(R.id.button_book);
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DoctorProfile.this, ChooseAppointmentSlot.class);
                Log.i("INFO", docname + "  " + docemail);
                i.putExtra("DoctorName", docname);
                i.putExtra("DoctorEmail", docemail);
                i.putExtra("PatientName", pref.getString("patientName", "NULL"));
                i.putExtra("PatientEmail", pref.getString("patientEmail", "NULL"));
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        new DoctorDetails().execute();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /* public void onBtnView(View v){
         Intent i = new Intent(DoctorProfile.this, PatientAppointmentStatus.class);
         startActivity(i);
     }*/
    class DoctorDetails extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("email", docemail);
            Log.i("docemail", docemail);

            JSONObject json = jsonParser.makeHttpRequest(new ServerURL().url_get_doctor_profile, "POST", params);
            try {
                if (json != null) {
                    Log.i("INFO", json.toString());
                    isFetched = json.getInt(TAG_SUCCESS);
                    if (isFetched == 1) {
                        docname = json.getString(TAG_DoctorName);
                        speciality = json.getString(TAG_DoctorSpeciality);
                        address = json.getString(TAG_DoctorAddress);
                        contactno = json.getString(TAG_DoctorContactno);
                        ugdegree = json.getString(TAG_DoctorUgdegree);
                        pgdegree = json.getString(TAG_DoctorPgdegree);
                        otherdegree = json.getString(TAG_DoctorOtherdegree);
                        yrsofexp = json.getInt(TAG_DoctorYrsofexp);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
                isFetched = -1;
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
//           pDialog.dismiss();
            if(isFetched==1) {
                txtDoctor = (TextView) findViewById(R.id.txtdoctorName);
                txtDoctor.setText("Dr " + docname);
                txtSpeciality = (TextView) findViewById(R.id.txtdoctorSpeciality);
                txtSpeciality.setText(" " + speciality);
                txtYrsofexp = (TextView) findViewById(R.id.txtdoctoryrsofexp);
                txtYrsofexp.setText(" " + yrsofexp + "yrs of experience");
                txtclinic = (TextView) findViewById(R.id.txtclinic);
                txtclinic.setText(address);
                txtmob_no = (TextView) findViewById(R.id.txtmobno);
                txtmob_no.setText(contactno);
                txteducation = (TextView) findViewById(R.id.txtedu);
                if (otherdegree != null) {
                    if (!otherdegree.equalsIgnoreCase("na"))
                        txteducation.setText(ugdegree + "," + pgdegree + "," + otherdegree);
                    else
                        txteducation.setText(ugdegree + "," + pgdegree);
                }
            }
            else if(isFetched == -1){
                Toast.makeText(DoctorProfile.this, "Server not working.Please try after sometime!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

