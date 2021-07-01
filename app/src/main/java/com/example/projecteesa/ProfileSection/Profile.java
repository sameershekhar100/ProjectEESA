package com.example.projecteesa.ProfileSection;
import java.io.Serializable;

@SuppressWarnings("serial")

public class Profile implements Serializable {
    private String name="";
    private String email;
    private String phoneNO;

    public Profile(String name, String email ,String phoneNO) {
        this.name = name;
        this.email = email;
        this.phoneNO=phoneNO;
    }
    public Profile(){

    }

    public String getEmail() {
        return email;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNO(String phoneNO) {
        this.phoneNO = phoneNO;
    }
}
