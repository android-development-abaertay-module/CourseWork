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
    void  insert(GoalSeasonal goalAnnual);
    @Update
    void update(GoalSeasonal goalAnnual);
    @Delete
    void delete(GoalSeasonal goalAnnual);

    @Query("SELECT * FROM GoalSeasonal")
    LiveData<List<GoalSeasonal>> getAllSeasonalGoals();

    @Query("SELECT * FROM GoalSeasonal WHERE userId ==:userId")
    LiveData<List<GoalSeasonal>> getAllSeasonalGoalsForUser(int userId);
}
