package com.example.coursework.model.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.coursework.model.User;

@Dao
public interface UserDAO {
    @Insert
    void  insert(User user);
    @Update
    void update(User user);
    @Delete
    void delete(User user);

    @Query("SELECT * FROM User WHERE id ==:userId LIMIT 1")
    LiveData<User> getAllRoutesInSession(int userId);
}
