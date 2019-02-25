package com.example.coursework.model.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.coursework.model.GoalSeasonal;

import java.util.List;

@Dao
public interface GoalSeasonalDAO {
    @Insert
    long  insert(GoalSeasonal goalSeasonal);
    @Update
    void update(GoalSeasonal goalSeasonal);
    @Delete
    void delete(GoalSeasonal goalSeasonal);

    @Query("SELECT * FROM GoalSeasonal WHERE userId ==:userId")
    LiveData<List<GoalSeasonal>> getAllSeasonalGoalsForUser(int userId);

    @Query("SELECT * FROM GoalSeasonal WHERE userId ==:userId AND goalAchieved = 0 LIMIT 1")
    LiveData<GoalSeasonal> getCurrentSeasonalGoalsForUser(int userId);
}
