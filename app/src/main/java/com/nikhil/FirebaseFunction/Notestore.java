package com.nikhil.FirebaseFunction;

public class Notestore {

    private String title;
    private String note;

    public Notestore(String title, String note) {
        this.title = title;
        this.note = note;
    }

    public Notestore() {
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}