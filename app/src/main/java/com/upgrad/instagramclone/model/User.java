package com.upgrad.instagramclone.model;

import java.io.Serializable;

public class User implements Serializable {

    private String userId = "";
    private String userName = "";
    private String userEmail = "";
    private String userImage = "";

    public User(){}

    public User(String userId, String userName, String userEmail, String userImage) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userImage = userImage;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserImage() {
        return userImage;
    }
}
