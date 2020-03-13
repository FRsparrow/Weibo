package com.example.weibo.ui.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.weibo.Info.BlogLab;
import com.example.weibo.R;
import com.example.weibo.Utils;
import com.example.weibo.ui.main.MainActivity;
import com.example.weibo.ui.register.RegisterActivity;


public class LoginActivity extends AppCompatActivity {
    private SharedPreferences sp;
    private EditText mUserName;
    private EditText mPassword;
    private CheckBox mCheckBox;
    private Button mLogin;
    private Button mRegister;
    private String userName;
    private String password;

    private static final int WRONG = 0;
    private static final int COMPLETED = 1;
    private static final int NOTEXIST = 2;
    private static final int FAIL = 3;
    public static final String TAG = "MainActivity";

        @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            String showInfo = "";
            switch (msg.what)
            {
                case COMPLETED:
                    showInfo = "登录成功";
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("userName", userName)
                        .putString("password", password)
                        .apply();
                    break;
                case NOTEXIST:
                    showInfo = "用户名不存在";
                    break;
                case WRONG:
                    showInfo = "密码错误";
                    break;
                default:
                    showInfo = "登录失败";
                    break;
            }
            Toast.makeText(getApplicationContext(), showInfo, Toast.LENGTH_SHORT).show();
            if(msg.what == COMPLETED)
            {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                BlogLab blogLab = BlogLab.getInstance();
                blogLab.userName = userName;
                startActivity(intent);
            }
        }
    };

    //初始化界面
    private void initView()
    {
        sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
        boolean rememberPassword = sp.getBoolean("rememberPassword", false);
        userName = sp.getString("userName", null);
        mUserName = (EditText) findViewById(R.id.userName);
        mPassword = (EditText) findViewById(R.id.password);
        mCheckBox = (CheckBox) findViewById(R.id.rememberPassword);
        mLogin = (Button) findViewById(R.id.login);
        mRegister = (Button) findViewById(R.id.toregister);
        mCheckBox.setChecked(rememberPassword);
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sp.edit().putBoolean("rememberPassword", isChecked).commit();
            }
        });
        if(userName != null)
        {
            mUserName.setText(userName);
            if(rememberPassword)
            {
                mPassword.setText(sp.getString("password", ""));
            }
        }

    }


    private class Login extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            String status = null;
            Message msg = new Message();
            try
            {
                String params = "userName=" + userName + '&' + "password=" + password;
                status = Utils.getStatus(params, "user/login");

            }catch (Exception e)
            {
                e.printStackTrace();
            }
            finally {
                if(status != null) {
                    if (status.equals("ok"))  //登录成功
                    {
                        msg.what = COMPLETED;
                        Log.d(TAG, "doInBackground: 登录成功");
//                    Intent intent = new Intent();
//                    intent.putExtra("userId", userId);
//                    startActivity(intent);
                    } else if (status.equals("wrong password")) {
                        msg.what = WRONG;
                        Log.d(TAG, "doInBackground: 密码错误");
                    } else if (status.equals("userName not exist")) {
                        msg.what = NOTEXIST;
                        Log.d(TAG, "doInBackground: 用户名不存在");
                    }else{
                        msg.what = FAIL;
                    }
                }
                else
                {
                    msg.what = FAIL;
                    Log.d(TAG, "doInBackground: 登录失败");
                }
                handler.sendMessage(msg);
            }
            return null;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        initView();

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*获取用户名和密码*/
                userName = mUserName.getText().toString();
                password = mPassword.getText().toString();
                if(userName == null || userName.length() <= 0){
                    Toast.makeText(getApplicationContext(),"请输入账号", Toast.LENGTH_LONG).show();
                    return;
                }
                if(password == null|| password.length() <= 0){
                    Toast.makeText(getApplicationContext(),"请输入密码", Toast.LENGTH_LONG).show();
                    return;
                }

                new Login().execute();
            }
        });
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
    }

}
