package com.example.personaldictonary;

public class Constants {
    public  static final String DATABASE_NAME="PersonalDictionary.db";
    public  static final int DATABASE_Version=2;
    public  static final String TABLE_NAME="PersonalDictionary";
    public  static final String COLUMN_ID="id";
    public  static final String COLUMN_ENGLISH="English";
    public  static final String COLUMN_BANGLA="Bangla";


    public static final String CREATE_TABLE  = " CREATE TABLE "+TABLE_NAME+"("
                +COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +COLUMN_ENGLISH+" TEXT, "
                +COLUMN_BANGLA+" TEXT "
                +")";

}
