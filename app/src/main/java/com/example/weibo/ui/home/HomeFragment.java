package com.example.weibo.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weibo.Info.Blog;
import com.example.weibo.Info.BlogLab;
import com.example.weibo.R;
import com.example.weibo.Utils;
import com.example.weibo.ui.addblog.AddBlogActivity;
import com.example.weibo.ui.main.MainActivity;
import com.example.weibo.ui.parent.BaseBlogFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseBlogFragment {


    public static final String TAG = "MainActivity";
    public static boolean needRefresh = true;

//    private Handler handler = new Handler(new Handler.Callback() {
//        @Override
//        public boolean handleMessage(@NonNull Message msg) {
//            switch (msg.what)
//            {
//                case SUCCESS:
//                    Log.d(TAG, "handleMessage: 即将执行showdata");
//                    showData();
//                    Toast.makeText(getContext(), "刷新成功", Toast.LENGTH_SHORT).show();
//                    Log.d(TAG, "handleMessage: 获取微博列表成功");
//                    break;
//                case FAILED:
//                    Toast.makeText(getContext(), "获取微博列表失败", Toast.LENGTH_SHORT).show();
//                    Log.d(TAG, "handleMessage: 获取微博列表失败");
//            }
//            return false;
//        }
//    });
//
//
//    private void showData()
//    {
//        ArrayList blogList = blogLab.blogList;
//        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.bloglist);
////        if(getActivity() instanceof MainActivity)
////            Log.d(TAG, "showData: activity为Main");
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        Log.d(TAG, "showData: 我被执行了1");
//        if(recyclerView == null) {
//            Log.d(TAG, "showData: recyclerview为空");
//            return;
//        }
//        Log.d(TAG, "showData: 我被执行了2");
//        recyclerView.setLayoutManager(linearLayoutManager);
//        Log.d(TAG, "showData: list size:" + blogList.size());
////        WeatherAdapter adapter = new WeatherAdapter(weatherInfos, isPhone);
//        BlogAdapter adapter = new BlogAdapter(getContext());
//        recyclerView.setAdapter(adapter);
//    }
//
//    public class Fetcher extends AsyncTask<String, Void, Void>{
//
//        @Override
//        protected Void doInBackground(String... params) {
//            String pathOnServer = "blog";
//            Message message = new Message();
//            try {
//                JSONObject res = Utils.getJSONObject(pathOnServer, params[0]);
//                if(res.getString("status").equals("ok"))
//                {
//                    message.what = SUCCESS;
//                    JSONArray data = res.getJSONArray("data");
//                    blogLab.setBlogList(data);
//                    Log.d(TAG, "doInBackground: 微博列表大小：" + blogLab.blogList.size());
//                }
//                else
//                {
//                    message.what = FAILED;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                message.what = FAILED;
//            }finally {
//                handler.sendMessage(message);
//            }
//            return null;
//        }
//
//    }
//
//    void initToolbar(View root)
//    {
//        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
//        toolbar.inflateMenu(R.menu.home_menu);
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId())
//                {
//                    case R.id.refresh:
//                        //点击刷新
//                        new Fetcher().execute();
//                        break;
//                    case R.id.add:
//                        //发布微博
//                        Intent intent = new Intent(getContext(), AddBlogActivity.class);
//                        startActivity(intent);
//                        break;
//                    default:
//                        break;
//                }
//                return true;
//            }
//        });
//    }
//
//
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        homeViewModel =
//                ViewModelProviders.of(this).get(HomeViewModel.class);
//        View root = inflater.inflate(R.layout.fragment_home, container, false);
////        final TextView textView = root.findViewById(R.id.text_home);
////        homeViewModel.getText().observe(this, new Observer<String>() {
////            @Override
////            public void onChanged(@Nullable String s) {
////                textView.setText(s);
////            }
////        });
//        initToolbar(root);
//        needRefresh = true;
//        blogLab = BlogLab.getInstance();
//        blogLab.userName = getActivity().getIntent().getStringExtra("userName");
//
//        Log.d(TAG, "onCreateView: 我又被执行了");
//        return root;
//    }
//


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        needRefresh = true;
        View root = super.onCreateView(inflater, container, savedInstanceState);
        initToolbar(root, "最新发布", params);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(needRefresh)
        {
            BlogLab blogLab = BlogLab.getInstance();
            new Fetcher().execute("userName=" + blogLab.userName);
            needRefresh = false;
        }
    }
}