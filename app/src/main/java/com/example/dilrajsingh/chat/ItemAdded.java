package com.example.dilrajsingh.chat;

/**
 * Created by Dilraj Singh on 07/12/2016.
 */
public class ItemAdded {

    private String name, phone;

    public ItemAdded()
    {}
    public ItemAdded(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getphone() {
        return phone;
    }

    public void setphone(String phone) {
        this.phone = phone;
    }
}
