package com.example.kapish.mchealthapp;


import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.support.v4.view.ViewPager;


import com.astuetz.PagerSlidingTabStrip;

public class TabsPatient extends FragmentActivity {


    MyPagerAdapter mPagerAdapter;

    ViewPager mViewPager=null;
    String patientName;
    String patientEmail;
    String patientGender;
    String patientAge;
    String patientContactNo;
    String patientAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs_doc);
        patientName=getIntent().getExtras().getString("name");
        patientAge=getIntent().getExtras().getString("age");
        patientGender=getIntent().getExtras().getString("gender");
        patientEmail=getIntent().getExtras().getString("email");
        patientContactNo=getIntent().getExtras().getString("contact");
        Log.i("INFO","Email Id is "+patientEmail);
        patientAddress=getIntent().getExtras().getString("address");


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mPagerAdapter);
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(mViewPager);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tabs_doc, menu);
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


    public class MyPagerAdapter extends FragmentStatePagerAdapter {

        public MyPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            Fragment fragment=null;
            if(position==0)
            {
                Patient_profile d1=new Patient_profile();
                d1.check(patientName, patientAge, patientGender, patientEmail, patientContactNo, patientAddress);
                fragment=d1;
            }
            else if(position==1)
            {
                FindDoctor d2= new FindDoctor();
                fragment=d2;
            }
            else if(position==2)
            {
                BookAppointment d1=new BookAppointment();
                d1.check(patientName,patientEmail);
                fragment=d1;
            }
            else if(position==3)
            {
                ViewAppointmentStatus d3 = new ViewAppointmentStatus();
                d3.check(patientName,patientEmail);
                fragment=d3;
            }


            return fragment;
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String s="";
            if(position==0)
            {
                s=" Profile ";
            }
            if(position==1)
            {
                s=" Find Doctor ";
            }
            if(position==2)
            {
                s=" Book Appointment ";
            }
            if(position==3)
            {
                s=" View Appointments ";
            }

            return s;
        }
    }



}
