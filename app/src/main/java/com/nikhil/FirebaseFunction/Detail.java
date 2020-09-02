package com.nikhil.FirebaseFunction;

public class Detail {

    private String Uid;
    private String Email;
    private String fname;
    private String mname;
    private String lname;
    private String mpass;
    private String ran;
    private Integer mpin;

    public Detail() {
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getMpass() {
        return mpass;
    }

    public void setMpass(String mpass) {
        this.mpass = mpass;
    }

    public String getRan() {
        return ran;
    }

    public void setRan(String ran) {
        this.ran = ran;
    }

    public Integer getMpin() {
        return mpin;
    }

    public void setMpin(Integer mpin) {
        this.mpin = mpin;
    }
}
