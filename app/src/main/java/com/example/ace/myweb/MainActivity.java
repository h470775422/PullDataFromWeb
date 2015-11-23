package com.example.ace.myweb;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.DataManager.MyDataManager;
import com.DataManager.Teacher;
import com.Http.MyHttpConnector;
import com.mydb.MyDB;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private MyDataManager myDataManager = null;

    ListView lv = null;
    ImageView imageView = null;
    EditText teacher = null;
    EditText yzm = null;
    private Bitmap bitmap = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        myDataManager = new MyDataManager(this);
        imageView = (ImageView)findViewById(R.id.validate);
        teacher = (EditText)findViewById(R.id.teacherEdit);
        yzm = (EditText)findViewById(R.id.yzm);
        lv = (ListView)findViewById(R.id.lv);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = ((TextView)view).getText().toString();
                myDataManager.setTeacher(name);
                teacher.setText(name);
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

    private  void pullTeacherThread(){
        myDataManager.getAllTeachers();
        Message m = new Message();
        m.what = 1;
        handler.sendMessage(m);
       // myDB.insertTeacher(pd.teachers);
    }


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

    public void initialImageView(){
        bitmap = myDataManager.getConnector().getValidateImage();
        Message m = new Message();
        m.what = 2;
        handler.sendMessage(m);
    }

    public void getNewValidateImage(View v){
        new Thread(new Runnable() {
            @Override
            public void run() {
                initialImageView();
            }
        }).start();
    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radioType1:
                if (checked)
                    myDataManager.setType("1");
                    break;
            case R.id.radioType2:
                if (checked)
                    myDataManager.setType("2");
                    break;
        }
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


}
