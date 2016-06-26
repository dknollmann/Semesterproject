package com.semesterdomain.semesterprojekt;

import java.io.Serializable;

/**
 * The type User.
 */
public class User implements Serializable {

    private int userId;
    private String username;

    /**
     * Instantiates a new User.
     *
     * @param username the username of the User
     */
    public User(String username) {
        this.username = username;
        this.userId = 1;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets user id.
     *
     * @param userId the user id
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }
}
