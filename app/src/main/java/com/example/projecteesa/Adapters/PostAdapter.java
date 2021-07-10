package com.example.projecteesa.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projecteesa.Fragment.ProfileFragment;
import com.example.projecteesa.Posts.Post;
import com.example.projecteesa.R;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {
    ArrayList<Post> posts;
    Context context;

    public PostAdapter(ArrayList<Post> posts,Context context) {
        this.posts = posts;
        this.context=context;
    }

    @NonNull
    @NotNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View myView= LayoutInflater.from(parent.getContext()).inflate(R.layout.post_element,parent,false);
        PostHolder holder=new PostHolder(myView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PostAdapter.PostHolder holder, int position) {
        holder.mainCard.setAnimation(AnimationUtils.loadAnimation(context, R.anim.post_animation));
        Post post=posts.get(position);
        holder.postHeader.setText(post.getName());
        Glide.with(context).load(post.getImageURL()).into(holder.postImg);
        holder.likes.setText(post.getLikes().size()+" likes");
        holder.captionHeader.setText(post.getName()+": ");
        holder.caption.setText(post.getCaption());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder {
        ImageView postImg,postProfileHeader,likeBtn,commentBtn;
        TextView caption,likes,captionHeader,postHeader;
        CardView mainCard;
        public PostHolder(@NonNull @NotNull View itemView) {
            super(itemView);
/*
            postProfileHeader=itemView.findViewById(R.id.post_header_img);
*/
            postHeader=itemView.findViewById(R.id.post_header);
            postImg=itemView.findViewById(R.id.post_image);
            likeBtn=itemView.findViewById(R.id.post_like_btn);
            commentBtn=itemView.findViewById(R.id.post_comment_btn);
            likes=itemView.findViewById(R.id.like_number_text);
            captionHeader=itemView.findViewById(R.id.caption_header);
            caption=itemView.findViewById(R.id.post_caption);
            mainCard = itemView.findViewById(R.id.mainCard);
        }
    }
}
