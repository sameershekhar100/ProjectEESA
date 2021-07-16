package com.example.projecteesa.Posts;

public class Comment {
    private String UserID,message;
    long time;
    //mandatory fun for firestore
    public Comment(){

    }




    public Comment(String userID, String message, long time) {
        this.UserID = userID;
        this.message = message;
        this.time=time;



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
