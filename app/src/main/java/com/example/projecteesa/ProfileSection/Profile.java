package com.example.projecteesa.ProfileSection;
import java.io.Serializable;

@SuppressWarnings("serial")

public class Profile implements Serializable {
    private String name;
    private String bio;
    private String phoneNO;
    private String image;


    public Profile(String name, String bio, String phoneNO) {
        this.name = name;
        this.bio = bio;
        this.phoneNO=phoneNO;
        image="";
    }
    public Profile(){

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
