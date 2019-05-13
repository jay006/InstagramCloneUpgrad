package com.upgrad.instagramclone.model;

public class Post {

    private String caption = "";
    private long uploadTimestamp  = 0;
    private String imageUrl = "";
    private User user = null;

    public Post(String caption, long uploadTimestamp, String imageUrl, User user) {
        this.caption = caption;
        this.uploadTimestamp = uploadTimestamp;
        this.imageUrl = imageUrl;
        this.user = user;
    }

    public String getCaption() {
        return caption;
    }

    public long getUploadTimestamp() {
        return uploadTimestamp;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public User getUser() {
        return user;
    }
}
