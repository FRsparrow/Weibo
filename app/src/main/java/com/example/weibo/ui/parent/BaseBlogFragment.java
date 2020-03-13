package com.example.weibo.ui.parent;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weibo.Info.BlogLab;
import com.example.weibo.R;
import com.example.weibo.Utils;
import com.example.weibo.ui.addblog.AddBlogActivity;
import com.example.weibo.ui.home.BlogAdapter;
import com.example.weibo.ui.home.HomeFragment;
import com.example.weibo.ui.home.HomeViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class BaseBlogFragment extends Fragment {

    protected BlogLab blogLab;
    private BlogAdapter adapter = null;

    public static final String TAG = "MainActivity";
    public static String params = "";

    private static final int SUCCESS = 0;
    private static final int FAILED = 1;
    private static final int EMPTY = 2;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            String showInfo = "";
            switch (msg.what)
            {
                case SUCCESS:
                    Log.d(TAG, "handleMessage: 即将执行showdata");
                    showData();
                    showInfo = "刷新成功";
                    Log.d(TAG, "handleMessage: 获取微博列表成功");
                    break;
                case FAILED:
                    showInfo = "获取微博列表失败";
                    Log.d(TAG, "handleMessage: 获取微博列表失败");
                    break;
                case EMPTY:
                    showInfo = "这里空空如也";
                    break;
            }
            Toast.makeText(getContext(), showInfo, Toast.LENGTH_SHORT).show();
            return false;
        }
    });


    private void showData()
    {
        ArrayList blogList = blogLab.blogList;
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.bloglist);
//        if(getActivity() instanceof MainActivity)
//            Log.d(TAG, "showData: activity为Main");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if(recyclerView == null) {
            Log.d(TAG, "showData: recyclerview为空");
            return;
        }
        recyclerView.setLayoutManager(linearLayoutManager);
        Log.d(TAG, "showData: list size:" + blogList.size());
//        WeatherAdapter adapter = new WeatherAdapter(weatherInfos, isPhone);
        if(adapter == null) {
            Log.d(TAG, "showData: 第一次建立");
            adapter = new BlogAdapter(getContext());
            recyclerView.setAdapter(adapter);
        }else
        {
            Log.d(TAG, "showData: 更新了");
            adapter.notifyDataSetChanged();
        }

    }

    public class Fetcher extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String pathOnServer = "blog";
            Message message = new Message();
            try {
                Log.d(TAG, "doInBackground: 参数是：" + params[0]);
                JSONObject res = Utils.getJSONObject(pathOnServer, params[0]);
                if(res.getString("status").equals("ok"))
                {
                    message.what = SUCCESS;
                    JSONObject data = res.getJSONObject("data");
                    JSONArray blog = data.getJSONArray("blog");
                    JSONArray like = data.getJSONArray("like");
                    blogLab.setBlogList(blog, like);
                    Log.d(TAG, "doInBackground: 微博列表大小：" + blogLab.blogList.size());
                }else if(res.getString("status").equals("failed")){
                    message.what = EMPTY;
                }
                else
                {
                    message.what = FAILED;
                }
            } catch (Exception e) {
                e.printStackTrace();
                message.what = FAILED;
            }finally {
                handler.sendMessage(message);
            }
            return null;
        }

    }

    public void initToolbar(View root, String title, final String... params)
    {
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.home_menu);
        toolbar.setTitle(title);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.refresh:
                        //点击刷新
                        new Fetcher().execute(params);
                        break;
                    case R.id.add:
                        //发布微博
                        Intent intent = new Intent(getContext(), AddBlogActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
//        initToolbar(root);
        blogLab = BlogLab.getInstance();
        params = "userName=" + blogLab.userName;

        Log.d(TAG, "onCreateView: 我又被执行了");
        return root;
    }


}
