package com.example.coursework.model.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.coursework.model.User;

import java.util.List;

@Dao
public interface UserDAO {
    @Insert
    long  insert(User user);
    @Update
    void update(User user);
    @Delete
    void delete(User user);

    @Query("SELECT * FROM User WHERE id ==:userId LIMIT 1")
    LiveData<User> getUserById(long userId);

    @Query("SELECT * FROM User WHERE userName ==:name LIMIT 1")
    User getUserByName(String name);

    @Query("SELECT * FROM User")
    LiveData<List<User>> getAllUsers();
}
