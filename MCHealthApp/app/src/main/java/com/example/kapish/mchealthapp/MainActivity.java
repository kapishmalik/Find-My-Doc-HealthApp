package com.example.kapish.mchealthapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int counter;
    private ViewTarget t1;
    private ViewTarget t2;
    ShowcaseView showcaseView;

    SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button patientButton          = (Button)  findViewById(R.id.patientButton);
        Button doctorButton           = (Button)   findViewById(R.id.doctorButton);
        final RelativeLayout buttonLayout   = (RelativeLayout) findViewById(R.id.buttonLayout);
        final LinearLayout textLayout       = (LinearLayout) findViewById(R.id.textViewLayout);
        final ImageView mainImage           = (ImageView) findViewById(R.id.mainimage);
        final ImageView splashImage         = (ImageView) findViewById(R.id.splashscreen);
         counter = 0;
        t1 = new ViewTarget(R.id.doctorButton,this);
        t2 = new ViewTarget(R.id.patientButton,this);


        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            splashImage.setVisibility(View.GONE);
                            buttonLayout.setVisibility(View.VISIBLE);
                            textLayout.setVisibility(View.VISIBLE);
                            mainImage.setVisibility(View.VISIBLE);
                            try {
                                SharedPreferences settings = getApplicationContext().getSharedPreferences("Pref", MODE_PRIVATE);
                                if (settings.getBoolean("my_first_time", true)) {
                                    showcaseView = new ShowcaseView.Builder(MainActivity.this)
                                            .setTarget(com.github.amlcurran.showcaseview.targets.Target.NONE)
                                            .setContentTitle("Book Your Appointments\n with Best Doctors in City \n Few Clicks Away.")
                                            .setContentText("\n\nDevelopers:\nAvni Malhan\nKapish Malik\nKarishma Tirthani\nMeetika Anand\nNeeti Arora")
                                            .setOnClickListener(MainActivity.this)
                                            .setStyle(R.style.AppTheme1)
                                            .build();
                                    settings.edit().putBoolean("my_first_time", false).commit();
                                }
                            }
                            catch (Exception e) {
                               //do something
                            }


                        }
                    });

                }
            }
        };

        timerThread.start();


        doctorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DoctorDifferentSignup.class);
                startActivity(intent);
            }
        });
        patientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PatientDifferentSignup.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(counter)
        {
            case 0:
                showcaseView.setShowcase(t1,true);
                showcaseView.setContentTitle("Doctor Mode");
                showcaseView.setContentText("Sign In as Doctor.\nManage your Clinics,Time Slots and Dates.\nApprove or Reject Appointments.");
                break;
            case 1:
                showcaseView.setShowcase(t2,true);
                showcaseView.setContentTitle("Patient Mode");
                showcaseView.setContentText("Sign In as Patient.\nSearch and View Doctor.\n Manage Your Appointments.");
                break;
            case 2:
                showcaseView.hide();
                break;

        }
        counter++;
    }
}
