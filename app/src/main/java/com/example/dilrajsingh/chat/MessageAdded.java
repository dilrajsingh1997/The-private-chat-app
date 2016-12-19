package com.example.dilrajsingh.chat;

/**
 * Created by Dilraj Singh on 08/12/2016.
 */
public class MessageAdded {

    String message;
    String token;
    String read_token;

    MessageAdded(){}

    public MessageAdded(String message, String read_token, String token) {
        this.message = message;
        this.read_token = read_token;
        this.token = token;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRead_token() {
        return read_token;
    }

    public void setRead_token(String read_token) {
        this.read_token = read_token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
