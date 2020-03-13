package com.example.weibo.ui.dashboard;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.weibo.Info.BlogLab;
import com.example.weibo.R;
import com.example.weibo.ui.home.HomeFragment;
import com.example.weibo.ui.parent.BaseBlogFragment;

public class DashboardFragment extends BaseBlogFragment {

    public static boolean needRefresh = true;

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        needRefresh = true;
        View root = super.onCreateView(inflater, container, savedInstanceState);
        BlogLab blogLab = BlogLab.getInstance();
        params = "userName=" + blogLab.userName + "&myblog=yes";
        initToolbar(root, "我的微博", params);
        return root;
    }

    public void onStart() {
        super.onStart();
        if(needRefresh)
        {
            new Fetcher().execute(params);
            needRefresh = false;
        }
    }
}