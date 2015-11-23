package com.Http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.DataManager.MyDataManager;
import com.DataManager.Teacher;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
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
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ACE on 2015/11/22.
 */
public class MyHttpConnector {

    private String cookie = "ASP.NET_SessionId=heggkcytcazhbz45kbkhlv55; ASP.NET_SessionId_NS_Sig=oenCV6mdwWt3-1C_";
    private String referer = "http://121.248.70.214/jwweb/ZNPK/TeacherKBFB.aspx";


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
                teacher.id = e.attr("value");
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

    public void getCourses(String term,String tno,String type,String yzm){
        HttpClient client = new DefaultHttpClient();
        HttpResponse res = null;
        Bitmap bitmap = null;
        try {
            res = client.execute(makeCourseRequest(term, tno, type, yzm));
            HttpEntity entity = res.getEntity();
            String html = null;
            html = EntityUtils.toString(entity);
            Document doc = Jsoup.parse(html);
            Elements eles1 = doc.select("br");
            for(Element e:eles1){
               e.remove();
            }
            Elements eles = doc.select("td");
            for(Element e:eles){
                Log.i("han", e.html());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
