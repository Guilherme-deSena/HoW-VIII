package com.example.teste;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ContactDAOSQLite extends SQLiteOpenHelper {

    public ContactDAOSQLite(@Nullable Context context, @Nullable String name, @Nullable  SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( "Create table Scheduling (id integer primary key autoincrement, description varchar(255), date integer, type integer)" );
    }


    public  ArrayList<String> getContacts(){
        Cursor cursor= this.getWritableDatabase().rawQuery("select nome from pessoa",null);
        ArrayList<String> lista=new ArrayList<String>();
        while(cursor.moveToNext()) {
            String nome=cursor.getString(0);
            lista.add(nome);
        }
        return lista;
    }

    public int getDayAccesses(Long dateUnix){
        Cursor cursor= this.getWritableDatabase().rawQuery("select id from Scheduling where date = " + dateUnix.toString() + " and type = 1",null);
        int accessCount = 0;
        while(cursor.moveToNext()) {
            accessCount++;
        }
        return accessCount;
    }

    public String getAudienceDescription(Long dateUnix){
        Cursor cursor = this.getWritableDatabase().rawQuery("select description from Scheduling where date = " + dateUnix.toString() + " and type = 0",null);
        String description = null;
        while(cursor.moveToNext()) {
            description = cursor.getString(0);
        }
        return description;
    }

    public int getAudienceCode(Long dateUnix){
        Cursor cursor = this.getWritableDatabase().rawQuery("select id from Scheduling where date = " + dateUnix.toString() + " and type = 0",null);
        int id = 0;
        while(cursor.moveToNext()) {
            id = cursor.getInt(0);
        }
        return id;
    }
    public int getDayAudience(Long dateUnix){
        Cursor cursor = this.getWritableDatabase().rawQuery("select id from Scheduling where date = " + dateUnix.toString() + " and type = 0",null);
        int audienceId = 0;
        while(cursor.moveToNext()) {
            audienceId = cursor.getInt(0);
        }
        if (audienceId > 0) {
            return audienceId;
        }
        return 0;
    }

    public void insertScheduling(Activity activity, String description, long dateUnix, int type){
        this.getWritableDatabase().execSQL( "insert into Scheduling (description, date, type) values(?,?,?)", new Object[]{description, dateUnix, type} );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}