package com.example.projecteesa.Adapters;

import com.example.projecteesa.Posts.Comment;

import java.util.ArrayList;

public interface PostItemClicked {
    public void onLikeClicked(ArrayList<String> likes, String postID);

    public void onBookmarkClicked(ArrayList<String> savedPosts, String uid);

    public void onOwnerProfileClicked(String uid);
  
    public void onCommentClicked(String postID);
}
