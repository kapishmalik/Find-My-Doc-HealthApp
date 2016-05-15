package com.example.kapish.mchealthapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

public class Doctor_profile extends Fragment {

    String doctorName;
    String doctorEmail;
    String doctorGender;
    String doctorAge;
    String doctorQualification;
    String doctorSpeciality;
    String doctorMCINo;
    String doctorYrsOfExp;
    ViewPager viewPager;


    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Boolean isSaved;

    public void check(ViewPager vp,String doctorName,String doctorAge,String doctorGender,String doctorEmail,String doctorQualification,String doctorSpeciality,String doctorMCINo,String doctorYrsOfExp)
    {
        this.viewPager = vp;
        this.doctorName=doctorName;
        this.doctorAge=doctorAge;
        this.doctorEmail=doctorEmail;
        this.doctorGender=doctorGender;
        this.doctorQualification=doctorQualification;
        this.doctorSpeciality=doctorSpeciality;
        this.doctorMCINo=doctorMCINo;
        this.doctorYrsOfExp=doctorYrsOfExp;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.doctor_profile,container,false);
        /**save=(Button) v.findViewById(R.id.button3);
        save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction t = getFragmentManager().beginTransaction();
                Fragment fd=new FindDoctor();
                t.replace(R.id.container, fd);
                t.addToBackStack(null);
                t.commit();
            }
        });**/


        pref = getActivity().getApplicationContext().getSharedPreferences("DoctorPref", Context.MODE_PRIVATE);
        editor = pref.edit();
        isSaved = pref.getBoolean("isSaved", false);

        ((EditText)(v.findViewById(R.id.editTextName))).setText(doctorName);
        ((EditText)(v.findViewById(R.id.editTextAge))).setText(doctorAge);
        ((EditText)(v.findViewById(R.id.editTextemail))).setText(doctorEmail);
        ((EditText)(v.findViewById(R.id.editTextCouncil))).setText(doctorMCINo);
        ((EditText)(v.findViewById(R.id.editTextqualification))).setText(doctorQualification);
        ((EditText)(v.findViewById(R.id.editTextspeciality))).setText(doctorSpeciality);
        ((EditText)(v.findViewById(R.id.editTextexperience))).setText(doctorYrsOfExp);
        ImageButton buttonLogout    = (ImageButton) v.findViewById(R.id.logout);
        if(doctorGender!=null) {
            if (doctorGender.equalsIgnoreCase("male")) {
                ((RadioButton) (v.findViewById(R.id.radioButtonMale))).setChecked(true);
            } else if (doctorGender.equalsIgnoreCase("female")) {
                ((RadioButton) (v.findViewById(R.id.radioButtonFemale))).setChecked(true);
            } else {
                ((RadioButton) (v.findViewById(R.id.radioButtonOther))).setChecked(true);
            }
        }

        ImageButton manageDates = (ImageButton)v.findViewById(R.id.imageButtonManageDates);
        manageDates.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(3, true);
            }
        });
        ImageButton manageClinics = (ImageButton)v.findViewById(R.id.imageButtonManageClinics);
        manageClinics.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(2,true);
            }
        });
        ImageButton manageSlots = (ImageButton)v.findViewById(R.id.imageButtonManageSlots);
        manageSlots.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(4,true);
            }
        });
        ImageButton changePassword = (ImageButton) v.findViewById(R.id.fwdPwd);
        changePassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), PatientChangePassword.class);
                intent.putExtra("email", doctorEmail);
                intent.putExtra("table", "doctor");
                startActivity(intent);
            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.remove("isSaved");
                editor.remove("isDoctor");
                editor.remove("docName");
                editor.remove("docEmail");
                editor.remove("docGender");
                editor.remove("docAge");
                editor.remove("docSpeciality");
                editor.remove("docYrsOfExp");
                editor.remove("docQualification");
                editor.remove("docMCINo");
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


        return v;
    }

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
            editor.putBoolean("isDoctor", true);
            editor.putString("docName", doctorName);
            editor.putString("docEmail", doctorEmail);
            editor.putString("docGender",doctorGender);
            editor.putString("docAge", doctorAge);
            editor.putString("docSpeciality",doctorSpeciality);
            editor.putString("docYrsOfExp",doctorYrsOfExp);
            editor.putString("docQualification",doctorQualification);
            editor.putString("docMCINo",doctorMCINo);
            editor.commit();

        }
    }

 /*   @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.post(new Runnable() {
            @Override
            public void run() {
                // your showcase initialisation code here
                t1 = new ViewTarget(R.id.imageButtonManageDates,getActivity());
                t2 = new ViewTarget(R.id.imageButtonManageClinics,getActivity());
                t3 = new ViewTarget(R.id.imageButtonManageSlots,getActivity());
                t4 = new ViewTarget(R.id.fwdPwd,getActivity());
                t5 = new ViewTarget(R.id.logout,getActivity());

                SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences("Pref", 0);
                if (settings.getBoolean("my_first_time_doctorprofile", true)) {
                    showcaseView = new ShowcaseView.Builder(getActivity())
                            .setTarget(com.github.amlcurran.showcaseview.targets.Target.NONE)
                            .setContentTitle("Doctor Profile.")
                            .setContentText("Welcome " + doctorName + "\nManage Your Profile Details.")
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    switch (counter) {
                                        case 0:
                                            showcaseView.setShowcase(t1, true);
                                            showcaseView.setContentTitle("Add New Dates");
                                            showcaseView.setContentText("Click here to add new Dates for your Appointments.");
                                            break;
                                        case 1:
                                            showcaseView.setShowcase(t2, true);
                                            showcaseView.setContentTitle("Add New Clinics");
                                            showcaseView.setContentText("Click here to add your new clinic.");
                                            break;
                                        case 2:
                                            showcaseView.setShowcase(t3, true);
                                            showcaseView.setContentTitle("Add New Time Slots");
                                            showcaseView.setContentText("Click here to add new time slot for particular clinic.");
                                            break;
                                        case 3:
                                            showcaseView.setShowcase(t4, true);
                                            showcaseView.setContentTitle("Change Password");
                                            showcaseView.setContentText("Click here to change your Password.");
                                            break;
                                        case 4:
                                            showcaseView.setShowcase(t5, true);
                                            showcaseView.setContentTitle("Logout From App");
                                            showcaseView.setContentText("Click here to Logout.");
                                            break;
                                        case 5:
                                            showcaseView.hide();
                                            break;

                                    }
                                    counter++;
                                }
                            })
                            .setStyle(R.style.AppTheme1)
                            .build();
                    settings.edit().putBoolean("my_first_time_doctorprofile", false).commit();
                }


            }
        });
    }
*/
    @Override
    public void onStop() {
        super.onStop();

    }



}






