package com.example.weibo.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weibo.Info.Blog;
import com.example.weibo.Info.BlogLab;
import com.example.weibo.R;
import com.example.weibo.Utils;
import com.example.weibo.ui.comment.CommentActivity;

import java.util.List;

import static com.example.weibo.ui.home.HomeFragment.TAG;


public class BlogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Blog> blogList;
    private Context context;
    private int currentPosition = 0;

    private class Sender extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String... strings) {
            BlogLab blogLab = BlogLab.getInstance();
            String params = "userName=" + blogLab.userName + "&blogId=" + strings[0];
            String pathOnServer = "blog/like";
            try {
                String status = Utils.getStatus(params, pathOnServer);
                if(status == null)
                    Log.d(TAG, "onClick: 空");
                if(status != null && status.equals("ok"))
                    Log.d(TAG, "onClick: 点赞或取消点赞成功");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public BlogAdapter(Context context){
        blogList = BlogLab.getInstance().blogList;
        Log.d(TAG, "BlogAdapter: size:" + blogList.size());
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        currentPosition = position;
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        int position = currentPosition;
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.blog_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
//        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
//        Log.d(TAG, "onBindViewHolder: " + blogList.size());
//        BlogLab blogLab = BlogLab.getInstance();
        final Blog blog = blogList.get(position);
        final ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.userName.setText(blog.userName);
        viewHolder.blogTime.setText(Utils.FormatTime(blog));
        viewHolder.blogContent.setText(blog.content);
        viewHolder.likeCount.setText(blog.likeCount);
        viewHolder.commentCount.setText(blog.commentCount);
        if(blog.liked) {
            viewHolder.like.setImageResource(R.drawable.liked);
            Log.d(TAG, "onBindViewHolder: 被点赞了：" + blog.id);
        }
        else{
            viewHolder.like.setImageResource(R.drawable.like);
        }
        viewHolder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int likeCount = Integer.parseInt(viewHolder.likeCount.getText().toString());
                //取消点赞
                if(blog.liked)
                {
                    viewHolder.like.setImageResource(R.drawable.like);
                    --likeCount;

                }//点赞
                else
                {
                    viewHolder.like.setImageResource(R.drawable.liked);
                    ++likeCount;
                }
                viewHolder.likeCount.setText(likeCount + "");
                blog.liked = likeCount > 0;
                blog.likeCount = likeCount + "";
                new Sender().execute(blog.id);
            }
        });
        viewHolder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlogLab.getInstance().blogPosition = position;
                Intent intent = new Intent(context, CommentActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + blogList.size());
        return blogList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView blogHeadImage;
        TextView userName;
        TextView blogTime;
        TextView blogContent;
        ImageView like;
        TextView likeCount;
        ImageView comment;
        TextView commentCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            blogHeadImage = (ImageView) itemView.findViewById(R.id.blog_head_image);
            userName = (TextView) itemView.findViewById(R.id.blog_userName);
            blogTime = (TextView) itemView.findViewById(R.id.blog_time);
            blogContent = (TextView) itemView.findViewById(R.id.blog_content);
            like = (ImageView) itemView.findViewById(R.id.blog_like_image);
            likeCount = (TextView) itemView.findViewById(R.id.blog_like);
            comment = (ImageView) itemView.findViewById(R.id.blog_comment_image);
            commentCount = (TextView) itemView.findViewById(R.id.blog_comment);
        }
    }
}
