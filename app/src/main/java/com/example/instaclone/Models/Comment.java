package com.example.instaclone.Models;

public class Comment {
    private String username;
    private String date;
    private String imageUrl;
    private String comment;

    public Comment() {
    }

    public Comment(String username, String date, String imageUrl, String comment) {
        this.username = username;
        this.date = date;
        this.imageUrl = imageUrl;
        this.comment = comment;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
