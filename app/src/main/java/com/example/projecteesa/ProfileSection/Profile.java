package com.example.projecteesa.ProfileSection;
import java.io.Serializable;

@SuppressWarnings("serial")

public class Profile implements Serializable {
    private String name="";
    private String BIO;
    private String phoneNO;
    private String image;


    public Profile(String name, String BIO, String phoneNO) {
        this.name = name;
        this.BIO = BIO;
        this.phoneNO=phoneNO;
        image="";
    }
    public Profile(){

    }

    public String getBIO() {
        return BIO;
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

    public void setBIO(String BIO) {
        this.BIO = BIO;
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
