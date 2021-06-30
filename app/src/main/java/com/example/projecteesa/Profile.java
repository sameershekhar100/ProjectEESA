package com.example.projecteesa;

public class Profile {
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
}
