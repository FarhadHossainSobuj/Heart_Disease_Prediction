package com.example.tensortest.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserAccountDao {

    @Insert
    void insert(UserAccount account);

    @Query("SELECT * FROM useraccounts WHERE useraccounts.userId LIKE :username")
    UserAccount getAccount(String username);
}