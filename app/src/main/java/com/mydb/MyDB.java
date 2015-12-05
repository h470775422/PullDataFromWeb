package com.mydb;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.DataManager.Course;
import com.DataManager.Teacher;

import java.util.ArrayList;
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
            writeDB.execSQL(sql,new String[]{t.no,t.name});
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

    public List<Teacher> checkTeacherInfo(){
        String sql = "select * from teacher";
        Cursor cursor = readDB.rawQuery(sql,null);
        List<Teacher> teachers = new ArrayList<>();
        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                Teacher teacher = new Teacher();
                teacher.no = cursor.getString(cursor.getColumnIndex("tno"));
                teacher.name = cursor.getString(cursor.getColumnIndex("name"));
                teachers.add(teacher);
            }
        }
        return teachers;
    }


    public List<Course> checkCourses(String tno){
        String sql = "select course.* from course,tc where tc.tno = ? and tc.cno = course.cno";
        Cursor cursor = readDB.rawQuery(sql,new String[]{tno});
        List<Course> courses = new ArrayList<>();
        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                Course course = new Course();
                course.setCno(cursor.getString(cursor.getColumnIndex("cno")));
                course.setName(cursor.getString(cursor.getColumnIndex("name")));
                course.setScore(cursor.getString(cursor.getColumnIndex("score")));
                course.setType(cursor.getString(cursor.getColumnIndex("type")));
                course.setCourseType(cursor.getString(cursor.getColumnIndex("courseType")));
                course.setClassNo(cursor.getString(cursor.getColumnIndex("classNo")));
                course.setClassName(cursor.getString(cursor.getColumnIndex("className")));
                course.setNumbers(cursor.getString(cursor.getColumnIndex("numbers")));
                course.setTime(cursor.getString(cursor.getColumnIndex("time")));
                course.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                courses.add(course);
            }
        }
        return courses;
    }
    public void closeDB(){
        writeDB.close();
        readDB.close();
    }
}
