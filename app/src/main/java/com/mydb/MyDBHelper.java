package com.mydb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by XJTUSE-PC on 2015/11/23.
 */
public class MyDBHelper extends SQLiteOpenHelper{

    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table teacher (tno text primary key,name text)";
        String sql2 = "create table student (sno text primary key,name text)";
        String sql3 = "create table course (cno text primary key,name text)";
        //String sql4 = "create table tc (cno text foreign key (teacher),cno text foreign key (course))";
        //String sql5 = "create table sc (sno text foreign key (student),cno text foreign key (course))";

        db.execSQL(sql);
        db.execSQL(sql2);
        db.execSQL(sql3);
        //db.execSQL(sql4);
        //db.execSQL(sql5);
        //db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
