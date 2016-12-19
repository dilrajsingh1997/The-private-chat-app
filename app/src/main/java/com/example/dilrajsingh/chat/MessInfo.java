package com.example.dilrajsingh.chat;

/**
 * Created by Dilraj Singh on 08/12/2016.
 */
public class MessInfo {

    String date, token, stamp, message;

    MessInfo(){}

    public MessInfo(String date, String message, String stamp, String token) {
        this.date = date;
        this.message = message;
        this.stamp = stamp;
        this.token = token;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStamp() {
        return stamp;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
