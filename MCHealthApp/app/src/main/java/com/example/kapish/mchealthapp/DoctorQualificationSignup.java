package com.example.kapish.mchealthapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;


public class DoctorQualificationSignup extends AppCompatActivity {

    private String[] speciality = {"Dermatologist", "Dentist", "General Physician", "Gynaecologist", "Homeopath", "Neurologist","Cardiology",
            "Pediatrics","ENT","Gastroenterology","Orthopedics","Neurosurgery","Radiology"};
    private String[] ugDegree = {"M.B.B.S","B.D.S","B.A.M.S","B.H.M.S","B.U.M.S","B.P.T","B.Sc BioLogy"};
    private String[] pgDegree = {"M.D","M.S","M.D.S","MHSc ","MSc CND","MHA","M.B.A" ,"D.P.T"};
    private String[] otherDegree={"D.M","Mch","Fellowship Medicine"};
    private AutoCompleteTextView specialityTextView;
    private AutoCompleteTextView ugDegreeTextView;
    private AutoCompleteTextView pgDegreeTextView;
    private AutoCompleteTextView otherDegreeTextView;
    private EditText             MCIEditText;
    private Button               clearButton;
    private Button               nextButton;
    private String               doctorUGDegree;
    private String               doctorPGDegree;
    private String               doctorOtherDegree;
    private String               doctorSpeciality;
    private String               doctorMCINo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_qualification_signup);

        //Initialize UI Elements
        specialityTextView    = (AutoCompleteTextView) findViewById(R.id.speciality);
        ugDegreeTextView      = (AutoCompleteTextView) findViewById(R.id.ugdegree);
        pgDegreeTextView      = (AutoCompleteTextView) findViewById(R.id.pgdegree);
        otherDegreeTextView   = (AutoCompleteTextView) findViewById(R.id.otherdegree);
        MCIEditText           = (EditText)             findViewById(R.id.medicalCouncilNo);
        clearButton           = (Button)               findViewById(R.id.clear);
        nextButton            = (Button)               findViewById(R.id.save_and_next);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, speciality);
        specialityTextView.setAdapter(adapter1);
        specialityTextView.setThreshold(1);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, ugDegree);
        ugDegreeTextView.setThreshold(1);
        ugDegreeTextView.setAdapter(adapter2);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, pgDegree);
        pgDegreeTextView.setThreshold(1);
        pgDegreeTextView.setAdapter(adapter3);
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, otherDegree);
        otherDegreeTextView.setThreshold(1);
        otherDegreeTextView.setAdapter(adapter4);


        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence str = "";
                ugDegreeTextView.setText(str);
                pgDegreeTextView.setText(str);
                otherDegreeTextView.setText(str);
                specialityTextView.setText(str);
                MCIEditText.setText(str);
                ugDegreeTextView.clearFocus();
                pgDegreeTextView.clearFocus();
                otherDegreeTextView.clearFocus();
                specialityTextView.clearFocus();
                MCIEditText.clearFocus();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((ugDegreeTextView.getText().toString().trim().length() == 0)) {
                    ugDegreeTextView.setError("UG Degree can not be blank");
                }
                else if ((pgDegreeTextView.getText().toString().trim().length() == 0)) {
                    pgDegreeTextView.setError("PG Degree can not be blank");
                }
                else if ((specialityTextView.getText().toString().trim().length() == 0)) {
                    specialityTextView.setError("Speciality can not be blank");
                }
                else if ((MCIEditText.getText().toString().trim().length() == 0)) {
                    MCIEditText.setError("Medical Council No can not be blank");
                }
                else
                {
                    //do something
                    doctorUGDegree   = ugDegreeTextView.getText().toString();
                    doctorPGDegree   = pgDegreeTextView.getText().toString();
                    doctorOtherDegree= otherDegreeTextView.getText().toString();
                    doctorSpeciality = specialityTextView.getText().toString();
                    doctorMCINo      = MCIEditText.getText().toString();
                    Intent intent = new Intent(DoctorQualificationSignup.this, DoctorClinicDetailsSignup.class);
                    intent.putExtra("name",getIntent().getExtras().getString("name"));
                    intent.putExtra("email",getIntent().getExtras().getString("email"));
                    intent.putExtra("password",getIntent().getExtras().getString("password"));
                    intent.putExtra("gender",getIntent().getExtras().getString("gender"));
                    intent.putExtra("YrsOfExp",getIntent().getExtras().getString("YrsOfExp"));
                    intent.putExtra("age",getIntent().getExtras().getString("age"));
                    intent.putExtra("fromActivity",getIntent().getExtras().getString("fromActivity"));
                    intent.putExtra("ugDegree",doctorUGDegree);
                    intent.putExtra("pgDegree",doctorPGDegree);
                    intent.putExtra("otherDegree",doctorOtherDegree);
                    intent.putExtra("speciality",doctorSpeciality);
                    intent.putExtra("MCINo",doctorMCINo);
                    Log.i("INFO",doctorUGDegree+" "+doctorPGDegree+" "+doctorOtherDegree+" "+doctorSpeciality+" "+doctorMCINo);
                    startActivity(intent);
                }
            }
        });
    }


}
