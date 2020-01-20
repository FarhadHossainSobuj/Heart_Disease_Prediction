package com.example.tensortest.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import static com.example.tensortest.data.UserAccount.TABLE_NAME;

@Entity(tableName = TABLE_NAME)
public class UserAccount {

    public static final String TABLE_NAME = "useraccounts";

    @PrimaryKey
    @NonNull
    String userId;
    String password;


    public UserAccount()
    {

    }
    public UserAccount(String username, String pwd)
    {
        this.userId = username;
        this.password = pwd;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
