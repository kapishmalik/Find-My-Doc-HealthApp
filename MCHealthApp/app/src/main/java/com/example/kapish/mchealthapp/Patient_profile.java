package com.example.kapish.mchealthapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;


public class Patient_profile extends Fragment {

    Button save;
    EditText editTextPatName;
    EditText editTextPatEmail;
    EditText editTextPatAge;
    EditText editTextPatContactNo;
    EditText editTextPatAddress;
    RadioGroup radioGroupGender;
    ImageButton buttonChangePwd;
    ImageButton buttonLogout;
    String patientName;
    String patientEmail;
    String patientGender;
    String patientAge;
    String patientContactNo;
    String patientAddress;
    String patientPrevEmail;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Boolean isSaved;


    private int isUpdatedSuccessCode;
    // Progress Bar
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";
    public void check(String patientName,String patientAge,String patientGender,String patientEmail,String patientContactNo,String patientAddress)
    {
        this.patientName=patientName;
        this.patientEmail=patientEmail;
        this.patientGender=patientGender;
        this.patientAge=patientAge;
        this.patientContactNo=patientContactNo;
        this.patientAddress=patientAddress;
        this.patientPrevEmail=patientEmail;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View v=inflater.inflate(R.layout.fragment_patient_profile, container, false);

        // Shared Preferences
        pref = getActivity().getApplicationContext().getSharedPreferences("PatientPref", Context.MODE_PRIVATE);
        editor = pref.edit();
        isSaved = pref.getBoolean("isSaved", false);
        editTextPatName      = (EditText)v.findViewById(R.id.editTextNamePat);
        editTextPatName.setText(patientName);
        editTextPatAge       = (EditText)(v.findViewById(R.id.editTextAgePat));
        editTextPatAge.setText(patientAge);
        editTextPatEmail     =(EditText) (v.findViewById(R.id.editTextemailPat));
        editTextPatEmail.setText(patientEmail);
        editTextPatContactNo = (EditText)(v.findViewById(R.id.editTextContactNo));
        editTextPatContactNo.setText(patientContactNo);
        editTextPatAddress   = (EditText)(v.findViewById(R.id.editTextAddress));
        editTextPatAddress.setText(patientAddress);
         save = (Button) v.findViewById(R.id.savebutton);
        radioGroupGender     = (RadioGroup) v.findViewById(R.id.radioSex);
        isUpdatedSuccessCode = 0;
        buttonChangePwd = (ImageButton) v.findViewById(R.id.changepwd);
        buttonLogout    = (ImageButton) v.findViewById(R.id.logout);
        if(patientGender != null) {
            if (patientGender.equalsIgnoreCase("male")) {
                ((RadioButton) (v.findViewById(R.id.radioButtonMalePat))).setChecked(true);

            } else if (patientGender.equalsIgnoreCase("female")) {
                ((RadioButton) (v.findViewById(R.id.radioButtonFemalePat))).setChecked(true);
            } else {
                ((RadioButton) (v.findViewById(R.id.radioButtonOtherPat))).setChecked(true);
            }
        }

        radioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                patientGender = ((RadioButton) v.findViewById(checkedId)).getText().toString();
            }
        });

        buttonChangePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PatientChangePassword.class);
                intent.putExtra("email", patientEmail);
                intent.putExtra("table", "patient");
                startActivity(intent);
            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.remove("isSaved");
                editor.remove("isPatient");
                editor.remove("patientName");
                editor.remove("patientEmail");
                editor.remove("patientGender");
                editor.remove("patientAge");
                editor.remove("patientContactNo");
                editor.remove("patientAddress");
                editor.clear();
                editor.commit();
                isSaved = true;
                Toast.makeText(getActivity().getApplicationContext(), "You are successfully Logout.", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getActivity().getApplicationContext(),MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                getActivity().finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                patientName      = editTextPatName.getText().toString();
                patientAge       = editTextPatAge.getText().toString();
                patientAddress   = editTextPatAddress.getText().toString();
                patientContactNo = editTextPatContactNo.getText().toString();
                patientEmail     = editTextPatEmail.getText().toString();

                if(patientName.trim().length() == 0)
                {
                    editTextPatName.setError("Name can not be blank");
                }
                else if(patientEmail.trim().length() == 0)
                {
                    editTextPatEmail.setError("Email Id can not be blank");
                }
                else
                {
                    String emailRegex = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                    Boolean emailFormat = patientEmail.matches(emailRegex);
                    if (!emailFormat) {
                        editTextPatEmail.setError("Email id is not in correct format");
                    }
                    else
                    {
                        //do something
                        new SavePatientDetails().execute();
                        Log.i("INFO", patientName);
                        Log.i("INFO",patientAddress);
                        Log.i("INFO",patientAge);
                        Log.i("INFO",patientContactNo);
                        Log.i("INFO",patientEmail);
                        Log.i("INFO",patientPrevEmail);
                        Log.i("INFO",patientGender);
                    }
                }
            }
        });

        return v;
    }



/*    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.post(new Runnable() {
            @Override
            public void run() {
                // your showcase initialisation code here
                t1 = new ViewTarget(R.id.savebutton,getActivity());
                t2 = new ViewTarget(R.id.changepwd,getActivity());
                t3 = new ViewTarget(R.id.logout,getActivity());

                SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences("Pref",0);
                if (settings.getBoolean("my_first_time_patientprofile", true)) {
                    showcaseView = new ShowcaseView.Builder(getActivity())
                            .setTarget(com.github.amlcurran.showcaseview.targets.Target.NONE)
                            .setContentTitle("Patient Profile.")
                            .setContentText("Welcome "+patientName+"\nManage Your Profile Details.")
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    switch (counter) {
                                        case 0:
                                            showcaseView.setShowcase(t1, true);
                                            showcaseView.setContentTitle("Save");
                                            showcaseView.setContentText("Click here to save your Profile Information.");
                                            break;
                                        case 1:
                                            showcaseView.setShowcase(t2, true);
                                            showcaseView.setContentTitle("Change your Password.");
                                            showcaseView.setContentText("Click here to change your password.");
                                            break;
                                        case 2:
                                            showcaseView.setShowcase(t3, true);
                                            showcaseView.setContentTitle("Logout From App");
                                            showcaseView.setContentText("Click here to Logout.");
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
                    settings.edit().putBoolean("my_first_time_patientprofile", false).commit();
                }


            }
        });
    }*/
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void onPause() {
        super.onPause();

        if(!isSaved) {
            editor.putBoolean("isSaved", true);
            editor.putBoolean("isPatient", true);
            editor.putString("patientName", patientName);
            editor.putString("patientEmail", patientEmail);
            editor.putString("patientGender", patientGender);
            editor.putString("patientAge", patientAge);
            editor.putString("patientContactNo", patientContactNo);
            editor.putString("patientAddress", patientAddress);
            editor.commit();
        }


    }

    @Override
    public void onStop() {
        super.onStop();

    }

    /**
     * Background Async Task to Validate User from Server
     * */
    class SavePatientDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Saving Your Details..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Validating User
         * */
        protected String doInBackground(String... args) {

            // Building Parameters

            HashMap<String,String> params= new HashMap<String,String>();
            params.put("name", patientName);
            params.put("email", patientEmail.toLowerCase());
            params.put("prevemail", patientPrevEmail);
            params.put("age", patientAge);
            params.put("contact", patientContactNo);
            params.put("address", patientAddress);
            params.put("gender", patientGender);


            JSONObject json = jsonParser.makeHttpRequest(new ServerURL().url_patient_save_details, "POST", params);
            try {
                if(json != null) {
                    Log.i("INFO",json.toString());
                    int success = json.getInt("success");
                    isUpdatedSuccessCode = success;
                   Log.i("INFO","Code is "+isUpdatedSuccessCode);
                }

            }
            catch (JSONException e) {
                isUpdatedSuccessCode = -1;
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
            if(isUpdatedSuccessCode == 1)
            {
                Context context = getActivity().getApplicationContext();
                CharSequence text = "Your Details saved successfully.";
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
            else
            {
                Context context = getActivity().getApplicationContext();
                CharSequence text = "Server not working. Please try again later";
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }

        }

    }

}
