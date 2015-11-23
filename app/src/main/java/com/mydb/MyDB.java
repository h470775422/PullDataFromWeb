package com.mydb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.DataManager.Teacher;

import java.util.List;

/**
 * Created by XJTUSE-PC on 2015/11/23.
 */
public class MyDB {

    MyDBHelper dbHelper;
    SQLiteDatabase writeDB = null,readDB = null;

    public MyDB(Context context){
        dbHelper = new MyDBHelper(context,"data.db",null,1);
        writeDB = dbHelper.getWritableDatabase();
        readDB = dbHelper.getReadableDatabase();
    }

    public void insertTeachers(List<Teacher> teachers){
        String sql = "insert into teacher values(?,?)";
        for(Teacher t:teachers){
            writeDB.execSQL(sql,new String[]{t.id,t.name});
        }
    }

//    public List<Teacher> getTeachers(){
//        //readDB.ex
//
//    }

    public void closeDB(){
        writeDB.close();
        readDB.close();
    }
}
