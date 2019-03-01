package com.example.coursework.model.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.coursework.model.GoalAnnual;

import java.util.List;

@Dao
public interface GoalAnnualDAO {
    @Insert
    long  insert(GoalAnnual goalAnnual);
    @Update
    void update(GoalAnnual goalAnnual);
    @Delete
    void delete(GoalAnnual goalAnnual);

    @Query("SELECT * FROM GoalAnnual WHERE userId ==:userId")
    LiveData<List<GoalAnnual>> getAllAnnualGoalsForUser(int userId);

    @Query("SELECT * FROM GoalAnnual WHERE userId ==:userId AND goalAchieved = 0 LIMIT 1")
    LiveData<GoalAnnual> getCurrentAnnualGoalsForUser(int userId);

    @Query("SELECT * FROM GoalAnnual WHERE userId ==:userId ORDER BY dateCreated DESC LIMIT 1")
    LiveData<GoalAnnual> getMostRecentAnnualGoalForUser(long userId);
}
