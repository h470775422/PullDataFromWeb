package com.example.ace.myweb;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    PullData pd = new PullData();
    ListView lv = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @Override
            public void run() {
                initialList();
            }
        }).start();

    }

    Handler handler = new Handler(){
        public void handleMessage(Message m){
            lv = (ListView)findViewById(R.id.lv);
            List<String> list = new ArrayList<String>();
            for(Teacher t:pd.teachers){
                String name = t.name;
                list.add(name);
                if(list.size() > 100)
                    break;
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,list);
            lv.setAdapter(adapter);
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

    public void initialList(){
        pd.verySimplePullDataThatYouNeverThinkOut();
        Message m = new Message();
        handler.sendMessage(m);
    }
}
