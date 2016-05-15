package com.example.kapish.mchealthapp;

/**
 * Created by karishma on 17/10/15.
 */
public class Appointments {
    int appointmentId;
    String date;
    String doctor_name;
    int clinicid;
    String clinic;
    String purpose;
    String calendar;
    String status;
    String doctor_email;
    int iconID;

    public Appointments(int appointmentId,int iconID,int clinicid,String date,String doctor_name,String doctor_email, String clinic, String purpose, String calendar, String status) {
        this.appointmentId = appointmentId;
        this.iconID = iconID;
        this.clinicid = clinicid;
        this.date = date;
        this.doctor_name  = doctor_name;
        this.doctor_email = doctor_email;
        this.clinic = clinic;
        this.purpose = purpose;
        this.calendar = calendar;
        this.status = status;
    }

    public String getDoctor_email() {
        return doctor_email;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public int getIconID() {
        return iconID;
    }
    public int getClinicid() {
        return clinicid;
    }

    public String getDate() {
        return date;
    }
    public String getDoctor_name() {
        return doctor_name;
    }

    public String getClinic() {
        return clinic;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getCalendar() {
        return calendar;
    }

    public String getStatus() {
        return status;
    }
}
