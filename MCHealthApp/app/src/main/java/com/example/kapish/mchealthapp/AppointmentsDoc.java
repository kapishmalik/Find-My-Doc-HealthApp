package com.example.kapish.mchealthapp;


public class AppointmentsDoc {
    String date;
    String pat_name;
    String slot;

    String status;
    String iconID;

    public AppointmentsDoc(String iconID,String date,String pat_name, String slot, String status) {
        this.iconID = iconID;
        this.date = date;
        this.pat_name = pat_name;

        this.slot = slot;

        this.status = status;
    }
    public String getIconID() {
        return iconID;
    }

    public String getDate() {
        return date;
    }
    public String getPat_name() {
        return pat_name;
    }



    public String getSlot() {
        return slot;
    }


    public String getStatus() {
        return status;
    }
}

