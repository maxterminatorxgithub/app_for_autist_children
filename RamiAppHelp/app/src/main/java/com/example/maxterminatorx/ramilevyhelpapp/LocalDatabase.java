package com.example.maxterminatorx.ramilevyhelpapp;

/**
 * Created by maxterminatorx on 18-Dec-17.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import serverredweb.Child;


public class LocalDatabase extends SQLiteOpenHelper{

    static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "local_db.db";


    public LocalDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE children (hash VARCHAR(10) PRIMARY KEY, name VARCHAR(32))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS children");

    }

    public boolean addChild(Child c){
        SQLiteDatabase readDB = getReadableDatabase();
        Cursor row = readDB.rawQuery("SELECT * FROM children WHERE hash = '"+c.getHash()+"'",null);

        if(row.getCount()>0)
            return false;


        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO children VALUES('"+c.getHash()+"','"+c.getName()+"')");
        return true;
    }

    public ArrayList<Child> getAllChildren(){
        SQLiteDatabase readDB = getReadableDatabase();

        Cursor data = readDB.rawQuery("SELECT * FROM children",null);

        ArrayList<Child> children = new ArrayList<Child>();

        int counter = 0;

        do{
            children.add(new Child(0,0,
                    data.getString(data.getColumnIndex("name")),
                    data.getString(data.getColumnIndex("hash"))));

            data.moveToNext();
        }while(data.isAfterLast());

        return children;

    }


}
