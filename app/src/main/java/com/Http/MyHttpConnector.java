package com.Http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.DataManager.Course;
import com.DataManager.Teacher;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ACE on 2015/11/22.
 */
public class MyHttpConnector {

    private String cookie = "ASP.NET_SessionId=lm5oiwrhduakcobhl20bn2q4";
    private String referer = "http://121.248.70.214/jwweb/ZNPK/TeacherKBFB.aspx";


    public MyHttpConnector(){
    }

    public void getCookieFromResponse(){
        String url = "http://121.248.70.214/jwweb/ZNPK/TeacherKBFB.aspx";
        HttpGet request = new HttpGet(url);
        HttpClient client = new DefaultHttpClient();
        try {
            client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Cookie> cookies = ((AbstractHttpClient) client).getCookieStore().getCookies();
        System.out.println("开始写Cookie啦！！！！！！！！！！！！！！！！！！");
        if (cookies.isEmpty()) {
            System.out.println("cookies is empty");
        } else {
            for (int i = 0; i < cookies.size(); i++) {
                cookie = "";
                cookie += cookies.get(i).getName();
                cookie += "=";
                cookie += cookies.get(i).getValue();
                Log.i("cooke",cookie);
            }
        }
    }

    private HttpGet makeTeacherRequest(String grade, String name) {
        String url = "http://121.248.70.214/jwweb/ZNPK/Private/List_JS.aspx?xnxq=";
        url += grade;
        url += "&js=";
        try {
            url += URLEncoder.encode(name, "gb2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpGet requset = new HttpGet(url);
        requset.setHeader("Cookie", cookie);
        return requset;
    }

    private HttpGet makeValidateImageRequest() {
        long mills = System.currentTimeMillis();
        String url = "http://121.248.70.214/jwweb/sys/ValidateCode.aspx?t=";
        url += mills;
        HttpGet requset = new HttpGet(url);
        requset.setHeader("Cookie", cookie);

        return requset;
    }

    private HttpPost makeCourseRequest(String term, String tno, String type, String validate) {
        String url = "http://121.248.70.214/jwweb/ZNPK/TeacherKBFB_rpt.aspx";
        HttpPost request = new HttpPost(url);
        request.setHeader("Cookie", cookie);
        request.setHeader("Referer", referer);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();

        NameValuePair p1 = new BasicNameValuePair("Sel_XNXQ", term);
        NameValuePair p2 = new BasicNameValuePair("Sel_JS", tno);
        NameValuePair p3 = new BasicNameValuePair("type", type);
        NameValuePair p4 = new BasicNameValuePair("txt_yzm", validate);
        pairs.add(p1);
        pairs.add(p2);
        pairs.add(p3);
        pairs.add(p4);
        UrlEncodedFormEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(pairs);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        request.setEntity(entity);
        return request;
    }


    //获取所有老师信息
    public List<Teacher> getAllTeachers() {
        List<Teacher> teachers = new ArrayList<Teacher>();
        try {
            HttpClient client = new DefaultHttpClient();
            HttpResponse res = client.execute(makeTeacherRequest("20150", ""));
            HttpEntity entity = res.getEntity();

            String html = null;
            html = EntityUtils.toString(entity);

            Document doc = Jsoup.parse(html);
            Elements eles = doc.select("script");
            Element element = eles.first();
            doc = Jsoup.parse(element.html());

            eles = doc.getElementsByTag("option");
            for (Element e : eles) {
                Teacher teacher = new Teacher();
                teacher.no = e.attr("value");
                teacher.name = e.text();
                if (teacher.name.equals(""))
                    continue;
                teachers.add(teacher);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return teachers;
    }


    public Bitmap getValidateImage() {
        HttpClient client = new DefaultHttpClient();
        HttpResponse res = null;
        Bitmap bitmap = null;
        try {
            res = client.execute(makeValidateImageRequest());
            HttpEntity entity = res.getEntity();
            InputStream in = entity.getContent();
            bitmap = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public List<Course> getCourses(String term,String tno,String type,String yzm) {
        HttpClient client = new DefaultHttpClient();
        HttpResponse res;
        String html = null;
        HttpEntity entity;
        List<Course> courses = new ArrayList<>();
        try {
            res = client.execute(makeCourseRequest(term, tno, type, yzm));
            entity = res.getEntity();
            html = EntityUtils.toString(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("all", html);
        Document doc = Jsoup.parse(html);
        Elements eles1 = doc.select("br");
        for (Element e : eles1) {
            e.remove();
        }

        Elements eles = doc.select("tbody>tr>td");
//            int g = 0;
//            for(Element e:eles){
//                Log.i("韩翔辉", e.html() + ".........." + g++);
//            }
        boolean begin = false;
        Course preCourse = null;
        for (int index = 0; index < eles.size(); index++) {
            if (eles.get(index).html().equals("地点")) {
                begin = true;
                continue;
            } else if (eles.get(index).html().equals("<b>注1：</b>")) {
                break;
            }
            if (begin) {
                Course c = new Course();
                ++index;
                String[] strs = getCno(eles.get(index++).html());
                String cno = strs[0];
                String name = strs[1];
                c.setCno(cno.equals("") ? preCourse.getCno() : cno);
                c.setName(name.equals("") ? preCourse.getName() : name);
                c.setScore(eles.get(index).html().equals("") ? preCourse.getScore() : eles.get(index).html());index++;
                c.setType(eles.get(index).html().equals("") ? preCourse.getType() : eles.get(index).html());index++;
                c.setCourseType(eles.get(index).html().equals("") ? preCourse.getCourseType() : eles.get(index).html());index++;
                c.setClassNo(eles.get(index).html().equals("") ? preCourse.getClassNo() : eles.get(index).html());index++;
                c.setClassName(eles.get(index).html().equals("") ? preCourse.getClassName() : eles.get(index).html());index++;
                c.setNumbers(eles.get(index).html().equals("") ? preCourse.getNumbers() : eles.get(index).html());index++;
                c.setTime(eles.get(index).html().equals("") ? preCourse.getTime() : eles.get(index).html());index++;
                c.setAddress(eles.get(index).html().equals("") ? preCourse.getAddress() : eles.get(index).html());
                if(!c.getCno().equals(""))
                    preCourse = c;
                courses.add(c);
                Log.i("课程表",c.getCno()+".."+c.getName());
            }

        }
        return courses;
    }

    public String[] getCno(String name) {
        if(name.indexOf("[") == -1){
            String[] d = new String[]{"",""};
            return d;
        }
        String[] strs = name.split("\\[");
        String s = strs[1];
        return s.split("\\]");
    }
}
