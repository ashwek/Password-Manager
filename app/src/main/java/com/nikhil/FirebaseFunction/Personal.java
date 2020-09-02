package com.nikhil.FirebaseFunction;

public class Personal {
    private String First_name;
    private String Last_name;
    private String Nickname;
    private String Email;
    private String Website;
    private String Address;
    private String Mobile_num;
    private String Birth_date;
    private String extranote;
    private String title;

    public Personal() {
    }

    public Personal(String title, String first_name, String last_name, String nickname, String email, String website, String address, String mobile_num, String birth_date, String extranote) {
        this.title = title;
        this.First_name = first_name;
        this.Last_name = last_name;
        this.Nickname = nickname;
        this.Email = email;
        this.Website = website;
        this.Address = address;
        this.Mobile_num = mobile_num;
        this.Birth_date = birth_date;
        this.extranote = extranote;
    }

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}

    public String getFirst_name() {
        return First_name;
    }

    public void setFirst_name(String first_name) {
        First_name = first_name;
    }

    public String getLast_name() {
        return Last_name;
    }

    public void setLast_name(String last_name) {
        Last_name = last_name;
    }

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getWebsite() {
        return Website;
    }

    public void setWebsite(String website) {
        Website = website;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getMobile_num() {
        return Mobile_num;
    }

    public void setMobile_num(String mobile_num) {
        Mobile_num = mobile_num;
    }

    public String getBirth_date() {
        return Birth_date;
    }

    public void setBirth_date(String birth_date) {
        Birth_date = birth_date;
    }

    public String getExtranote() {
        return extranote;
    }

    public void setExtranote(String extranote) {
        this.extranote = extranote;
    }

}