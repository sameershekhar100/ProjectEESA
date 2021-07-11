package com.example.projecteesa.Posts;

import java.util.ArrayList;

public class Post {
    private String postID;
    private String UserID;
    private String caption;
    private String imageURL;
    private String name;
    private String userProfile;
    private long timestamp;
    private ArrayList<String> likes;

    public Post(String postID, String userID,String name, String userProfile, String caption, String imageURL, long timestamp, ArrayList<String> likes) {
        UserID = userID;
        this.postID=postID;
        this.caption = caption;
        this.imageURL = imageURL;
        this.userProfile=userProfile;
        this.timestamp = timestamp;
        this.likes = likes;
        this.name=name;
    }

    public Post()
    {
        //Required because of firestore
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
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
