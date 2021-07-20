package com.example.projecteesa.ProfileSection;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")

public class Profile implements Serializable {
    private String name;
    private String bio;
    private String phoneNo;
    private String userImg;
    private ArrayList<String> savedPost;
    private int passingYear;
    private String branch;
    private String linkedinUrl;
    private String email;
    private String uid;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public int getPassingYear() {
        return passingYear;
    }

    public void setPassingYear(int passingYear) {
        this.passingYear = passingYear;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }


    public Profile(String name, String bio, String phoneNo, ArrayList<String> savedPost) {
        this.name = name;
        this.bio = bio;
        this.phoneNo = phoneNo;
        this.savedPost = savedPost;
        userImg = "";
        this.branch = "";
        this.passingYear = 0;
        this.linkedinUrl = "";
    }

    public Profile(String name, String bio, String phoneNo, ArrayList<String> savedPost, int passingYear, String branch, String linkedinUrl, String uid, String email) {
        this.name = name;
        this.bio = bio;
        this.phoneNo = phoneNo;
        this.savedPost = savedPost;
        this.passingYear = passingYear;
        this.branch = branch;
        this.linkedinUrl = linkedinUrl;
        this.userImg = "";
        this.uid = uid;
        this.email = email;
    }

    public Profile() {

    }

    public ArrayList<String> getSavedPost() {
        return savedPost;
    }

    public void setSavedPost(ArrayList<String> savedPost) {
        this.savedPost = savedPost;
    }

    public String getBio() {
        return bio;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
