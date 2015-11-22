package com.example.ace.myweb;

import android.net.http.AndroidHttpClient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ACE on 2015/11/22.
 */
public class PullData {

    public List<Teacher> teachers = new ArrayList<Teacher>();
        public void verySimplePullDataThatYouNeverThinkOut(){
            String url1 = "http://121.248.70.214/jwweb/ZNPK/Private/List_JS.aspx?js=&xnxq=20150";

            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(url1);
                HttpResponse res = client.execute(request);
                HttpEntity entity = res.getEntity();

                String html = null;
                html = EntityUtils.toString(entity);

                Document doc = Jsoup.parse(html);
                Elements ele = doc.select("script");
                Element e = ele.first();
                String scripts = e.html();
                getTeacher(scripts);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        private void getTeacher(String html) {
            Teacher tea = null;
            while (true) {
                tea = new Teacher();
                int index = html.indexOf("value=");
                if (index == -1)
                    break;
                int i = index + 6;
                while (true) {
                    char id = html.charAt(i++);
                    if (id == '>')
                        break;
                    tea.id += id;
                }
                System.out.println(tea.id);
                while (true) {
                    char name = html.charAt(i++);
                    if (name == '<')
                        break;
                    tea.name += name;
                }
                System.out.println(tea.name);
                teachers.add(tea);
                html = html.substring(i);
            }
        }
}
