package com.example.weibo.Info;

import org.json.JSONException;
import org.json.JSONObject;

public class Blog {
    public String id;
    public String userName;
    public String content;
    public String commentCount;
    public String likeCount;
    public String upYear;
    public String upMonth;
    public String upDay;
    public String upHour;
    public String upMinute;
    public boolean liked;

    public Blog(String userName, String content, String commentCount, String upYear, String upMonth, String upDay, String upHour, String upMinute) {
        this.userName = userName;
        this.content = content;
        this.commentCount = commentCount;
        this.upYear = upYear;
        this.upMonth = upMonth;
        this.upDay = upDay;
        this.upHour = upHour;
        this.upMinute = upMinute;
    }

    public Blog(JSONObject blogData) throws JSONException
    {
        id = blogData.getString("id");
        userName = blogData.getString("userName");
        content = blogData.getString("content");
        commentCount = blogData.getString("commentCount");
        likeCount = blogData.getString("likeCount");
        upYear = blogData.getString("upYear");
        upMonth = blogData.getString("upMonth");
        upDay = blogData.getString("upDay");
        upHour = blogData.getString("upHour");
        upMinute = blogData.getString("upMinute");
    }
}
