package com.example.projecteesa.Posts;

import java.util.ArrayList;

@SuppressWarnings("serial")

public class Post {
    private String postId;
    private String uid;
    private String caption;
    private String imageUrl;
    private String name;
    private String userImg;
    private long timestamp;
    private ArrayList<String> likes;

    public Post(String postID, String userID, String name, String userProfile, String caption, String imageURL, long timestamp, ArrayList<String> likes) {
        uid = userID;
        this.postId = postID;
        this.caption = caption;
        this.imageUrl = imageURL;
        this.userImg = userProfile;
        this.timestamp = timestamp;
        this.likes = likes;
        this.name = name;
    }

    public Post() {
        //Required because of firestore
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<String> likes) {
        this.likes = likes;
    }
}
