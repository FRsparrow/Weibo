package com.example.weibo.Info;

import org.json.JSONException;
import org.json.JSONObject;

public class Comment {
    public String id;
    public String blogId;
    public String userName;
    public String content;
    public String upYear;
    public String upMonth;
    public String upDay;
    public String upHour;
    public String upMinute;

    public Comment(JSONObject commentData) throws JSONException
    {
        id = commentData.getString("id");
        blogId = commentData.getString("blogId");
        userName = commentData.getString("userName");
        content = commentData.getString("content");
        upYear = commentData.getString("upYear");
        upMonth = commentData.getString("upMonth");
        upDay = commentData.getString("upDay");
        upHour = commentData.getString("upHour");
        upMinute = commentData.getString("upMinute");
    }
}
