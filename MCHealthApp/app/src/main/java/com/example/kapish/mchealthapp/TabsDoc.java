package com.example.kapish.mchealthapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;

public class TabsDoc extends FragmentActivity {


    MyPagerAdapter mPagerAdapter;

    ViewPager mViewPager=null;
    String doctorName;
    String doctorEmail;
    String doctorGender;
    String doctorAge;
    String doctorQualification;
    String doctorSpeciality;
    String doctorMCINo;
    String doctorYrsOfExp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs_doc);
        doctorName=getIntent().getExtras().getString("name");
        doctorAge=getIntent().getExtras().getString("age");
        doctorEmail=getIntent().getExtras().getString("email");
        doctorGender=getIntent().getExtras().getString("gender");
        doctorEmail=getIntent().getExtras().getString("email");
        doctorYrsOfExp=getIntent().getExtras().getString("YrsOfExp");
        doctorMCINo=getIntent().getExtras().getString("MCINo");
        doctorQualification=getIntent().getExtras().getString("qualification");
        doctorSpeciality=getIntent().getExtras().getString("speciality");

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(),mViewPager);
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
        ViewPager viewPager;
        public MyPagerAdapter(FragmentManager fm,ViewPager vp)
        {
            super(fm);
            this.viewPager = vp;
        }


        @Override
        public Fragment getItem(int position) {
            Fragment fragment=null;
            if(position==0)
            {
                Doctor_profile d1=new Doctor_profile();
                d1.check(viewPager,doctorName, doctorAge, doctorGender, doctorEmail, doctorQualification, doctorSpeciality, doctorMCINo, doctorYrsOfExp);
                fragment=d1;
            }
            else if(position==1)
            {
                ApprovalList a=new ApprovalList();
                a.check(doctorEmail,doctorName);
                fragment=a;
            }
            else if(position==2)
            {
                ManageClinics a= new ManageClinics();
                a.check(doctorEmail);
                fragment=a;    //manage clinics
            }
            else if(position==3)
            {
                ManageAppointmentDates a=new ManageAppointmentDates();
                a.check(doctorEmail);
                fragment=a;
            }
            else if(position==4)
            {
                ChooseSlot a=new ChooseSlot();
                a.check(doctorEmail);
                fragment=a;
            }
            else if(position==5)
            {
                TodaysAppointmentList a=new TodaysAppointmentList();
                a.check(doctorEmail);
                fragment=a;
            }


            return fragment;
        }

        @Override
        public int getCount() {
            // Show 6 total pages.
            return 6;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String s="";
            if(position==0)
            {
                s="Profile";
            }
            if(position==1)
            {
                s="Approval List";
            }
            if(position==2)
            {
                s="Manage Clinics";
            }
            if(position==3)
            {
                s="Manage Date";
            }
            if(position==4)
            {
                s="Manage Slots";
            }
            if(position==5)
            {
                s="Today's Appointment List";
            }

            return s;
        }
    }



}
