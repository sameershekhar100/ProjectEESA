package com.example.projecteesa.Adapters;

import java.util.ArrayList;

public interface PostItemClicked {
    public void onLikeClicked(ArrayList<String> likes, String postID);

    public void onBookmarkClicked(ArrayList<String> savedPosts, String uid);

    public void onOwnerProfileClicked(String uid);
}
