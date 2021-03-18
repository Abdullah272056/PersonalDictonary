package com.example.personaldictonary;

import android.content.Context;

public class Note {
    Context context;
    int id;
    String english,bangla;

    public Note(Context context, int id, String english, String bangla) {
        this.context = context;
        this.id = id;
        this.english = english;
        this.bangla = bangla;
    }

    public Note() {
    }

    public Note(int id, String english, String bangla) {
        this.id = id;
        this.english = english;
        this.bangla = bangla;
    }

    public Note(String english, String bangla) {
        this.english = english;
        this.bangla = bangla;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getBangla() {
        return bangla;
    }

    public void setBangla(String bangla) {
        this.bangla = bangla;
    }
}
