package com.nikhil.FirebaseFunction;


public class Activationstore {
    private String softname;
    private String key;
    private String date;
    private String note;

    public Activationstore(String softname, String key, String date, String note) {
        this.softname = softname;
        this.key = key;
        this.date = date;
        this.note = note;
    }

    public Activationstore() {
    }

    public String getSoftname() {
        return softname;
    }

    public void setSoftname(String softname) {
        this.softname = softname;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}