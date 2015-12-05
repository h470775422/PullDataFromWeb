package com.course;

import android.gesture.Gesture;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.DataManager.Course;
import com.example.ace.myweb.R;

import java.util.List;

public class CoursesActivity extends AppCompatActivity implements View.OnTouchListener,GestureDetector.OnGestureListener{


    private GestureDetector detector = null;
    private LayoutInflater layoutInflater = null;
    private View courseView  = null;
    private ViewFlipper vf = null;
    private List<Course> courses = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        vf = (ViewFlipper)findViewById(R.id.vf2);
        layoutInflater = LayoutInflater.from(this);
        initialCourse();
        setDetectorLisitener();

    }

    public void setDetectorLisitener(){
        detector = new GestureDetector(this,this);
        vf.setOnTouchListener(this);
        vf.setLongClickable(true);
    }

    public void initialCourse(){
        courses = (List<Course>)getIntent().getSerializableExtra("courses");
        if(courses == null || courses.size() <= 0)
            return;

        for(Course c:courses){
            courseView = layoutInflater.inflate(R.layout.activity_course, null);
            initialTestView(c,courseView);
            vf.addView(courseView);
        }
    }

    public void initialTestView(Course course,View view){
        getViewById(view,R.id.cno).setText("教师编号：" + course.getCno());
        getViewById(view,R.id.cname).setText("教师姓名：" + course.getName());
        getViewById(view,R.id.score).setText("课程学分：" + course.getScore());
        getViewById(view,R.id.type).setText("授课方式：" + course.getType());
        getViewById(view,R.id.courseType).setText("课程类别：" + course.getCourseType());
        getViewById(view,R.id.classNo).setText("上课班号：" + course.getClassNo());
        getViewById(view,R.id.className).setText("上课班级：" + course.getClassName());
        getViewById(view,R.id.number).setText("上课人数：" + course.getNumbers());
        getViewById(view,R.id.time).setText("上课时间：" + course.getTime());
        getViewById(view,R.id.address).setText("上课地址：" + course.getAddress());
    }

    private TextView getViewById(View view,int id){
        return (TextView)view.findViewById(id);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_courses, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        detector.onTouchEvent(event);
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        float x1 = e1.getX();
        float x2 = e2.getX();
        if(x2 - x1 > 50.0){
            setRightInAnimation();
            vf.showNext();
        }else if(x1 - x2 > 50.0){
            setLeftInAnimation();
            vf.showPrevious();
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    private void setLeftInAnimation(){
        vf.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.left_in));
        vf.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.right_out));
    }
    private void setRightInAnimation(){
        vf.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.right_in));
        vf.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.left_out));
    }
}
