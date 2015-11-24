package com.example.ace.myweb;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.DataManager.MyDataManager;
import com.DataManager.Teacher;
import com.Http.MyHttpConnector;
import com.mydb.MyDB;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener{


    private MyDataManager myDataManager = null;

    private ListView lv = null;
    private ImageView imageView = null;
    private EditText teacher = null;
    private EditText yzm = null;
    private Bitmap bitmap = null;
    private ViewFlipper vf = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        myDataManager = new MyDataManager(this);
        imageView = (ImageView)findViewById(R.id.validate);
        teacher = (EditText)findViewById(R.id.teacherEdit);
        yzm = (EditText)findViewById(R.id.yzm);


        vf = (ViewFlipper)findViewById(R.id.vf);



        lv = (ListView)findViewById(R.id.lv);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = ((TextView)view).getText().toString();
                myDataManager.setTeacher(name);
                teacher.setText(name);
                setRightInAnimation();
                vf.showPrevious();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                pullTeacherThread();
                initialImageView();
            }
        }).start();

    }

    final Handler handler = new Handler(){
        public void handleMessage(Message m){
            switch (m.what){
                case 1:
                    initialList();
                    break;
                case 2:
                    if(bitmap != null)
                      imageView.setImageBitmap(bitmap);
                    break;
            }
            super.handleMessage(m);
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    //获取所有信息
    private  void pullTeacherThread(){
        myDataManager.getAllTeachers();
        Message m = new Message();
        m.what = 1;
        handler.sendMessage(m);
       // myDB.insertTeacher(pd.teachers);
    }

    //初始化listview
    public void initialList(){
        List<String> list = new ArrayList<String>();
        for(Teacher t:myDataManager.getTeachers()){
            String name = t.name;
            list.add(name);
            //if(list.size() > 100)
            // break;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,list);
        lv.setAdapter(adapter);
    }

    //显示验证码
    public void initialImageView(){
        bitmap = myDataManager.getConnector().getValidateImage();
        Message m = new Message();
        m.what = 2;
        handler.sendMessage(m);
    }

    //获取验证码图片
    public void getNewValidateImage(View v){
        new Thread(new Runnable() {
            @Override
            public void run() {
                initialImageView();
            }
        }).start();
    }

    public void searchCourse(View v){
        String yzm = this.yzm.getText().toString();
        myDataManager.setValidate(yzm);
        new Thread(new Runnable() {
            @Override
            public void run() {
                myDataManager.getCourses();
            }
        }).start();
    }


    public void selectName(View view) {
        setLeftInAnimation();
        vf.showNext();
    }

    private void setLeftInAnimation(){
        vf.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.left_in));
        vf.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.right_out));
    }
    private void setRightInAnimation(){
        vf.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.right_in));
        vf.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.left_out));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
