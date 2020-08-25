package com.example.personaldictonary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    Context context;

    public DataBaseHelper(@Nullable Context context) {
        super(context, Constants.TABLE_NAME, null, Constants.DATABASE_Version);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Constants.CREATE_TABLE);
        Toast.makeText(context, "On created is called", Toast.LENGTH_SHORT).show();
    }

   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL(" DROP TABLE IF EXISTS "+Constants.TABLE_NAME);
//        onCreate(db);
   }

    public long insertData(Notes notes){
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(Constants.COLUMN_ENGLISH,notes.getEnglish());
        contentValues.put(Constants.COLUMN_BANGLA,notes.getBangla());

        long id=sqLiteDatabase.insert(Constants.TABLE_NAME,null,contentValues);
        return id;
    }


    public List<Notes> getAllNotes(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        List<Notes> dataList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+Constants.TABLE_NAME,
                null);
        if (cursor.moveToFirst()){
            do {
                Notes note = new Notes(cursor.getInt(cursor.getColumnIndex(Constants.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(Constants.COLUMN_ENGLISH)),
                        cursor.getString(cursor.getColumnIndex(Constants.COLUMN_BANGLA)));

                dataList.add(note);
            }while (cursor.moveToNext());
        }
        return dataList;
    }


    public int updateData(Notes notes){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.COLUMN_ENGLISH,notes.getEnglish());
        contentValues.put(Constants.COLUMN_BANGLA,notes.getBangla());
        int status = sqLiteDatabase.update(Constants.TABLE_NAME,contentValues," id=? ",new String[]{String.valueOf(notes.getId())});
        return status;
    }

    public int deleteData(int id){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        int status = sqLiteDatabase.delete(Constants.TABLE_NAME,"id=?",new String[]{String.valueOf(id)});
        return status;

    }
}
