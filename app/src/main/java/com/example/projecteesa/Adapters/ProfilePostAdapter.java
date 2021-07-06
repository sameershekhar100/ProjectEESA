package com.example.projecteesa.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projecteesa.Posts.Post;
import com.example.projecteesa.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ProfilePostAdapter extends RecyclerView.Adapter<ProfilePostAdapter.PostHolder> {
    ArrayList<Post> posts;
    Context context;

    public ProfilePostAdapter(ArrayList<Post> posts,Context context) {
        this.posts = posts;
        this.context=context;
    }

    @NonNull
    @NotNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View myView= LayoutInflater.from(parent.getContext()).inflate(R.layout.my_post_item,parent,false);
        PostHolder holder=new PostHolder(myView);
        return holder;    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ProfilePostAdapter.PostHolder holder, int position) {

        Post post=posts.get(position);
        Glide.with(context).load(post.getImageURL()).into(holder.postImg);

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder{
        ImageView postImg;
        public PostHolder(@NonNull @NotNull View itemView) {
            super(itemView);
/*
            postProfileHeader=itemView.findViewById(R.id.post_header_img);*/
            postImg=itemView.findViewById(R.id.userImg);


        }
    }
}
