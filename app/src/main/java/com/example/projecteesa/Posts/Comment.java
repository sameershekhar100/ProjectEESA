package com.example.projecteesa.Posts;

public class Comment {
    private String UserID,message;

    public Comment(String userID, String message) {
        UserID = userID;
        this.message = message;
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

    public void setMessage(String message) {
        this.message = message;
    }
}
