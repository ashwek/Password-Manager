package com.nikhil.FirebaseFunction;

public class Passwordstore {


    private String sname;
    private String uname;
    private String pass;
    private String note;

    public Passwordstore() {
    }

    public Passwordstore(String sname, String uname, String pass, String note) {
        this.sname = sname;
        this.uname = uname;
        this.pass = pass;
        this.note = note;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

