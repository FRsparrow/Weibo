package com.example.weibo.ui.comment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weibo.Info.Blog;
import com.example.weibo.Info.BlogLab;
import com.example.weibo.Info.Comment;
import com.example.weibo.R;
import com.example.weibo.Utils;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Comment> comments;
    private final int BLOGVIEW = 0;
    private final int COMMENTVIEW = 1;

    public CommentAdapter()
    {
        comments = BlogLab.getInstance().commentList;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0)
            return BLOGVIEW;
        else
            return COMMENTVIEW;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == BLOGVIEW)
        {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.comment_blog_item, parent, false);
            BlogViewHolder blogViewHolder = new BlogViewHolder(view);
            return blogViewHolder;
        }
        else if(viewType == COMMENTVIEW)
        {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.comment_item, parent, false);
            CommentViewHolder commentViewHolder = new CommentViewHolder(view);
            return commentViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof BlogViewHolder)
        {
            BlogLab blogLab = BlogLab.getInstance();
            Blog blog = blogLab.blogList.get(blogLab.blogPosition);
            BlogViewHolder bvh = (BlogViewHolder)holder;
            bvh.userName.setText(blog.userName);
            bvh.blogContent.setText(blog.content);
            bvh.blogTime.setText(Utils.FormatTime(blog));
            bvh.commentCountTextView.setText("评论" + blogLab.commentList.size());
        }
        else if(holder instanceof CommentViewHolder)
        {
            Comment comment = comments.get(position - 1);
            CommentViewHolder cvh = (CommentViewHolder)holder;
            cvh.commentUserName.setText(comment.userName);
            cvh.commentContent.setText(comment.content);
            cvh.commentTime.setText(Utils.FormatTime(comment));
        }
    }

    @Override
    public int getItemCount() {
        return comments.size() + 1;
    }

    public class BlogViewHolder extends RecyclerView.ViewHolder
    {
        ImageView blogHeadImage;
        TextView userName;
        TextView blogTime;
        TextView blogContent;
        TextView commentCountTextView;

        public BlogViewHolder(@NonNull View itemView) {
            super(itemView);
            blogHeadImage = (ImageView) itemView.findViewById(R.id.comment_blog_head_image);
            userName = (TextView) itemView.findViewById(R.id.comment_blog_userName);
            blogTime = (TextView) itemView.findViewById(R.id.comment_blog_time);
            blogContent = (TextView) itemView.findViewById(R.id.comment_blog_content);
            commentCountTextView = (TextView) itemView.findViewById(R.id.comment_count);
        }
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder
    {
        ImageView commentHeadImage;
        TextView commentUserName;
        TextView commentTime;
        TextView commentContent;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commentHeadImage = (ImageView) itemView.findViewById(R.id.comment_head_image);
            commentUserName = (TextView) itemView.findViewById(R.id.comment_userName);
            commentTime = (TextView) itemView.findViewById(R.id.comment_time);
            commentContent = (TextView) itemView.findViewById(R.id.comment_content);
        }
    }
}
