package com.example.projecteesa.ProfileSection;
import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")

public class Profile implements Serializable {
    private String name;
    private String bio;
    private String phoneNO;
    private String image;
    private ArrayList<String> savedPost;


    public Profile(String name, String bio, String phoneNO,ArrayList<String> savedPost) {
        this.name = name;
        this.bio = bio;
        this.phoneNO=phoneNO;
        this.savedPost=savedPost;
        image="";
    }

    public Profile(){

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

    public String getPhoneNO() {
        return phoneNO;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPhoneNO(String phoneNO) {
        this.phoneNO = phoneNO;
    }
}
