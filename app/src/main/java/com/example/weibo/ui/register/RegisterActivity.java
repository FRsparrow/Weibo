package com.example.weibo.ui.register;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.weibo.R;
import com.example.weibo.Utils;
import com.example.weibo.ui.login.LoginActivity;

public class RegisterActivity extends AppCompatActivity {
    private EditText mUserName;
    private EditText mPassword;
    private Button mRegister;
    private String userName;
    private String password;
    private int SUCCESS = 0;
    private int ILLEGAL = 1;
    private int FAIL = 2;
    public static final String TAG = LoginActivity.TAG;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            String showInfo = "";
            if(msg.what == SUCCESS)
            {
                showInfo = "注册成功";

            }
            else if(msg.what == ILLEGAL)
            {
                showInfo = "用户名已存在或不合法;";
            }
            else if(msg.what == FAIL)
            {
                showInfo = "网络连接失败";
            }
            Log.d(TAG, showInfo);
            Toast.makeText(getApplicationContext(), showInfo, Toast.LENGTH_SHORT).show();
        }
    };

    //初始化界面
    private void initView()
    {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        mUserName = (EditText) findViewById(R.id.userName);
        mPassword = (EditText) findViewById(R.id.password);
        mRegister = (Button) findViewById(R.id.register);
    }

    private class Register extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            String status = null;
            try
            {
                String params = "userName=" + userName + '&' + "password=" + password;
                status = Utils.getStatus(params,"user/register");
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            finally {
                Message msg = new Message();
                if(status != null) {
                    if (status.equals("ok"))  //取得数据成功
                    {
                        Log.d(TAG, "doInBackground: 注册成功");
                        msg.what = SUCCESS;
                    } else if (status.equals("userName existed")) {
                        Log.d(TAG, "doInBackground: 用户名已存在");
                        msg.what = ILLEGAL;
                    } else {
                        Log.d(TAG, "doInBackground: 注册失败");
                        msg.what = FAIL;
                    }
                }
                else
                {
                    Log.d(TAG, "doInBackground: status为空");
                    msg.what = FAIL;
                }
                handler.sendMessage(msg);
            }
            return null;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = mUserName.getText().toString();
                password = mPassword.getText().toString();
                if(userName == null || userName.length()<=0){
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(),"请输入账号", Toast.LENGTH_LONG).show();
                    Looper.loop();

                }
                if(password == null|| password.length() <= 0){
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(),"请输入密码", Toast.LENGTH_LONG).show();
                    Looper.loop();
                }

                new Register().execute();
            }
        });
    }
}
