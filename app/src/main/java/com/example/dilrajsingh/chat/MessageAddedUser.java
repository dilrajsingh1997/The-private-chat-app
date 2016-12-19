package com.example.dilrajsingh.chat;

/**
 * Created by Dilraj Singh on 15/12/2016.
 */
public class MessageAddedUser {

    String message;
    String token;

    public MessageAddedUser(){}

    public MessageAddedUser(String message, String token) {
        this.message = message;
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
