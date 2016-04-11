package com.yinnonbar.securedwallet;

/**
 * Created by Yinnon Bratspiess on 06/04/2016.
 */
public class User {
    private int id;
    private String userName;
    private String password;
    User() {}
    User (String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
