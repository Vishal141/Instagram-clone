package com.example.instaclone.Models;

public class User {
    private String id;
    private String username;
    private String email;
    private String fullname;
    private String imgUrl;
    private String bio;

    public User(String id, String username, String email, String fullName, String imgUrl, String bio) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.fullname = fullName;
        this.imgUrl = imgUrl;
        this.bio = bio;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullName) {
        this.fullname = fullName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
