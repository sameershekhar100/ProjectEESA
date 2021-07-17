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
import com.example.projecteesa.utils.AccountsUtil;
import com.example.projecteesa.utils.TimeUtils;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilePostAdapter extends RecyclerView.Adapter<ProfilePostAdapter.PostHolder> {
    ArrayList<Post> posts;
    Context context;
    PostItemClicked listener;
    ArrayList<String> savedPosts;
    public ProfilePostAdapter(ArrayList<Post> posts,Context context,PostItemClicked listener) {
        this.posts = posts;
        this.context=context;
        this.listener=listener;
        if(AccountsUtil.fetchData()!=null) {
            savedPosts = AccountsUtil.fetchData().getSavedPost();
        }
    }

    @NonNull
    @NotNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View myView= LayoutInflater.from(parent.getContext()).inflate(R.layout.post_element,parent,false);
        PostHolder holder=new PostHolder(myView);
        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> likes=posts.get(holder.getAdapterPosition()).getLikes();
                String postID=posts.get(holder.getAdapterPosition()).getPostID();
                String uid= FirebaseAuth.getInstance().getUid();
                if(likes.contains(uid))
                {
                    likes.remove(uid);
                    listener.onLikeClicked(likes,postID);
                    holder.likeBtn.setImageResource(R.drawable.ic_like_border);
                    if(likes.size()<2)
                    {
                        holder.likesTv.setText(likes.size()+" like");
                    }
                    else {
                        holder.likesTv.setText(likes.size() + " likes");
                    }
                }
                else
                {
                    likes.add(uid);
                    listener.onLikeClicked(likes,postID);
                    holder.likeBtn.setImageResource(R.drawable.ic_like);
                    if(likes.size()<2)
                    {
                        holder.likesTv.setText(likes.size()+" like");
                    }
                    else {
                        holder.likesTv.setText(likes.size() + " likes");
                    }
                }
            }
        });
        holder.bookmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert AccountsUtil.fetchData() != null;
                String path="AllPost/"+posts.get(holder.getAdapterPosition()).getPostID();
                if (savedPosts == null){
                    savedPosts = new ArrayList<>();
                    savedPosts.add(path);
                    holder.bookmarkBtn.setImageResource(R.drawable.ic_bookmark_black);
                    listener.onBookmarkClicked(savedPosts, AccountsUtil.getUID());
                    return;
                }
                if(savedPosts.contains(path))
                {
                    savedPosts.remove(path);
                    holder.bookmarkBtn.setImageResource(R.drawable.ic_bookmark_border);
                    listener.onBookmarkClicked(savedPosts,AccountsUtil.getUID());
                }
                else
                {
                    savedPosts.add(path);
                    holder.bookmarkBtn.setImageResource(R.drawable.ic_bookmark_black);
                    listener.onBookmarkClicked(savedPosts,AccountsUtil.getUID());
                }

            }
        });
        holder.commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postID=posts.get(holder.getAdapterPosition()).getPostID();
                listener.onCommentClicked(postID);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ProfilePostAdapter.PostHolder holder, int position) {
        ArrayList<String> saved;
        Post post=posts.get(position);
        if (AccountsUtil.fetchData() !=null)
            saved= AccountsUtil.fetchData().getSavedPost();
        else
            saved = new ArrayList<>();
        Glide.with(context).load(post.getImageURL()).into(holder.postImg);
        holder.captionHeader.setText(post.getName()+": ");
        holder.captionTv.setText(post.getCaption());
        if (post.getUserProfile() != null && !post.getUserProfile().isEmpty())
            Glide.with(context).load(post.getUserProfile()).into(holder.ownerImg);
        else
            holder.ownerImg.setImageResource(R.drawable.user_profile_placeholder);
        holder.ownerNameTv.setText(post.getName());
        holder.likesTv.setText(post.getLikes().size()+" likes");
        holder.time.setText(TimeUtils.getTime(post.getTimestamp()));
        if(post.getLikes().contains(AccountsUtil.getUID()))
        {
            holder.likeBtn.setImageResource(R.drawable.ic_like);
        }
        else
        {
            holder.likeBtn.setImageResource(R.drawable.ic_like_border);
        }
        if(saved!=null && saved.contains("AllPost/"+post.getPostID()))
        {
            holder.bookmarkBtn.setImageResource(R.drawable.ic_bookmark_black);
        }
        else {
            holder.bookmarkBtn.setImageResource(R.drawable.ic_bookmark_border);
        }
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
        TextView time;
        ImageView likeBtn,commentBtn,bookmarkBtn;
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
            time= itemView.findViewById(R.id.post_time);
            likeBtn=itemView.findViewById(R.id.post_like_btn);
            commentBtn=itemView.findViewById(R.id.post_comment_btn);
            bookmarkBtn=itemView.findViewById(R.id.post_save_btn);
        }
    }
}
