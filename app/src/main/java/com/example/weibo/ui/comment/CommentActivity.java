package com.example.weibo.ui.comment;

import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weibo.Info.BlogLab;
import com.example.weibo.R;
import com.example.weibo.Utils;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

public class CommentActivity extends AppCompatActivity {

    private int position;
    private String commentContent;
    private TextInputEditText comment;
    private Button sendComment;
    private CommentAdapter adapter = null;

    private static final int SUCCESS = 0;
    private static final int FAILED = 1;
    private static final int FETCHSUCCESS = 2;
    private static final int FETCHFAILED = 3;
    private static final String TAG = "MainActivity";

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            String showInfo = "";
            if(msg.what == SUCCESS)
            {
                showInfo = "评论成功";
//                BlogLab blogLab = BlogLab.getInstance();
//                blogLab.incCommentCount();
                comment.getText().clear();
                new CommentFetcher().execute();
            } else if(msg.what == FAILED)
            {
                showInfo = "评论失败";
            }else if(msg.what == FETCHSUCCESS)
            {
                showInfo = "刷新成功";
                showData();
            }else if(msg.what == FETCHFAILED)
            {
                showInfo = "刷新失败";
            }
            Toast.makeText(getApplicationContext(), showInfo, Toast.LENGTH_SHORT).show();
            return false;
        }
    });

    private void showData()
    {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.comment_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
//        Log.d(TAG, "showData: list size:" + weatherInfos.size());
//        WeatherAdapter adapter = new WeatherAdapter(weatherInfos, isPhone);
        if(adapter == null){
            CommentAdapter adapter = new CommentAdapter();
            recyclerView.setAdapter(adapter);
        }else{
            adapter.notifyDataSetChanged();
        }
    }

    private class CommentFetcher extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            BlogLab blogLab = BlogLab.getInstance();
            Message msg = new Message();
            try{
                String pathOnServer = "blog/readcomment";
                String params = "blogId=" + blogLab.blogList.get(position).id;
                JSONObject res = Utils.getJSONObject(pathOnServer, params);
                if(res.getString("status").equals("ok"))
                {
                    JSONArray data = res.getJSONArray("data");
                    blogLab.setCommentList(data);
                    msg.what = FETCHSUCCESS;
                }
            } catch (Exception e)
            {
                msg.what = FETCHFAILED;
                e.printStackTrace();
            }finally {
                handler.sendMessage(msg);
            }
            return null;
        }
    }

    private class CommentSender extends AsyncTask<Void, Void, Void>
    {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(Void... voids) {
            String status = null;
            Message msg = new Message();
            try{
                BlogLab blogLab = BlogLab.getInstance();
                String userName = blogLab.userName;
                String params = "blogId=" + blogLab.blogList.get(position).id
                        + "&userName=" + userName
                        + "&content=" + commentContent
                        + Utils.getTimeParam();
                status = Utils.getStatus(params, "blog/sendcomment");
            } catch (Exception e)
            {
                e.printStackTrace();
            }finally {
                if(status != null)
                {
                    if(status.equals("ok"))
                        msg.what = SUCCESS;
                    else
                        msg.what = FAILED;
                }
                else
                    msg.what = FAILED;
                handler.sendMessage(msg);
            }
            return null;
        }
    }

    void initView()
    {
        new CommentFetcher().execute();
        position = BlogLab.getInstance().blogPosition;
        comment = (TextInputEditText) findViewById(R.id.comment_input);
        sendComment = (Button) findViewById(R.id.send_comment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.comment_toolbar);
        toolbar.inflateMenu(R.menu.refresh_menu);
        toolbar.setNavigationIcon(R.drawable.backs);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.refresh)
                    new CommentFetcher().execute();
                return true;
            }
        });
        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentContent = comment.getText().toString();
                if(!commentContent.equals(""))
                    new CommentSender().execute();
                else
                    Toast.makeText(getApplicationContext(), "请输入评论内容！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_comment);
        initView();
    }
}
