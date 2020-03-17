package com.example.myapplication;

import com.example.myapplication.network.User;

import java.util.List;

public class UserDB {

    private static UserDB mInstance;
    private List<User> userList;
    private User lastUser;
    private String currentName;

    public static UserDB getInstance() {
        if (mInstance == null) {
            mInstance = new UserDB();
        }
        return mInstance;
    }

    public User getLastUser() {
        return lastUser;
    }

    public void setLastUser(User lastUser) {
        this.lastUser = lastUser;
    }

    public String getCurrentName() {
        return currentName;
    }

    //Getters and Setters
    public void setCurrentName(String currentName) {
        this.currentName = currentName;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

}

