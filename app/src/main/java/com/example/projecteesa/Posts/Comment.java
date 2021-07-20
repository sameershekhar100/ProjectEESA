package com.example.projecteesa.Posts;

public class Comment {
    private String uid, message;
    long time;

    //mandatory fun for firestore
    public Comment() {

    }


    public Comment(String userID, String message, long time) {
        this.uid = userID;
        this.message = message;
        this.time = time;


    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMessage() {
        return message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
