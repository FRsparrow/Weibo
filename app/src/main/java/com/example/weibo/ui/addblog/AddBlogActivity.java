package com.example.weibo.ui.addblog;

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
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.weibo.Info.BlogLab;
import com.example.weibo.R;
import com.example.weibo.Utils;
import com.example.weibo.ui.dashboard.DashboardFragment;
import com.example.weibo.ui.home.HomeFragment;

import org.json.JSONObject;

import java.util.Date;

public class AddBlogActivity extends AppCompatActivity {

    private EditText blogContent;
    private String userName;
    private String content;
    private static final int SUCCESS = 0;
    private static final int FAILED = 1;
    private static final String TAG = "MainActivity";

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if(msg.what == SUCCESS)
            {
                HomeFragment.needRefresh = true;
                DashboardFragment.needRefresh = true;
                Toast.makeText(getApplicationContext(), "发布成功！", Toast.LENGTH_SHORT).show();
                finish();
            }
            else if(msg.what == FAILED)
            {
                Toast.makeText(getApplicationContext(), "发送失败", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    });

    private class Writter extends AsyncTask<Void, Void, Void>
    {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(Void... voids) {
            String status = null;
            Message msg = new Message();
            try{
                String params = "userName=" + userName
                        + "&content=" + content
                        + "&commentCount=0"
                        + Utils.getTimeParam()
                        + "&likeCount=0";
                Log.d(TAG, "doInBackground: " + params);
                status = Utils.getStatus(params, "blog/send");
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

    private void initToolbar()
    {
        blogContent = (EditText) findViewById(R.id.add_blog_content);
        Toolbar toolbar = findViewById(R.id.add_blog_toolbar);
        toolbar.inflateMenu(R.menu.add_blog_menu);
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
                //发布微博
                if(item.getItemId() == R.id.add_blog_add)
                {
                    userName = BlogLab.getInstance().userName;
                    content = blogContent.getText().toString();
                    if(!content.equals(""))
                        new Writter().execute();
                    else
                        Toast.makeText(getApplicationContext(), "微博内容不允许为空！", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_blog);
        initToolbar();

    }
}
