package com.semesterdomain.semesterprojekt;

import java.io.Serializable;

public class User implements Serializable {

    private int userId;
    private String username;

    public User(String username) {
        this.username = username;
        this.userId = 1;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
