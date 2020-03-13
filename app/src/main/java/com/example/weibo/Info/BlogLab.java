package com.example.weibo.Info;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.weibo.ui.home.HomeFragment.TAG;

public class BlogLab {
    public String userName;
    public int blogPosition;
    public ArrayList<Blog> blogList;
    public ArrayList<Comment> commentList;

    //私有构造器
    private BlogLab(){
        blogList = new ArrayList<>();
        commentList = new ArrayList<>();
        Log.d("MainActivity", "BlogLab: 我被创建了");
    }

    //静态内部类
    private static class BlogLabInner
    {
        private static BlogLab instance = new BlogLab();
    }

    //获取类的实例
    public static BlogLab getInstance(){ return BlogLabInner.instance; }

    //解析json数据为Blog数组
    public void setBlogList(JSONArray data, JSONArray like) throws JSONException {
        blogList.clear();
        ArrayList<String> likes = new ArrayList<>();
//        String[] likes = new String[like.length()];
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < like.length(); ++i)
        {
            likes.add(like.get(i).toString());
            stringBuilder.append(likes.get(i) + " ");
        }
        Log.d(TAG, "likeList: " + stringBuilder.toString());
        for(int i = 0; i < data.length(); ++i)
        {
            blogList.add(new Blog((JSONObject)data.get(i)));
            if(likes.contains(blogList.get(i).id))
                blogList.get(i).liked = true;
        }
    }
    //解析json数据为BComment数组
    public void setCommentList(JSONArray data) throws JSONException{
        commentList.clear();
        for(int i = 0; i < data.length(); ++i)
        {
            commentList.add(new Comment((JSONObject)data.get(i)));
        }
    }

    //评论数+1
    public void incCommentCount()
    {
        int count = Integer.parseInt(blogList.get(blogPosition).commentCount);
        ++count;
        blogList.get(blogPosition).commentCount = count + "";
    }
}
