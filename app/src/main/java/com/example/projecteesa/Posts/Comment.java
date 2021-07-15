package com.example.projecteesa.Posts;

public class Comment {
    private String UserID,message,imageURL;
    long time;
    //mandatory fun for firestore
    public Comment(){

    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Comment(String userID, String message, long time, String imageURL) {
        this.UserID = userID;
        this.message = message;
        this.time=time;
        this.imageURL=imageURL;


    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
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
