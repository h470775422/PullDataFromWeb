package com.DataManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ACE on 2015/11/24.
 */
public class Course {

    private String cno = "";//序号
    private String name = "";//课程名
    private String score = "";//学分
    private String type = "";//授课方式
    private String courseType = "";//课程类别
    private String classNo = "";//上课班号
    private String className = "";//上课班级
    private String numbers = "";//上课人数
    private String time = "";//上课时间
    private String address = "";//上课地址

    public String getTime() { return time;}

    public void setTime(String time) {
        this.time = time;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getCourseType() {
        return courseType;
    }
    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public String getCno() {
        return cno;
    }

    public void setCno(String cno) {
        this.cno = cno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClassNo() {
        return classNo;
    }

    public void setClassNo(String classNo) {
        this.classNo = classNo;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }




}
