package com.example.kapish.mchealthapp;

import java.util.Date;

/**
 * Created by user on 11/13/2015.
 */
public class DatesDoc {
    String clinic_name;
    String date;
    DatesDoc(String name,String date)
    {
        this.clinic_name = name;
        this.date = date;
    }
    public String getClinic_name()
    {
        return this.clinic_name;
    }

    public String getDate() {
        return this.date;
    }
}
