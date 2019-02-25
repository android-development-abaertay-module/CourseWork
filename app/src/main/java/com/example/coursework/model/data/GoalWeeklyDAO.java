package com.example.coursework.model.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import com.example.coursework.model.GoalWeekly;

import java.util.List;

@Dao
public interface GoalWeeklyDAO {
    @Insert
    long insert(GoalWeekly goalWeekly);
    @Update
    void update(GoalWeekly goalWeekly);
    @Delete
    void delete(GoalWeekly goalWeekly);

    @Query("SELECT * FROM GoalWeekly WHERE userId ==:userId")
    LiveData<List<GoalWeekly>> getAllWeeklyGoalsForUser(int userId);

    @Query("SELECT * FROM GoalWeekly WHERE userId ==:userId AND goalAchieved = 0 LIMIT 1")
    LiveData<GoalWeekly> getCurrentWeeklyGoalsForUser(int userId);
}
