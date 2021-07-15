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

import de.hdodenhof.circleimageview.CircleImageView;

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
        View myView= LayoutInflater.from(parent.getContext()).inflate(R.layout.post_element,parent,false);
        PostHolder holder=new PostHolder(myView);
        return holder;    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ProfilePostAdapter.PostHolder holder, int position) {

        Post post=posts.get(position);
        Glide.with(context).load(post.getImageURL()).into(holder.postImg);
        holder.captionHeader.setText(post.getName()+": ");
        holder.captionTv.setText(post.getCaption());
        if (post.getUserProfile() != null && !post.getUserProfile().isEmpty())
            Glide.with(context).load(post.getUserProfile()).into(holder.ownerImg);
        else
            holder.ownerImg.setImageResource(R.drawable.user_profile_placeholder);
        holder.ownerNameTv.setText(post.getName());
        holder.likesTv.setText(post.getLikes().size()+" likes");
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder{
        ImageView postImg;
        CircleImageView ownerImg;
        TextView ownerNameTv;
        TextView captionHeader;
        TextView captionTv;
        TextView likesTv;

        public PostHolder(@NonNull @NotNull View itemView) {
            super(itemView);
/*
            postProfileHeader=itemView.findViewById(R.id.post_header_img);*/
            postImg=itemView.findViewById(R.id.post_image);
            ownerImg = itemView.findViewById(R.id.post_header_img);
            ownerNameTv = itemView.findViewById(R.id.post_header);
            captionTv = itemView.findViewById(R.id.post_caption);
            captionHeader = itemView.findViewById(R.id.caption_header);
            likesTv = itemView.findViewById(R.id.like_number_text);
        }
    }
}
