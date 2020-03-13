package com.example.weibo;

import android.icu.util.Calendar;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.weibo.Info.Blog;
import com.example.weibo.Info.Comment;
import com.example.weibo.ui.login.LoginActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Utils {
    public static final String TAG = LoginActivity.TAG;

    public static JSONObject getJSONObject(String pathOnServer, String params) throws IOException
    {
        String urlstr = "http://192.168.137.1:80/" + pathOnServer;
//        String urlstr = "http://10.0.2.2:80/" + pathOnServer;
//        String urlstr = "http://127.0.0.1:80/" + pathOnServer;
        //String urlstr="http://www.baidu.com";
        //建立网络连接
        URL url = new URL(urlstr);
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        //往网页写入POST数据，和网页POST方法类似，参数间用‘&’连接
//        String params = "uid=" + userName + '&' + "pwd=" + password;
        http.setRequestMethod("POST");
        http.setConnectTimeout(2000);
        http.setReadTimeout(2000);
        http.setDoOutput(true);
        http.setDoInput(true);

        //设置请求属性
        //获得数据字节数据，请求数据流的编码，必须和下面服务器端处理请求流的编码一致
        byte[] requestStringBytes = null;
        if(params != null)
        {
            requestStringBytes = params.getBytes();
            http.setRequestProperty("Content-length", "" + requestStringBytes.length);
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        }



        OutputStream out = http.getOutputStream();
        if(requestStringBytes != null)
            out.write(requestStringBytes);//post提交参数
        out.flush();
        out.close();

        if(http.getResponseCode() == HttpURLConnection.HTTP_OK)
            Log.d(TAG, "connect success");
        else {
            Log.d(TAG, "status:" + http.getResponseCode());
            return null;
        }
        Log.d(TAG, "endif");

        Log.d(TAG, "post success");

        //读取网页返回的数据
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(http.getInputStream()));//获得输入流
        Log.d(TAG, "read success");
        String line="";
        StringBuilder sb=new StringBuilder();//建立输入缓冲区
        while (null != (line=bufferedReader.readLine())){//结束会读入一个null值
            sb.append(line);//写缓冲区
        }
        String result= sb.toString();//返回结果
        Log.d(TAG, "backresult:" + result);

        try {
            /*获取服务器返回的JSON数据*/
            JSONObject jsonObject= new JSONObject(result);
            Log.d(TAG, "get success");
            return jsonObject;
//            }
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("log_tag", "the Error parsing data "+e.toString());
            return null;
        }
    }

    public static String getStatus(String params, String type) throws Exception
    {
        //参数间用‘&’连接
        try {
            /*获取服务器返回的JSON数据*/
            JSONObject jsonObject = getJSONObject(type, params);
            return jsonObject.getString("status");

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("log_tag", "the Error parsing data "+e.toString());
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String getTimeParam()
    {
        Calendar now = Calendar.getInstance();
        return "&upYear=" + now.get(Calendar.YEAR)
                + "&upMonth=" + (now.get(Calendar.MONTH) + 1)
                + "&upDay=" + now.get(Calendar.DAY_OF_MONTH)
                + "&upHour=" + now.get(Calendar.HOUR_OF_DAY)
                + "&upMinute=" + now.get(Calendar.MINUTE);
    }

    //返回blog时间的h:m y-m-d格式
    public static String FormatTime(Blog blog)
    {
        return blog.upHour + ":" + blog.upMinute
                + " " + blog.upYear + "-" + blog.upMonth + "-" + blog.upDay;
    }

    //返回comment时间的h:m y-m-d格式
    public static String FormatTime(Comment comment)
    {
        return comment.upHour + ":" + comment.upMinute
                + " " + comment.upYear + "-" + comment.upMonth + "-" + comment.upDay;
    }

}
