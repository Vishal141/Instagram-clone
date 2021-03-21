package com.example.instaclone.Models;

public class Notification {
    private String time;
    private String receiver;
    private String from;
    private String message;

    public Notification() {
    }

    public Notification(String time, String receiver, String from, String message) {
        this.time = time;
        this.receiver = receiver;
        this.from = from;
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
