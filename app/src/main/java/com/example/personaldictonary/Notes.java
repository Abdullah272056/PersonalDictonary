package com.example.personaldictonary;

public class Notes {
int id;
String english,bangla;

    public Notes(int id, String english, String bangla) {
        this.id = id;
        this.english = english;
        this.bangla = bangla;
    }

    public Notes() {
    }

    public Notes(String english, String bangla) {
        this.english = english;
        this.bangla = bangla;
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
