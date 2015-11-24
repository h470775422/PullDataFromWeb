package com.mydb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.DataManager.Course;
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
//    c.setCno(cno.equals("") ? preCourse.getCno() : cno);
//    c.setName(name.equals("") ? preCourse.getName() : name);
//    c.setScore(eles.get(index).html().equals("") ? preCourse.getScore() : eles.get(index++).html()
//
//    );
//    c.setType(eles.get(index).html().equals("") ? preCourse.getType() : eles.get(index++).html());
//    c.setCourseType(eles.get(index).html().equals("") ? preCourse.getCourseType() : eles.get(index++).html());
//    c.setClassNo(eles.get(index).html().equals("") ? preCourse.getClassNo() : eles.get(index++).html());
//    c.setClassName(eles.get(index).html().equals("") ? preCourse.getClassName() : eles.get(index++).html());
//    c.setNumbers(eles.get(index).html().equals("") ? preCourse.getNumbers() : eles.get(index++).html());
//    c.setTime(eles.get(index).html().equals("") ? preCourse.getTime() : eles.get(index++).html());
//    c.setAddress(eles.get(index).html().equals("") ? preCourse.getAddress() : eles.get(index++).html());
    public void insertCourses(List<Course> courses,String tno){
        String courseSql = "insert into course values(?,?,?,?,?,?,?,?,?,?)";
        String tnoSql = "insert into tc values(?,?)";
        String preCno = "";
        for(Course c:courses){
            String[] items = new String[]{c.getCno(),c.getName(),c.getScore(),c.getType(),c.getCourseType(),c.getClassNo(),
                        c.getClassName(),c.getNumbers(),c.getTime(),c.getAddress()};

            readDB.execSQL(courseSql,items);
            if(!c.getCno().equals(preCno)){
                String[] items2 = new String[]{c.getCno(),tno};
                readDB.execSQL(tnoSql,items2);
                preCno = c.getCno();
            }

        }

    }
    public void closeDB(){
        writeDB.close();
        readDB.close();
    }
}
