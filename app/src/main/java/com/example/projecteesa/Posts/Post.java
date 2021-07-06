package com.example.projecteesa.Posts;

import java.util.ArrayList;

public class Post {
    private String UserID;
    private String caption;
    private String imageURL;
    private String name;
    private long timestamp;
    private ArrayList<String> likes;

    public Post(String userID,String name, String caption, String imageURL, long timestamp, ArrayList<String> likes) {
        UserID = userID;
        this.caption = caption;
        this.imageURL = imageURL;
        this.timestamp = timestamp;
        this.likes = likes;
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Post() {
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
