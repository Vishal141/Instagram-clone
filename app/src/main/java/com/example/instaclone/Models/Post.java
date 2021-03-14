package com.example.instaclone.Models;

import java.util.StringTokenizer;

public class Post {
    private String username;
    private String profileImgUrl;
    private String postImgUrl;
    private String description;
    private String likes;
    private String comments;

    public Post() {
    }

    public Post(String username, String profileImgUrl, String postImgUrl, String description) {
        this.username = username;
        this.profileImgUrl = profileImgUrl;
        this.postImgUrl = postImgUrl;
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileImgUrl() {
        return profileImgUrl;
    }

    public void setProfileImgUrl(String profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
    }

    public String getPostImgUrl() {
        return postImgUrl;
    }

    public void setPostImgUrl(String postImgUrl) {
        this.postImgUrl = postImgUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
