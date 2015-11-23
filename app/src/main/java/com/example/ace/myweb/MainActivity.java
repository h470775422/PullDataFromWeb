package com.example.ace.myweb;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.DataManager.MyDataManager;
import com.DataManager.Teacher;
import com.Http.MyHttpConnector;
import com.mydb.MyDB;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private MyDataManager myDataManager = null;

    ListView lv = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDataManager = new MyDataManager(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                pullTeacherThread();
            }
        }).start();

    }

    final Handler handler = new Handler(){
        public void handleMessage(Message m){
        switch (m.what){
            case 1:
                initialList();
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
        lv = (ListView)findViewById(R.id.lv);
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


}
