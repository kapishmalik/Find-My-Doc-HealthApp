package com.example.kapish.mchealthapp;

/**
 * Created by karishma on 16/10/15.
 */
public class Doctors {
    private String doctors_email;
    private String doctors_name;
    private String speciality;
    private int iconID;
    private int charges;

    public Doctors(String doctors_email,String doctors_name, String speciality, int iconID, int charges) {
        this.doctors_email = doctors_email;
        this.doctors_name = doctors_name;
        this.speciality = speciality;
        this.iconID = iconID;
        this.charges = charges;
    }
    public String getDoctors_email() {
        return doctors_email;
    }

    public String getDoctors_name() {
        return doctors_name;
    }

    public String getSpeciality() {
        return speciality;
    }

    public int getIconID() {
        return iconID;
    }

    public int getCharges() {
        return charges;
    }
}