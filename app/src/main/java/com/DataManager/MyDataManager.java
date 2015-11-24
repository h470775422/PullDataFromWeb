package com.DataManager;

import android.content.Context;
import android.graphics.Bitmap;

import com.Http.MyHttpConnector;
import com.mydb.MyDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ACE on 2015/11/23.
 */
public class MyDataManager {

    private MyDB myDB = null;
    private MyHttpConnector connector = null;

    private List<Teacher> teachers = new ArrayList<Teacher>();

    private String teacher;

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
        for(Teacher t:teachers){
            if(t.name.equals(teacher)){
                tno = t.id;
            }
        }
    }

    private String term = "20150";
    private String tno;
    private String type = "2";
    private String validate;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    private Bitmap bitmap;

    //construst
    public MyDataManager(Context context){
        myDB = new MyDB(context);
        connector = new MyHttpConnector();


    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    public String getTno() {
        return tno;
    }

    public void setTno(String tno) {
        this.tno = tno;
    }

    public MyHttpConnector getConnector() {
        return connector;
    }

    public void setConnector(MyHttpConnector connector) {
        this.connector = connector;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValidate() {
        return validate;
    }

    public void setValidate(String validate) {
        this.validate = validate;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }


    public void getAllTeachers(){
        teachers = connector.getAllTeachers();
    }

    public void getCourses(){
        connector.getCourses(term,tno,type,validate);
    }
}

